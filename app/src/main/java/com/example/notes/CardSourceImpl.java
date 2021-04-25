package com.example.notes;

import android.annotation.SuppressLint;
import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardSourceImpl implements CardSource<Notes> {

    private final List<Notes> dataSource;
    private final Resources resources;

    public CardSourceImpl(Resources resources) {
        this.dataSource = new ArrayList<>();
        this.resources = resources;
    }

    public CardSource<Notes> init(CardSourceResponse cardSourceResponse) {
        String[] notesTitle = resources.getStringArray(R.array.notes_titles);
        String[] description = resources.getStringArray(R.array.notes_description);
        String[] dataOfCreated = resources.getStringArray(R.array.notes_data_of_created);

        for (int i = 0; i < notesTitle.length; i++) {
            try {
                @SuppressLint("SimpleDateFormat")
                Date date = new SimpleDateFormat("yyyy.MM.dd").parse(dataOfCreated[i]);
                dataSource.add(new Notes(notesTitle[i],
                        description[i],
                        date,
                        false));
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (cardSourceResponse != null) {
            cardSourceResponse.initialized(this);

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

    @Override
    public void deleteCardData(int position) {
        if (position >= 0 && position < size()) {
            dataSource.remove(position);
        }
    }

    @Override
    public void updateCardData(int position, Notes cardData) {
        if (position >= 0 && position < size()) {
            dataSource.set(position, cardData);
        }
    }

    @Override
    public void addCardData(Notes cardData) {
        dataSource.add(cardData);
    }

    @Override
    public void clearData() {
        dataSource.clear();
    }
}
