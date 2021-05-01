package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ViewNoteActivity extends AppCompatActivity {

    public static final int ADD_NOTE_CODE = 1;
    public static final int EDIT_NOTE_CODE = 2;
    private Toolbar toolbar;
    private TextInputEditText title_et, content_et;
    private int noteId = -1;

    private DatabaseAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);

        db = DatabaseAccess.getInstance(this);

        Intent intent = getIntent();
        noteId = intent.getIntExtra(MainActivity.NOTE_KEY, -1);
        if(noteId == -1){

            disableOrEnabledFields(true);
            clearFields();
        }
        else{

            disableOrEnabledFields(false);
            db.open();
            Note note = db.getNote(noteId);
            db.close();
            if(note != null){

                fillNoteFields(note);
            }
        }
    }

    private void fillNoteFields(Note note){
        title_et.setText(note.getTitle());
        content_et.setText(note.getContent());
    }

    private void disableOrEnabledFields(boolean value){

        title_et.setEnabled(value);
        content_et.setEnabled(value);
    }

    private void clearFields(){

        title_et.setText("");
        content_et.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.details_menu, menu);
        MenuItem saveItem = menu.findItem(R.id.details_menu_save);
        MenuItem editItem = menu.findItem(R.id.details_menu_edit);
        MenuItem deleteItem = menu.findItem(R.id.details_menu_delete);
        if(noteId == -1){
            saveItem.setVisible(true);
            editItem.setVisible(false);
            deleteItem.setVisible(false);
        }
        else{
            saveItem.setVisible(false);
            editItem.setVisible(true);
            deleteItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String title, content;
        db.open();
        switch(item.getItemId()){
            case R.id.details_menu_save:

                title = title_et.getText().toString();
                content = content_et.getText().toString();
                Note note = new Note(noteId, title, content);
                boolean result;
                if(noteId == -1){
                    result = db.insertNote(note);
                    if(result) {

                        Toast.makeText(this, "Note is added successfully", Toast.LENGTH_SHORT).show();
                        setResult(ADD_NOTE_CODE, null);
                        finish();
                    }
                }
                else{
                    result = db.updateNote(note);
                    if(result) {

                        Toast.makeText(this, "Note is updated successfully", Toast.LENGTH_SHORT).show();
                        setResult(EDIT_NOTE_CODE, null);
                        finish();
                    }
                }

                return true;
            case R.id.details_menu_edit:

                disableOrEnabledFields(true);
                MenuItem saveItem = toolbar.getMenu().findItem(R.id.details_menu_save);
                MenuItem editItem = toolbar.getMenu().findItem(R.id.details_menu_edit);
                MenuItem deleteItem = toolbar.getMenu().findItem(R.id.details_menu_delete);
                saveItem.setVisible(true);
                editItem.setVisible(false);
                deleteItem.setVisible(false);
                return true;
            case R.id.details_menu_delete:
                note = new Note(noteId, null, null);
                result = db.deleteNote(note);
                    if(result) {

                        Toast.makeText(this, "Note is deleted successfully", Toast.LENGTH_SHORT).show();
                        setResult(EDIT_NOTE_CODE, null);
                        finish();
                    }
                return true;
        }
        db.close();
        return true;
    }
}