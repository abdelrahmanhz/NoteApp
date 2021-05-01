package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE_RE_CODE = 1;
    private static final int EDIT_NOTE_RE_CODE = 2;
    public static final String NOTE_KEY = "note_key";
    private RecyclerView rv;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    NoteRVAdapter adapter;
    DatabaseAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.main_rv);
        fab = findViewById(R.id.main_fab);
        toolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);

        db = DatabaseAccess.getInstance(this);

        db.open();
        ArrayList<Note> notes = db.getAllNotes();
        db.close();
        adapter = new NoteRVAdapter(notes, new OnRVItemClickListener() {
            @Override
            public void onItemClick(int noteId) {
                Intent i = new Intent(getBaseContext(), ViewNoteActivity.class);
                i.putExtra(NOTE_KEY, noteId);
                startActivityForResult(i, EDIT_NOTE_RE_CODE);
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), ViewNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_RE_CODE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.main_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                db.open();
                ArrayList<Note> notes = db.searchNotes(query);
                adapter.setNotes(notes);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Search is done.", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                db.open();
                ArrayList<Note> notes = db.searchNotes(newText);
                adapter.setNotes(notes);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                db.open();
                ArrayList<Note> notes = db.getAllNotes();
                adapter.setNotes(notes);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_NOTE_RE_CODE || requestCode == EDIT_NOTE_RE_CODE){

            db.open();
            ArrayList<Note> notes = db.getAllNotes();
            db.close();
            adapter.setNotes(notes);
            adapter.notifyDataSetChanged();
        }
    }
}