package com.example.slt_project.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;

import java.util.List;

public class TextOutputAdapter extends RecyclerView.Adapter<TextOutputViewHolder> {

    private List<String> textList;

    public TextOutputAdapter(List<String> textList) {
        this.textList = textList;
    }

    @NonNull
    @Override
    public TextOutputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s2t_recycler_item, parent, false);
        return new TextOutputViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TextOutputViewHolder holder, int position) {
        holder.bind(textList.get(position));
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public void addText(String text) {
        textList.add(text);
        notifyItemInserted(textList.size() - 1);
    }

    public void clearText() {
        int size = textList.size();
        textList.clear();
        notifyItemRangeRemoved(0, size);
    }

}
