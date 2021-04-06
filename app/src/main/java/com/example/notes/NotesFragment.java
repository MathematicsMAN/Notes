package com.example.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class NotesFragment extends Fragment {

    private final String LOG = "NOTES";

    private Notes[] notesList;
    private Notes currentNote;
    private int currentIndex;
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
                currentNote = notes;
                currentIndex = index;
                showContentOfNotes(currentNote);
            });
            tv.setOnLongClickListener(v -> {
                        initPopupMenu(tv);
                        return true;
                    }
            );
        }
        Log.d(LOG, "NotesFragment.initList()");
    }

    private void initPopupMenu(TextView text) {
        Activity activity = requireActivity();
        PopupMenu popupMenu = new PopupMenu(activity, text);
        activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.popup_add:
                    Toast.makeText(getContext(), "Add new notes", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_delete:
                    Toast.makeText(getContext(), "Delete this notes", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_rename:
                    Toast.makeText(getContext(), "Rename this notes", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.popup_clear:
                    Toast.makeText(getContext(), "Clear all notes", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return true;
        });
        popupMenu.show();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(ContentOfNotesFragment.KEY_NOTES, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = (Notes) savedInstanceState.getSerializable(ContentOfNotesFragment.KEY_NOTES);
            notesList[currentIndex] = currentNote;
        } else {
            currentNote = new Notes(notesList[0].getTitle(),
                    notesList[0].getDescription(),
                    notesList[0].getDataOfCreated());
        }

        if (isLandscape) {
            showLandContentOfNotes(currentNote);
        }
        Log.d(LOG, "NotesFragment.onActivityCreated()");
    }

    private void showContentOfNotes(Notes currentNote) {
        if (isLandscape) {
            showLandContentOfNotes(currentNote);
        } else {
            showPortContentOfNotes(currentNote);
        }
    }

    private void showLandContentOfNotes(Notes currentNote) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_of_notes, ContentOfNotesFragment.newInstance(currentNote))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showPortContentOfNotes(Notes currentNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ContentOfNotesActivity.class);
        intent.putExtra(ContentOfNotesFragment.KEY_NOTES, currentNote);
        startActivity(intent);
    }
}