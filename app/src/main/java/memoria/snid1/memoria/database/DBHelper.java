package memoria.snid1.memoria.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// by default saved in the directory DATA/data/APP_NAME/databases/FILENAME
public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_IF_DONE= "ifDone";
    public static final String COLUMN_IF_BACKED= "ifBackuped";
    private static final String DATABASE_NAME = "memoria.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table "+TABLE_COMMENTS+
            "("+COLUMN_ID+" integer primary key autoincrement, " +
            ""+COLUMN_NOTE+" text, " +
            ""+COLUMN_TIME+" DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            ""+COLUMN_IF_DONE+" INTEGER DEFAULT '0', " +
            ""+COLUMN_IF_BACKED+" INTEGER DEFAULT '0' ); ";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    // insert into mems (note) values ("Hello, snid1")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }
}
