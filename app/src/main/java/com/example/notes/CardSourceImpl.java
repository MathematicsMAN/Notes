package com.example.notes;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class CardSourceImpl implements CardSource<Notes> {

    private final List<Notes> dataSource;
    private final Resources resources;

    public CardSourceImpl(Resources resources) {
        this.dataSource = new ArrayList<>();
        this.resources = resources;
    }

    public CardSourceImpl init() {
        String[] notesTitle = resources.getStringArray(R.array.notes_titles);
        String[] description = resources.getStringArray(R.array.notes_description);
        String[] dataOfCreated = resources.getStringArray(R.array.notes_data_of_created);

        for (int i = 0; i < notesTitle.length; i++) {
            dataSource.add(new Notes(notesTitle[i],
                    description[i],
                    dataOfCreated[i],
                    false));
        }
        return this;
    }

    @Override
    public Notes getCardData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }
}
