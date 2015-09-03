package memoria.snid1.memoria.database;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DAOManager {
    // Database fields

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    SimpleDateFormat df;

    private String[] allColumns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_NOTE,
            DBHelper.COLUMN_TIME,
        };

    public DAOManager(Context context) {
        dbHelper = new DBHelper(context);
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH/*context.getResources().getConfiguration().locale*/);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public DAOMem addNote(String comment) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NOTE, comment);
        long insertId = database.insert(DBHelper.TABLE_COMMENTS, null,
                values);

        Cursor cursor = database.query(DBHelper.TABLE_COMMENTS,
                allColumns, DBHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        DAOMem newDAOMem = cursorToDAOMem(cursor);
        cursor.close();
        return newDAOMem;
    }
    // changing ifDone to any value, 0 = seen, 1 = unseen, maybe tags later
    public void changeMemStatus(int id, int status){
        database.execSQL("UPDATE " + DBHelper.TABLE_COMMENTS + " SET " + DBHelper.COLUMN_IF_DONE + " = \"" + status + "\" where _id = \"" + id + "\"");
    }

    public void editMemText(int id, String text){
        database.execSQL("UPDATE "+ DBHelper.TABLE_COMMENTS +" SET "+ DBHelper.COLUMN_NOTE +" = \""+ text +"\" where _id = \""+ id +"\"");
    }

    // instant delete for the future
    public void deleteDAOMem(DAOMem comment) {
        long id = comment.getId();
        database.delete(DBHelper.TABLE_COMMENTS, DBHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<DAOMem> getAllDAOMems() {
        List<DAOMem> comments = new ArrayList<DAOMem>();

        Cursor cursor = database.query(DBHelper.TABLE_COMMENTS,
                allColumns, DBHelper.COLUMN_IF_DONE+ " = 0", null, null, null, DBHelper.COLUMN_ID+ " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DAOMem comment = cursorToDAOMem(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }
// factory sql -> dao
    private DAOMem cursorToDAOMem(Cursor cursor) {
        DAOMem comment = new DAOMem();
        comment.setId(cursor.getLong(0));
        comment.setNote(cursor.getString(1));
        try {
            comment.setDate((Date) df.parse(cursor.getString(2)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return comment;
    }
}
