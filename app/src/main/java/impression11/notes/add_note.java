package impression11.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class add_note extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        // get the ID if this note already exists and needs to be updated
        final int NoteId = getIntent().getIntExtra("id", 0);
        // if the note exists it will have an ID greater than 0
        if (NoteId > 0 ){
            DatabaseHandler db = new DatabaseHandler(this);
            Notes dbnote = db.getNote(NoteId);

            // if note exists set the title and note content in the form
            EditText title = (EditText) findViewById(R.id.Title);
            EditText note = (EditText) findViewById(R.id.Note);

            title.setText(dbnote.getTitle());
            note.setText(dbnote.getNote());

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    public void SaveNote(View view){

        EditText title = (EditText) findViewById(R.id.Title);
        EditText note = (EditText) findViewById(R.id.Note);

        final int NoteId = getIntent().getIntExtra("id", 0);


        Notes sendNote = new Notes(title.getText().toString(),note.getText().toString(), NoteId);

        //Initialise the database functions

        DatabaseHandler db = new DatabaseHandler(this);

        // If note is already exists

        if (NoteId > 0 ){
            //update current note
            db.updateNote(sendNote);
            Log.w("DB", "Updated"+" "+NoteId);
        }
        else {

            // If doesn't exist add note to the database
            db.addNote(sendNote);


        }

        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();

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
}
