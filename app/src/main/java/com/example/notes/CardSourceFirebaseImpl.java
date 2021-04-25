package com.example.notes;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardSourceFirebaseImpl<T> implements CardSource {

    public static final String CARD_COLLECTION = "cards";
    public static final String TAG = "CardSourceFirebaseImpl";

    private final FirebaseFirestore store = FirebaseFirestore.getInstance();
    private final CollectionReference collection = store.collection(CARD_COLLECTION);
    private List<T> notes = new ArrayList<>();

    @Override
    public CardSource init(CardSourceResponse cardSourceResponse) {
        collection
                .orderBy(NotesMapping.Fields.DATEOFCREATED)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            Map<String, Object> doc = document.getData();
                            T note = (T) NotesMapping.toNotes(document.getId(), doc);
                            notes.add(note);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        return this;
    }

    @Override
    public T getCardData(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        return notes.size();
    }

    @Override
    public void deleteCardData(int position) {
        collection.document(((Notes) notes.get(position)).getId())
                .delete();
        notes.remove(position);
    }

    @Override
    public void updateCardData(int position, Object cardData) {
        collection.document(((Notes) notes.get(position)).getId())
                .set(NotesMapping.toDocument((Notes) cardData));
        notes.set(position, (T) cardData);
    }

    @Override
    public void addCardData(Object cardData) {
        collection.add(NotesMapping.toDocument((Notes) cardData))
                .addOnSuccessListener(documentReference ->
                    ((Notes) cardData).setId(documentReference.getId())
                );
        notes.add((T) cardData);
    }

    @Override
    public void clearData() {
        for (T note : notes) {
            collection.document(((Notes)note).getId()).delete();
        }
        notes.clear();
    }
}
