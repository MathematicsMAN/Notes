package com.example.notes;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ContentOfNotesFragment extends Fragment {

    public static final String KEY_NOTES = "NOTES";
    public static final String KEY_POSITION = "POSITION";
    private final String LOG = "ContentOfNotesFragment";

    private Notes notes;
    private DatePicker datePicker;

    private AppCompatTextView titleOfNotes;
    private AppCompatEditText descriptionOfNotes;
    private AppCompatEditText dateOfNotes;
    private Button saveButton;
    private Button cancelButton;

    public static ContentOfNotesFragment newInstance(Notes notes) {
        ContentOfNotesFragment fragment = new ContentOfNotesFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_NOTES, notes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notes = (Notes) getArguments().getSerializable(KEY_NOTES);
        }
        Log.d(LOG, "onCreate()");
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_of_notes, container, false);

        titleOfNotes = view.findViewById(R.id.title_of_notes);
        descriptionOfNotes = view.findViewById(R.id.description_of_notes);
        dateOfNotes = view.findViewById(R.id.date_of_created_notes);
        createContext();

        saveButton = view.findViewById(R.id.save_notes);
        saveButton.setOnClickListener(v ->
            saveInstance()
        );

        cancelButton = view.findViewById(R.id.cancel_notes);
        cancelButton.setOnClickListener(v -> {
            if (getArguments() != null) {
                notes = (Notes) getArguments().getSerializable(KEY_NOTES);
                createContext();
            }
        });

        datePicker = view.findViewById(R.id.date_picker);
        datePicker.setVisibility(DatePicker.GONE);
        Calendar today = Calendar.getInstance();

        datePicker.init(today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                (view1, year, monthOfYear, dayOfMonth) ->
                        dateOfNotes.setText(String.format("%d.%d.%d",
                                year,
                                monthOfYear + 1,
                                dayOfMonth))
        );

        dateOfNotes.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                datePicker.setVisibility(DatePicker.VISIBLE);
            } else {
                dateOfNotes.setText(String.format("%d.%d.%d",
                        datePicker.getYear(),
                        datePicker.getMonth() + 1,
                        datePicker.getDayOfMonth()));
                datePicker.setVisibility(DatePicker.GONE);
            }
        });

        datePicker.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
            datePicker.setVisibility(DatePicker.GONE);
            dateOfNotes.clearFocus();
        });
        Log.d(LOG, "onCreateView()");

        return view;
    }

    private void createContext() {
        titleOfNotes.setText(notes.getTitle());
        descriptionOfNotes.setText(notes.getDescription());
        dateOfNotes.setText(notes.getDateOfCreated().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInstance();
        Log.d(LOG, "onPause()");
    }

    private void saveInstance() {
        String dateInString = dateOfNotes.getText().toString();
        try {
            Date date = new SimpleDateFormat("yyyy.MM.dd").parse(dateInString);
            notes.setDateOfCreated(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notes.setDescription(descriptionOfNotes.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NOTES, notes);
        setArguments(bundle);
    }
}