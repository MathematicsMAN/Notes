package com.example.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class NotesFragment extends Fragment {

    public static final String CURRENT_NOTES = "CurrentNotes";
    private int currentPosition = 0;
    private Notes[] notesList;
    private boolean isLandscape;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(CURRENT_NOTES, 0);
            notesList[currentPosition] = (Notes) savedInstanceState.getSerializable(ContentOfNotesFragment.KEY_NOTES);
        }

        if (isLandscape) {
            showLandContentOfNotes(currentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_NOTES, currentPosition);
        outState.putSerializable(ContentOfNotesFragment.KEY_NOTES, notesList[currentPosition]);
        super.onSaveInstanceState(outState);
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notesTitle = getResources().getStringArray(R.array.notes_titles);
        String[] description = getResources().getStringArray(R.array.notes_description);
        String[] dataOfCreated = getResources().getStringArray(R.array.notes_data_of_created);
        notesList = new Notes[notesTitle.length];

        for (int i = 0; i < notesTitle.length; i++) {
            Notes notes = new Notes(notesTitle[i], description[i], dataOfCreated[i]);
            notesList[i] = notes;

            TextView tv = new TextView(getContext());
            tv.setText(notesTitle[i]);
            tv.setTextSize(30);
            layoutView.addView(tv);

            final int index = i;
            tv.setOnClickListener(v -> {
                currentPosition = index;
                showContentOfNotes(currentPosition);
            });
        }
    }

    private void showContentOfNotes(int index) {
        if (isLandscape) {
            showLandContentOfNotes(index);
        } else {
            showPortContentOfNotes(index);
        }
    }

    private void showLandContentOfNotes(int index) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_of_notes, ContentOfNotesFragment.newInstance(index, notesList[index]))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showPortContentOfNotes(int index) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ContentOfNotesActivity.class);
        intent.putExtra(ContentOfNotesFragment.ARG_INDEX, index);
        intent.putExtra(ContentOfNotesFragment.KEY_NOTES, notesList[index]);
        startActivity(intent);
    }
}