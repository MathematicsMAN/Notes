package com.example.notes;

public interface CardSource<T> {
    CardSource<T> init(CardSourceResponse<T> cardSourceResponse);

    T getCardData(int position);

    int size();

    void deleteCardData(int position);

    void updateCardData(int position, T cardData);

    void addCardData(T cardData);

    void clearData();
}
