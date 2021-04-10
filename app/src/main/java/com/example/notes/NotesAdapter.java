package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private CardSource<Notes> cardSource;

    public NotesAdapter(CardSource<Notes> cardSource) {
        this.cardSource = cardSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cardSource.getCardData(position));
    }

    @Override
    public int getItemCount() {
        return cardSource.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final EditText dataOfCreated;
        private final CheckBox asCompleted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title_of_notes);
            asCompleted = itemView.findViewById(R.id.item_check_notes_as_completed);
            dataOfCreated = itemView.findViewById(R.id.item_date_of_created_notes);
        }

        public void bind(Notes cardData) {
            title.setText(cardData.getTitle());
            asCompleted.setChecked(cardData.isAsChecked());
            dataOfCreated.setText(cardData.getDataOfCreated());
        }
    }

}
