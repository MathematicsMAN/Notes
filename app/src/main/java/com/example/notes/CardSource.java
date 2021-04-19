package com.example.notes;

public interface CardSource<T> {
        T getCardData(int position);
        int size();
}
