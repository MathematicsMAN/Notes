package com.example.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ContentOfNotesActivity extends AppCompatActivity {

    Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_of_notes);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        if (savedInstanceState == null) {
            ContentOfNotesFragment details = new ContentOfNotesFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Intent intent = new Intent();
        TextView title = findViewById(R.id.title_of_notes);
        EditText date = findViewById(R.id.date_of_created_notes);
        EditText description = findViewById(R.id.description_of_notes);
        notes = new Notes(title.getText().toString(),
                description.getText().toString(),
                date.getText().toString(),
                false);
        intent.putExtra(ContentOfNotesFragment.KEY_NOTES, notes);
        setResult(RESULT_OK, intent);
        super.onSaveInstanceState(outState);
    }
}