package com.example.eventmap;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmap.db.AppDatabase;
import com.example.eventmap.db.Event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private AppDatabase database;

    public EventAdapter(List<Event> events, AppDatabase database) {
        this.events = events;
        this.database = database;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.descriptionText.setText(event.description);

        holder.deleteButton.setOnClickListener(v -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                database.eventDao().delete(event);
                events.remove(position);
                holder.itemView.post(() -> notifyItemRemoved(position));
            });
        });

        holder.editButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Редактировать описание");

            final EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(event.getDescription());
            builder.setView(input);

            builder.setPositiveButton("Сохранить", (dialog, which) -> {
                String newDescription = input.getText().toString().trim();
                if (!newDescription.isEmpty()) {
                    event.setDescription(newDescription);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        database.eventDao().update(event);
                        holder.itemView.post(() -> {
                            events.set(position, event);
                            notifyItemChanged(position);
                        });
                    });
                }
            });

            builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionText;
        Button deleteButton;
        Button editButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionText = itemView.findViewById(R.id.eventDescription);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
