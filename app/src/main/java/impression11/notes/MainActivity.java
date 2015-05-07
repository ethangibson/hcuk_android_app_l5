package impression11.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);
        String count = db.getNotesCount()+"";

        Log.w("Count", count);
        final ListView list = (ListView) findViewById(R.id.list1);

        final ArrayAdapter<Notes> mArrayAdapter = new ArrayAdapter<Notes>(this, android.R.layout.simple_list_item_1, android.R.id.text1, db.getAllNotes());
        list.setAdapter(mArrayAdapter);

        // Because it's a list item and we cannot set a "onClick" we have to use a click listerner

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // get the note from the position in the list clicked
                Notes note = (Notes) (list.getItemAtPosition(position));
                // get the database id from that note
                int nid = note.getId();
                // create an intenet to open an activity
                Intent sendNote = new Intent(MainActivity.this, add_note.class);
                // send the id across to the new intent for retrival on the other side
                sendNote.putExtra("id", nid);
                startActivityForResult(sendNote, 0);

            }
        });



        // On Long Click listener - prompt delete dialog for the note
        list.setLongClickable(true);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id) {
                final Notes note = (Notes) (list.getItemAtPosition(position));
                final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                // Create dialog builder - use the object to build
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Delete Note?");
                builder1.setCancelable(true);

                // set the negative repsonse and what it does
                builder1.setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // delete note from database
                                db.deleteNotes(note);
                                // remove from the list on main activity
                                mArrayAdapter.remove(mArrayAdapter.getItem(position));
                                // force it to update
                                mArrayAdapter.notifyDataSetChanged();
                                Log.w("clicked", note.getId()+"");
                            }
                        });
                builder1.setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                // convert alert builder into a dialog
                AlertDialog alert11 = builder1.create();
                // show dialog
                alert11.show();

                return true;
            }
        });


    }

    public void AddNote(MenuItem item){

        Intent intent = new Intent(this, add_note.class);

        startActivityForResult(intent, 0);


    }

    @Override
    // When the note has been saved or updated we listen for the result code from it being sucessful
    // if it's sucessful we need to re-get all of the notes from the database and rebuild our list
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                DatabaseHandler db = new DatabaseHandler(this);
                String count = db.getNotesCount()+"";

                Log.w("Count", count);
                final ListView list = (ListView) findViewById(R.id.list1);

                final ArrayAdapter<Notes> mArrayAdapter = new ArrayAdapter<Notes>(this, android.R.layout.simple_list_item_1, android.R.id.text1, db.getAllNotes());
                list.setAdapter(mArrayAdapter);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
