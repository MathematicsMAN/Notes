package com.example.notes;

public interface CardSourceResponse<T> {
    void initialized(CardSource<T> cardSource);
}
