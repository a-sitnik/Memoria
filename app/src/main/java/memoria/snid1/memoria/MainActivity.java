package memoria.snid1.memoria;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.*;

import java.util.ArrayList;

import memoria.snid1.memoria.database.DAOManager;
import memoria.snid1.memoria.database.DAOMem;
import memoria.snid1.memoria.listView.MemAdapter;
import memoria.snid1.memoria.utils.SettingsManager;


import static java.lang.Math.min;


public class MainActivity extends FragmentActivity{

    final Intent intent = new Intent(Intent.ACTION_SEND);

    EditText edText;
    ImageButton restore;
    SwipeListView listNotes;
    ArrayList<DAOMem> Mems;
    SettingsManager Settings;
    private DAOManager Dao;
    MemAdapter adapter;
    final String message = "";

    int lastDeletedId = -1;
    int lastDeletedPosition = -1;
    DAOMem lastDel;

    ///////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Settings = SettingsManager.getINSTANCE(getApplicationContext());
        Dao = new DAOManager(this);
        Dao.open();

        listNotes = (SwipeListView) findViewById(R.id.list_s);//android.R.id.list_s
        edText = (EditText) findViewById(R.id.editText);
        edText.setText(Settings.loadPrefs()); //all other settings are also loaded in Settings obj
        restore = (ImageButton) findViewById(R.id.restore_butt);
        restore.setVisibility(View.GONE); //because nothing to restore yet
        listNotes.setChoiceMode(ListView.CHOICE_MODE_NONE);

        Mems = Dao.getAllDAOMems();
        adapter = new MemAdapter(this, R.layout.mem_in_list_item, Mems); // R.layout.mem_in_list_item
        listNotes.setAdapter(adapter);


        listNotes.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                if (SettingsManager.INSTANCE.swipeDirectionToRight == toRight){
                    executeListAction(SettingsManager.INSTANCE.getSwipeOption(), position);
                    listNotes.dismiss(position);
                    fullRefresh();

                    // adapter.notifyDataSetChanged();
                    //TODO:
                    // http://stackoverflow.com/questions/34147816/android-custom-listview-adapter-doesnt-want-to-fit-deleted-items
                }

            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }
            @Override
            public void onListChanged() {
            }
            @Override
            public void onMove(int position, float x) {
            }
            @Override
            public void onStartOpen(int position, int action, boolean right) {
            }
            @Override
            public void onStartClose(int position, boolean right) {
            }
            /** Place to set tap action **/
            /////////////////
            @Override
            public void onClickFrontView(int position) {
                    executeListAction(SettingsManager.INSTANCE.clickOption, position);
                Log.d("click", String.format("onClickFrontView %d", position));
            }
            @Override
            public void onClickBackView(int position) {
                Log.d("back click", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    // data.remove(position);
                }
            }

        });
    }


    @Override
    protected void onPause() {
        Dao.close();
        Settings.savePrefs(edText.getText().toString());
        super.onPause();
    }

    protected void onResume() {
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
    public boolean onOptionsItemSelected(MenuItem mem_in_list_item) {
        // Handle action bar mem_in_list_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = mem_in_list_item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(mem_in_list_item);
    }
*/
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    void refreshList() {
        Mems.clear();
        Mems.addAll(Dao.getAllDAOMems());
        adapter.notifyDataSetChanged();
        //adapter = new MemAdapter(this, R.layout.mem_in_list_item, Mems); // R.layout.mem_in_list_item
        //listNotes.setAdapter(adapter);
    }
    //TODO: refactor every usage of this method
    void fullRefresh(){
        Mems = Dao.getAllDAOMems();
        adapter = new MemAdapter(this, R.layout.mem_in_list_item, Mems);
        listNotes.setAdapter(adapter);
    }

    ///Swipe ACTION SELECTOR & METHODS////////////////////////////////////////////////////////////////////////////////////////
    public void executeListAction(SettingsManager.Actions act, int position) {
        switch (act) {
            case OFF:
                //do nothing
                break;
            case DELETE:
                deleteItem(position);
                break;
            case UPDATE:
                break;
            case COPYCLIPBOARD:
                copyToClipboard(position);
                break;
            case SEND:
                shareNote(position);
                break;
        }
    }

    public void copyToClipboard(int position){
        DAOMem m = adapter.getMem(position);
        String text = m.getNote();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(text, text);
        clipboard.setPrimaryClip(clip);
        String copy = getString(R.string.copied) +" "+ text;
        Toast.makeText(getApplicationContext(),
                copy.substring(0,min(20, copy.length())), Toast.LENGTH_SHORT).show();
    }
    public void deleteItem(int position) { // deleteItem(position);
        DAOMem m = adapter.getMem(position);
        lastDeletedPosition = position;
        lastDeletedId = (int) m.getId();
        Dao.changeMemStatus(lastDeletedId, 1);
        restore.setVisibility(View.VISIBLE);//now must be seen restore button
        Toast.makeText(getApplicationContext(), getString(R.string.deletedMessage), Toast.LENGTH_SHORT).show();
        lastDel = Mems.get(position);
        adapter.remove(Mems.get(position));
        //adapter.notifyDataSetChanged();
        //renderList();
    }

    public void shareNote(int position) {
        intent.setType("text/plain");
        String textToSend = (adapter.getMem(position)).toSend();
        intent.putExtra(Intent.EXTRA_TEXT, textToSend);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.shareText)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.ERROR), Toast.LENGTH_SHORT).show();
        }
    }

     //////////////
    // Interface common buttons actions
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void onEraseClick(View view) {
        edText.setText("");
    }

    public void onSubmitClick(View view) {
        String brunnenG = edText.getText().toString();
        if (!brunnenG.isEmpty()) {
            //adapter.add(Dao.addNote(brunnenG)); TODO: make this working
            Dao.addNote(brunnenG);
            edText.setText("");
            refreshList();
            //adapter.notifyDataSetChanged();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    getString(R.string.WTF), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onRestoreClick(View view) {
        Dao.changeMemStatus(lastDeletedId, 0);
        adapter.insert(lastDel, lastDeletedPosition);
        restore.setVisibility(View.GONE);
        //adapter.notifyDataSetChanged();
        //renderList();
    }
}