package impression11.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NotesManager";

    // Notes table name
    private static final String TABLE_NOTES = "Notes";

    // Notes Table Columns names
    private static final String KEY_ID = "id";
    private static final String TITLE = "title";
    private static final String NOTES = "notes";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TITLE + " TEXT,"
                + NOTES + " TEXT)";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding New Note
    void addNote(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle()); // Notes Name
        values.put(NOTES, note.getNote()); // Notes Name
        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
    }

    Notes getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[] { KEY_ID,
                        TITLE, NOTES}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null , null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Notes note = new Notes(cursor.getString(1), cursor.getString(2), cursor.getInt(0));
//       return note
        return note;
    }

    // Getting All Notes
    public List<Notes> getAllNotes() {
        List<Notes> noteList = new ArrayList<Notes>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));

                // Adding Note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // Return Note list
        return noteList;
    }

    // Updating single Note
    public int updateNote(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, note.getTitle());
        values.put(NOTES, note.getNote());

        return db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                new String[] { note.getId()+"" });
    }

    // Deleting Single Note
    public void deleteNotes(Notes note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + " = ?",
                new String[] { note.getId()+"" });
        db.close();
    }


    // Getting Notes Count
    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }


}
