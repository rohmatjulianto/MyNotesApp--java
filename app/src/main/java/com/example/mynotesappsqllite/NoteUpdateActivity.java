package com.example.mynotesappsqllite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotesappsqllite.entity.note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.DATE;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.DESC;
import static com.example.mynotesappsqllite.db.DatabaseContract.NoteColumns.TITLE;

public class NoteUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTitle,edtDesc;
    private Button btnSave;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private note Note;
    private int position;

//    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);

        edtTitle = findViewById(R.id.edt_title);
        edtDesc = findViewById(R.id.edt_desc);

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

//        noteHelper = NoteHelper.getInstance(getApplicationContext());
        Note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (Note != null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }else{
            Note = new note();
        }

        Uri uri = getIntent().getData();
        if (uri != null){
            Cursor cursor = getContentResolver().query(uri, null, null,null,null);

            if (cursor != null){
                if (cursor.moveToNext()) Note = new note(cursor);
                cursor.close();
            }
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit){
            actionBarTitle = "Edit Notes";
            btnTitle = "Update";
            if (Note != null){
                edtTitle.setText(Note.getTitle());
                edtDesc.setText(Note.getDesc());
            }
        }else{
            actionBarTitle = "Add New Notes";
            btnTitle = "Save";
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnSave.setText(btnTitle);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save){
            String title = edtTitle.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();

            boolean isEmpty = false;

            if (TextUtils.isEmpty(title)){
                edtTitle.setError("Field can not be blank");
                isEmpty = true;
//                return;
            }

            Note.setTitle(title);
            Note.setDesc(desc);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, Note);
            intent.putExtra(EXTRA_POSITION, position);

            if(!isEmpty){
                ContentValues values = new ContentValues();
                values.put(TITLE, title);
                values.put(DESC, desc);

                if (isEdit){
                    getContentResolver().update(getIntent().getData(), values, null,null);
                    Toast.makeText(NoteUpdateActivity.this, "Sucess Update", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    values.put(DATE, getCurrentDate());
                    Note.setDate(getCurrentDate());
                    Toast.makeText(NoteUpdateActivity.this, "Added item succsessfuly", Toast.LENGTH_SHORT).show();
                    getContentResolver().insert(CONTENT_URI, values);
                    finish();
                }
            }
        }
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit){
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;
    private void showAlertDialog(int type){
        final boolean isDialogclose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogclose){
            dialogTitle = "Close";
            dialogMessage = "Close Add Notes?";

        }else{
            dialogTitle =" Delete";
            dialogMessage ="Delete this notes?";
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(dialogTitle);
        alert.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isDialogclose){
                            finish();
                        } else{
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            Toast.makeText(NoteUpdateActivity.this, "Delete Successfuly", Toast.LENGTH_SHORT).show();
                            getContentResolver().delete(getIntent().getData(), null,null);
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }
}
