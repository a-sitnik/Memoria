package memoria.snid1.memoria.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import memoria.snid1.memoria.MainActivity;
import memoria.snid1.memoria.R;

/**
 * Created by snid1 on 01.09.2015.
 */
public class SettingsManager {
    // inner shit
    public SettingsManager (Context ctx){
        context = ctx;
    }
    static Context context;
    SharedPreferences sPref/* = context.getSharedPreferences("settings", Context.MODE_PRIVATE)*/;


    //params
    public Actions clickOption;
    public Actions longClickOption;
    public Actions swipeLeftOption;
    public Actions swipeRightOption;
    public Actions backButton1;
    public Actions backButton2;
    public Actions backButton3;

    private enum Actions {
        OFF(R.string.off),
        DELETE(R.string.deleteAct),
        UPDATE(R.string.updateAct),
        COPYCLIPBOARD(R.string.copyAct),
        SEND(R.string.sendAct);

        private int descr;

        private Actions(int description) {
            descr = description;
        }
        public String getDescr(){
            return context.getString(this.descr);
        }
    }
    //savePrefs(edText.getText().toString());
    public void savePrefs(String message) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("clickOption", clickOption.name());
        ed.putString("longClickOption", longClickOption.name());
        ed.putString("swipeRightOption", swipeRightOption.name());
        ed.putString("swipeLeftOption", swipeLeftOption.name());

        ed.putString("message", message);
        ed.commit();
    }
    // edText = settings.loadPrefs();
    public String loadPrefs() {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        // -----------------Here to change defaults---------------------------/
        //                                                                   V
        clickOption = Actions.valueOf(sPref.getString("clickOption", "COPYCLIPBOARD"));
        longClickOption = Actions.valueOf(sPref.getString("longClickOption", "UPDATE"));
        swipeRightOption = Actions.valueOf(sPref.getString("swipeRightOption", "OFF"));
        swipeLeftOption = Actions.valueOf(sPref.getString("swipeLeftOption", "DELETE"));

        return sPref.getString("message", "");
    }

    public Actions getClickOption() {
        return clickOption;
    }

    public void setClickOption(Actions clickOption) {
        this.clickOption = clickOption;
    }

    public Actions getLongClickOption() {
        return longClickOption;
    }

    public void setLongClickOption(Actions longClickOption) {
        this.longClickOption = longClickOption;
    }

    public Actions getSwipeLeftOption() {
        return swipeLeftOption;
    }

    public void setSwipeLeftOption(Actions swipeLeftOption) {
        this.swipeLeftOption = swipeLeftOption;
    }

    public Actions getSwipeRightOption() {
        return swipeRightOption;
    }

    public void setSwipeRightOption(Actions swipeRightOption) {
        this.swipeRightOption = swipeRightOption;
    }
}
