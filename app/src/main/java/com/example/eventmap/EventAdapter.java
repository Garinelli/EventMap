package com.example.eventmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventmap.db.Event;
import com.example.eventmap.db.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private AppDatabase database;

    // Конструктор адаптера
    public EventAdapter(List<Event> events, AppDatabase database) {
        this.events = events;
        this.database = database;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.descriptionText.setText(event.description);

        // Обработка клика по кнопке удаления
        holder.deleteButton.setOnClickListener(v -> {
            // Удаляем событие из базы данных
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                database.eventDao().delete(event);

                // Удаляем событие из списка и уведомляем адаптер
                events.remove(position);
                holder.itemView.post(() -> notifyItemRemoved(position));
            });
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionText;
        Button deleteButton;

        public EventViewHolder(View itemView) {
            super(itemView);
            descriptionText = itemView.findViewById(R.id.eventDescription); // TextView для описания
            deleteButton = itemView.findViewById(R.id.deleteButton); // Кнопка удаления
        }
    }
}
