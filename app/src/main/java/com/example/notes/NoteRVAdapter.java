package com.example.notes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteRVAdapter extends RecyclerView.Adapter<NoteRVAdapter.NoteViewHolder> {

    ArrayList<Note> notes;
    private OnRVItemClickListener listener;


    public NoteRVAdapter(ArrayList<Note> notes, OnRVItemClickListener listener){

        this.notes = notes;
        this.listener = listener;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_note_layout,
                null, false);
        NoteViewHolder viewHolder = new NoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = notes.get(position);
        if(note.getTitle() != null && !note.getTitle().isEmpty()) {
            holder.card_title.setText(note.getTitle());
            holder.card_content.setText(note.getContent());
            holder.card_title.setTag(note.getId());
        }
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView card_title, card_content;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            card_title = itemView.findViewById(R.id.card_title);
            card_content = itemView.findViewById(R.id.card_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int id = (int) card_title.getTag();
                    listener.onItemClick(id);
                }
            });
        }
    }
}
