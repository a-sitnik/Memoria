package memoria.snid1.memoria;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;


public class MainActivity extends ListActivity implements AdapterView.OnItemLongClickListener, OnTouchListener{

    final Intent intent = new Intent(Intent.ACTION_SEND);

    EditText edText;
    ImageButton restore;
    SharedPreferences sPref;

    ListView listNotes;
    //settings toggles shit
    ToggleButton ifDate;
    ToggleButton longClick;
    boolean showDate;
    boolean optionLongClick;

    final String message = "";
    private DAOManager Dao;
    int lastDeletedId = -1;



///////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listNotes = (ListView)findViewById(android.R.id.list);
        listNotes.setOnTouchListener(this);

        edText = (EditText) findViewById(R.id.editText);
        loadPrefs();

        restore = (ImageButton) findViewById(R.id.restore_butt);
        restore.setVisibility(View.GONE); //because nothing to restore yet

        // initing settings toggles
        ifDate = (ToggleButton) findViewById(R.id.toggleDate);
        ifDate.setChecked(showDate);
        longClick = (ToggleButton) findViewById(R.id.LongPress);
        longClick.setChecked(optionLongClick);


        Dao = new DAOManager(this);
        Dao.open();

        renderList();

        getListView().setOnItemLongClickListener(this);
    }

    @Override
    protected void onPause() {
        Dao.close();
        savePrefs();
        super.onDestroy();
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

        if(showDate == false){ //case if we need only note
            ArrayAdapter<DAOMem> adapter = new ArrayAdapter<DAOMem>(this, android.R.layout.simple_list_item_1, Mems);
            setListAdapter(adapter);

        }else {//case if we need note and time
            MemAdapter adapter = new MemAdapter(this, R.layout.item, Mems);
            setListAdapter(adapter);
        }
    }

    void savePrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean("showDate", showDate);
        ed.putBoolean("optionLongClick", optionLongClick);
        ed.putString(message, edText.getText().toString());
        ed.commit();
    }


    void loadPrefs() {
        sPref = getPreferences(MODE_PRIVATE);
        optionLongClick = sPref.getBoolean("optionLongClick",false);
        showDate = sPref.getBoolean("showDate", true);
        edText.setText(sPref.getString(message, ""));
    }
    ////  CLICK LISTENERS////////////////////////////////////////////////////////////////////////////////////////

    // SWI(Y)PE
    long startTime, endTime;
    float x;
    float y;
    Float Xstart, Ystart, Xfinish, Yfinish;

    String sDown;
    String sMove;
    String sUp;
    //Float Xstart, Ystart, Xfinish, Yfinish;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                startTime = System.currentTimeMillis();
                Xstart = x;
                Ystart = y;
                sDown = "Down: " + x + ",  " + y;
                sMove = ""; sUp = "";
                //
                break;
            case MotionEvent.ACTION_MOVE: // движение
                //
                sMove = "Move: " + x + ",  " + y;
                //if (Ystart-50 < y || Ystart+50 > y && Xstart-100 > x ||Xstart+100 < x)
                //    return true;
                if (startTime + 250 > System.currentTimeMillis()){
                    if (abs(Xstart-x)<30)
                        return false;
                }

                if (abs(Xstart-x)<abs(Ystart-y)*2)
                    if (Ystart > y+ 15.0 || Ystart < y- 15.0) {
                        sMove = "yoba: " + x + ",  " + y;
                        return false;
                    }
                break;


            case MotionEvent.ACTION_UP: // отпускание
                //
                sMove = "";
                sUp = "Up: " + x + ",  " + y;
                //
                endTime = System.currentTimeMillis();
                Xfinish = x;
                Yfinish = y;

                //swype R to L
                if (Xstart - x > 70){
                    /*
                    v.getHandler().
                    v.getparent
                    */

                }
                // L to R
                if ((x - Xstart > 70)){

                }

                break;
            case MotionEvent.ACTION_CANCEL:
                return true;
        }
        //
        edText.setText(sDown + "\n" + sMove + "\n" + sUp);
        //

        return true;
    }

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
        if (optionLongClick) {
            deleteItem(parent, position);
        }else{
            shareNote(parent, position);
        }
        return true;
    }

    ///SUB LONGCLICK METHODS////////////////////////////////////////////////////////////////////////////////////////
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
    // on/off time
    public void onDateToggle(View view) {
        boolean checked = ((ToggleButton) view).isChecked();
        if(checked){
            showDate = true;
        }else{
            showDate = false;
        }
        renderList();
    }
    // switches long press func
    public void onLongPress(View view) {
        boolean checked = ((ToggleButton) view).isChecked();
        if(checked){
            optionLongClick = true;
        }else{
            optionLongClick = false;
        }
        renderList();
    }
    //onrestore button
    public void onRestoreClick(View view) {
        Dao.changeMemStatus(lastDeletedId,0);
        restore.setVisibility(View.GONE);
        renderList();
    }
}