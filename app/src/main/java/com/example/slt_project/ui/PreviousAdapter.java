package com.example.slt_project.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.activity.SendPreviousActivity;

import java.util.List;

public class PreviousAdapter extends RecyclerView.Adapter<PreviousHolder>{
    private final List<String> textList;
    private final SendPreviousActivity activity;
    public PreviousAdapter(List<String> textList, SendPreviousActivity activity){
        this.textList=textList;
        this.activity=activity;
    }

    @NonNull
    @Override
    public PreviousHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_item, parent, false);
        return new PreviousHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousHolder holder, int position) {
        holder.bind(textList.get(position));
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }
}
