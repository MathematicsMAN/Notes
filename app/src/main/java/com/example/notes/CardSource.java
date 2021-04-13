package com.example.notes;

public interface CardSource<T> {
    T getCardData(int position);

    int size();

    void deleteCardData(int position);

    void updateCardData(int position, T cardData);

    void addCardData(T cardData);

    void clearData();
}
