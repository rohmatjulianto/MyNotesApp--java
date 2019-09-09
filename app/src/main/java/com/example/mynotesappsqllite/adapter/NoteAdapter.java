package com.example.mynotesappsqllite.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mynotesappsqllite.CustomkOnClickListener;
import com.example.mynotesappsqllite.NoteUpdateActivity;
import com.example.mynotesappsqllite.R;
import com.example.mynotesappsqllite.entity.note;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.CONTENT_URI;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteVIewHolder> {

    private ArrayList<note> listNotes = new ArrayList<>();
    private Activity activity;

    public void setListNotes(ArrayList<note> listNotes) {
//        this.listNotes = listNotes;
//
//        if (listNotes.size() > 0){
//            this.listNotes.clear();
//        }
//
//        this.listNotes.addAll(listNotes);
//        notifyDataSetChanged();
        this.listNotes.clear();
        this.listNotes.addAll(listNotes);
        notifyDataSetChanged();
    }

    public ArrayList<note> getListNotes() {
        return listNotes;
    }

    public Activity getActivity() {
        return activity;
    }

    public NoteAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteVIewHolder holder, int position) {
        holder.tvTitle.setText(getListNotes().get(position).getTitle());
        holder.tvDate.setText(getListNotes().get(position).getDate());
        holder.tvDesc.setText(getListNotes().get(position).getDesc());
        holder.cvNote.setOnClickListener(new CustomkOnClickListener(position, new CustomkOnClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, NoteUpdateActivity.class);

                Uri uri = Uri.parse(CONTENT_URI + "/" + getListNotes().get(position).getId());
                intent.setData(uri);

                intent.putExtra(NoteUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(NoteUpdateActivity.EXTRA_NOTE, listNotes.get(position));
                activity.startActivityForResult(intent, NoteUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class NoteVIewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvDesc, tvDate;
        final CardView cvNote;

        public NoteVIewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            cvNote = itemView.findViewById(R.id.cv_item);
        }
    }


    public void addItem(note Note){
        this.listNotes.add(Note);
        notifyItemInserted(listNotes.size() -1);
    }

    public void updateItem(int position , note Note){
        this.listNotes.set(position, Note);
        notifyItemChanged(position, Note);
    }

    public void removeItem(int position){
        this.listNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listNotes.size());
    }
}
