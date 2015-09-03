package memoria.snid1.memoria;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.fortysevendeg.swipelistview.*;



import java.util.List;

import memoria.snid1.memoria.database.DAOManager;
import memoria.snid1.memoria.database.DAOMem;
import memoria.snid1.memoria.database.MemAdapter;
import memoria.snid1.memoria.utils.SettingsManager;

import static java.lang.Math.abs;
import static java.lang.Math.min;


public class MainActivity extends FragmentActivity /*implements AdapterView.OnItemLongClickListener*/{

    final Intent intent = new Intent(Intent.ACTION_SEND);

    EditText edText;

    ImageButton restore;
    SwipeListView listNotes;

    SettingsManager Settings;
    private DAOManager Dao;

    final String message = "";
    int lastDeletedId = -1;



///////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Settings = new SettingsManager(getApplicationContext());
        Dao = new DAOManager(this);
        Dao.open();

        listNotes = (SwipeListView)findViewById(android.R.id.list);
        edText = (EditText) findViewById(R.id.editText);
        edText.setText(Settings.loadPrefs()); //all other settings are also loaded in Settings obj
        restore = (ImageButton) findViewById(R.id.restore_butt);
        restore.setVisibility(View.GONE); //because nothing to restore yet

        renderList();

        //getListView().setOnItemLongClickListener(this);

        //listNotes.setOnTouchListener(this);
    }

    @Override
    protected void onPause() {
        Dao.close();
        Settings.savePrefs(edText.getText().toString());
        //super.onDestroy();
        super.onPause();
    }

    protected void onResume(){
        Dao.open();
        super.onResume();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add("menu1");
        menu.add("menu2");
        menu.add("menu3");
        menu.add("menu4");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    void renderList(){

        List<DAOMem> Mems = Dao.getAllDAOMems();
        ArrayAdapter<DAOMem> adapter = new ArrayAdapter<DAOMem>(this, android.R.layout.simple_list_item_1, Mems);
        listNotes.setAdapter(adapter);
    }
/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        DAOMem m = (DAOMem) l.getAdapter().getItem(position);
        String text = m.getNote();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(text, text);
        clipboard.setPrimaryClip(clip);
        String copy = getString(R.string.copied) +" "+ text;
        Toast.makeText(getApplicationContext(),
                copy.substring(0,min(20, copy.length())), Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (Settings.getLongClickOption()){
            case OFF:
                return false;
                break;
            case DELETE:
                deleteItem(parent, position);
                break;
            case UPDATE:
                break;
            case COPYCLIPBOARD:
                break;
            case SEND:
                shareNote(parent, position);
        }
        return true;
    }
*/
    ///ACTION METHODS////////////////////////////////////////////////////////////////////////////////////////
    public void deleteItem(AdapterView<?> parent, int position){ // deleteItem(parent, position);
        DAOMem m = (DAOMem) parent.getAdapter().getItem(position);
        lastDeletedId = (int) m.getId();
        Dao.changeMemStatus(lastDeletedId,1);
        restore.setVisibility(View.VISIBLE);//now must be seen restore button
        Toast.makeText(getApplicationContext(),getString(R.string.deletedMessage), Toast.LENGTH_SHORT).show();
        renderList();
    }
    public void shareNote(AdapterView<?> parent, int position){
        intent.setType("text/plain");
        String textToSend = ((DAOMem)parent.getAdapter().getItem(position)).toSend();
        intent.putExtra(Intent.EXTRA_TEXT, textToSend);
        try
        {
            startActivity(Intent.createChooser(intent, getString(R.string.shareText)));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.ERROR), Toast.LENGTH_SHORT).show();
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void onEraseClick(View view) {
        edText.setText("");
    }

    public void onSubmitClick(View view) {
        String brunnenG = edText.getText().toString();
        if(!brunnenG.isEmpty()) {
            Dao.addNote(brunnenG);
            edText.setText("");
            renderList();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    getString(R.string.WTF), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onRestoreClick(View view) {
        Dao.changeMemStatus(lastDeletedId,0);
        restore.setVisibility(View.GONE);
        renderList();
    }
}