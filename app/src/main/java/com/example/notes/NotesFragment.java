package com.example.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesFragment extends Fragment {

    private static final int REQUIRE_CODE = 90;
    public static final String DATE_FORMAT = "yyyy.MM.dd";
    private final String LOG = "NotesFragment";

    private CardSource<Notes> cardSource;
    private NotesAdapter adapter;
    private Notes currentNote;
    private RecyclerView recyclerView;
    private boolean isLandscape;
    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        activity = (AppCompatActivity) getActivity();

        initRecyclerView(view);
        setHasOptionsMenu(true);

        Toolbar toolbar = initToolbar(view);
//        initDrawer(view, toolbar);

        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter(this);

        cardSource = new CardSourceFirebaseImpl<Notes>().init(cs ->
                adapter.notifyDataSetChanged()
        );
        adapter.setCardSource(cardSource);
        recyclerView.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
            itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
            recyclerView.addItemDecoration(itemDecoration);
        }

        adapter.setOnItemClickListener((view1, position) ->
            showContentOfNotes(position)
        );

        adapter.setOnCheckedChangeListener((view1, position, isChecked) -> {
            cardSource.getCardData(position).setAsChecked(isChecked);
//            adapter.notifyItemChanged(position);
        });

        Log.d(LOG, "initRecyclerView()");
    }

    private void initDrawer(View view, Toolbar toolbar) {
        final DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            int position = adapter.getMenuPosition();
            switch (id) {
                case R.id.drawer_add:
                    addNewNotes();
                    break;
                case R.id.drawer_delete:
                    deleteNotes(position);
                    break;
                case R.id.drawer_edit:
                    editNotes(position);
                    break;
                case R.id.drawer_clear:
                    clearAllNotes();
                    break;
                case R.id.drawer_about:
                    Toast.makeText(getContext(), "Made for people", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    return false;
            }
            drawer.closeDrawer(GravityCompat.START);
            Log.d(LOG, "initDrawer()");
            return true;
        });
    }

    private Toolbar initToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.setSupportActionBar(toolbar);
        }
        return toolbar;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                addNewNotes();
                return true;
            case R.id.menu_clear:
                clearAllNotes();
                return true;
            case R.id.menu_refresh:
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.popup, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.popup_delete:
                deleteNotes(position);
                return true;
            case R.id.popup_edit:
                editNotes(position);
                return true;
            case R.id.popup_add:
                addNewNotes();
                return true;
            case R.id.popup_clear:
                clearAllNotes();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putSerializable(ContentOfNotesFragment.KEY_NOTES, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
//            currentNote = (Notes) savedInstanceState.getSerializable(ContentOfNotesFragment.KEY_NOTES);
            currentNote = cardSource.getCardData(0);
        } else {

//            currentNote = cardSource.getCardData(0);
        }

        if (isLandscape) {
            showLandContentOfNotes(currentNote);
        }
        Log.d(LOG, "onActivityCreated()");
    }

    private void showContentOfNotes(int position) {
        if (isLandscape) {
            showLandContentOfNotes(cardSource.getCardData(position));
        } else {
//            cardSource.getCardData(position)
//            showPortContentOfNotes(currentNote);
            editNotes(position);
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
        intent.setClass(activity, ContentOfNotesActivity.class);
        intent.putExtra(ContentOfNotesFragment.KEY_NOTES, currentNote);
        startActivityForResult(intent, REQUIRE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == REQUIRE_CODE) {
            if (resultCode == activity.RESULT_OK) {
                currentNote = (Notes) data.getSerializableExtra(ContentOfNotesFragment.KEY_NOTES);
                int position = data.getIntExtra(ContentOfNotesFragment.KEY_POSITION, 0);
                cardSource.updateCardData(position, currentNote);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addNewNotes() {
        final View contentView = getLayoutInflater().inflate(R.layout.fragment_content_of_notes, null);
        EditText titleOfNotes = contentView.findViewById(R.id.title_of_notes);
        EditText descriptionOfNotes = contentView.findViewById(R.id.description_of_notes);
        EditText dateOfNotes = contentView.findViewById(R.id.date_of_created_notes);

        titleOfNotes.setText("Title");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        dateOfNotes.setText(sdf.format(new Date()));

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.add_new)
                .setView(contentView)
                .setPositiveButton(R.string.button_save, (dialog, which) -> {
                    String dateInString = dateOfNotes.getText().toString();
                    try {
                        cardSource.addCardData(new Notes(titleOfNotes.getText().toString(),
                                descriptionOfNotes.getText().toString(),
                                sdf.parse(dateInString),
                                false));
                        adapter.notifyItemInserted(cardSource.size() - 1);
                        recyclerView.scrollToPosition(cardSource.size() - 1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void editNotes(int position) {
        final View contentView = getLayoutInflater().inflate(R.layout.fragment_content_of_notes, null);
        EditText titleOfNotes = contentView.findViewById(R.id.title_of_notes);
        EditText descriptionOfNotes = contentView.findViewById(R.id.description_of_notes);
        EditText dateOfNotes = contentView.findViewById(R.id.date_of_created_notes);

        titleOfNotes.setText(cardSource.getCardData(position).getTitle());
        descriptionOfNotes.setText(cardSource.getCardData(position).getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        dateOfNotes.setText(sdf.format(cardSource.getCardData(position).getDateOfCreated()));

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.edit_note)
                .setView(contentView)
                .setPositiveButton(R.string.button_save, (dialog, which) -> {
                    String dateInString = dateOfNotes.getText().toString();
                    try {
                        Notes note = new Notes(titleOfNotes.getText().toString(),
                                descriptionOfNotes.getText().toString(),
                                sdf.parse(dateInString),
                                false);
                        note.setId(cardSource.getCardData(position).getId());
                        cardSource.updateCardData(position, note);
                        adapter.notifyItemChanged(position);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.button_cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void deleteNotes(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.warning)
                .setMessage("This card will be deleted. Are you sure?")
                .setCancelable(true)
                .setNegativeButton(R.string.no, (dialog, which) -> {
                })
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    cardSource.deleteCardData(position);
                    adapter.notifyItemRemoved(position);
                })
                .show();
    }

    private void clearAllNotes() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.warning)
                .setMessage("All cards will be deleted. Are you sure?")
                .setCancelable(true)
                .setNegativeButton(R.string.no, (dialog, which) -> {
                })
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    cardSource.clearData();
                    adapter.notifyDataSetChanged();
                })
                .show();
    }
}