package com.kefawatch.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kefawatch.app.network.dto.TitlesListDto;

import java.util.ArrayList;
import java.util.List;

public class TitlesAdapter extends RecyclerView.Adapter<TitlesAdapter.Holder> {

    public interface OnTitleClickListener {
        void onTitleClick(long id, String name);
    }

    private final List<TitlesListDto.TitleItem> items = new ArrayList<>();
    private final OnTitleClickListener listener;

    public TitlesAdapter(OnTitleClickListener listener) {
        this.listener = listener;
    }

    public void submit(List<TitlesListDto.TitleItem> next) {
        items.clear();
        items.addAll(next);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TitlesListDto.TitleItem item = items.get(position);
        holder.name.setText(item.name);
        holder.meta.setText(item.type + " · id=" + item.id);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTitleClick(item.id, item.name);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView meta;

        Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textTitleName);
            meta = itemView.findViewById(R.id.textTitleMeta);
        }
    }
}
