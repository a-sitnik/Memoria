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
    // singleton`s shit
    public static SettingsManager INSTANCE;
    public static SettingsManager getINSTANCE(Context ctx){
        INSTANCE = new SettingsManager(ctx);
        return INSTANCE;
    }
    public static SettingsManager getINSTANCE(){
        return INSTANCE;
    }

    // inner shit
    private SettingsManager (Context ctx){
        context = ctx;
    }
    static Context context;
    SharedPreferences sPref/* = context.getSharedPreferences("settings", Context.MODE_PRIVATE)*/;


    public boolean swipeDirectionToRight;
    //params
    public Actions clickOption;
    public Actions swipeOption;
    public Actions backButton1;
    public Actions backButton2;
    public Actions backButton3;

    public enum Actions {
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
        ed.putString("swipeOption", swipeOption.name());
        ed.putBoolean("swipeDirection", swipeDirectionToRight);

        ed.putString("message", message);
        ed.commit();
    }
    // edText = settings.loadPrefs();
    public String loadPrefs() {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        // -----------------Here to change defaults---------------------------/
        //                                                                   V
        clickOption = Actions.valueOf(sPref.getString("clickOption", "COPYCLIPBOARD"));

        swipeOption = Actions.valueOf(sPref.getString("swipeOption", "DELETE"));
        swipeDirectionToRight = sPref.getBoolean("swipeDirection", true);

        return sPref.getString("message", "");
    }

    public Actions getClickOption() {
        return clickOption;
    }

    public void setClickOption(Actions clickOption) {
        this.clickOption = clickOption;
    }

    public Actions getSwipeOption() {
        return swipeOption;
    }

    public void setSwipeOption(Actions swipeLeftOption) {
        this.swipeOption = swipeLeftOption;
    }

}
