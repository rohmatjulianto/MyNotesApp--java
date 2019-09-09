package com.example.mynotesappsqllite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mynotesappsqllite.adapter.NoteAdapter;
import com.example.mynotesappsqllite.entity.note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.example.mynotesappsqllite.helper.MappingHelper.mapCursorToArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoadNotesCallback {

    RecyclerView rvNotes;
    ProgressBar pBar;
    FloatingActionButton fabAdd;

    final String EXTRA_STATE = "extra_state";
    NoteAdapter adapter;
//    NoteHelper noteHelper;

   static HandlerThread handlerThread;
    DataObserver dataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("My Notes App");
        }

        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

//        noteHelper = NoteHelper.getInstance(getApplicationContext());
//        noteHelper.open();

        pBar = findViewById(R.id.proBar);
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        adapter = new NoteAdapter(this);
        rvNotes.setAdapter(adapter);

        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());
        dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);

        if (savedInstanceState == null){
//            new LoadNotesAsync(noteHelper, this).execute();
            new LoadNotesAsync(this, this).execute();
        }else{
            ArrayList<note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if(list != null){
                adapter.setListNotes(list);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListNotes());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, NoteUpdateActivity.class);
            startActivityForResult(intent, NoteUpdateActivity.REQUEST_ADD);
        }
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    @Override
    public void postExecute(Cursor notes) {
        pBar.setVisibility(View.GONE);

        ArrayList<note> listNotes = mapCursorToArrayList(notes);
        if (listNotes.size() > 0){
            adapter.setListNotes(listNotes);
        } else {
            adapter.setListNotes(new ArrayList<note>());
            showSackbar("Tidak Ada data saat ini");
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, Cursor>{
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> WeakCallback;

        LoadNotesAsync(Context context, LoadNotesCallback callback) {
//            WeakHelper = new WeakReference<>(noteHelper);
            weakContext = new WeakReference<>(context);
            WeakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WeakCallback.get().preExecute();
        }

//        @Override
//        protected ArrayList<note> doInBackground(Void... voids) {
//            return WeakHelper.get().getAllNotes();
//        }


        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null,null,null,null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            WeakCallback.get().postExecute(notes);
        }
    }

    public void showSackbar(String message){
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNotesAsync(context, (LoadNotesCallback) context).execute();
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nul Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data != null){
//            if (requestCode == NoteUpdateActivity.REQUEST_ADD){
//                if (resultCode == NoteUpdateActivity.RESULT_ADD){
//                    note Note = data.getParcelableExtra(NoteUpdateActivity.EXTRA_NOTE);
//                    adapter.addItem(Note);
//                    rvNotes.smoothScrollToPosition(adapter.getItemCount() -1);
//                    showSackbar("Satu item berhasil ditambah");
//                }
//            }else if (requestCode == NoteUpdateActivity.REQUEST_UPDATE){
//                if (resultCode == NoteUpdateActivity.RESULT_UPDATE){
//                    note Note = data.getParcelableExtra(NoteUpdateActivity.EXTRA_NOTE);
//                    int position = data.getIntExtra(NoteUpdateActivity.EXTRA_POSITION, 0);
//                    adapter.updateItem(position, Note);
//                    rvNotes.smoothScrollToPosition(position);
//                    showSackbar("satu item berubnah");
//                } else if(resultCode == NoteUpdateActivity.RESULT_DELETE){
//                    int position = data.getIntExtra(NoteUpdateActivity.EXTRA_POSITION, 0);
//                    adapter.removeItem(position);
//                    showSackbar("satu item hapus cuy");
//                }
//            }
//        }
//    }




}
