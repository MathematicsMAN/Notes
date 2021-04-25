package com.example.notes;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private CardSource<Notes> cardSource;
    public static OnItemClickListener listener;
    public static Fragment fragment;
    public static int menuPosition;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setCardSource(CardSource<Notes> cardSource) {
        this.cardSource = cardSource;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        if (cardSource != null) {
            return cardSource.size();
        } else {
            return 0;
        }
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final EditText dataOfCreated;
        private final CheckBox asCompleted;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title_of_notes);
            asCompleted = itemView.findViewById(R.id.item_check_notes_as_completed);
            dataOfCreated = itemView.findViewById(R.id.item_date_of_created_notes);

            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return true;
                });
                fragment.registerForContextMenu(itemView);
            }

            title.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });

            title.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu(10, 10);
                return true;
            });
        }

        public void bind(Notes cardData) {
            title.setText(cardData.getTitle());
            asCompleted.setChecked(cardData.isAsChecked());
            dataOfCreated.setText(cardData.getDateOfCreated().toString());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }
}
