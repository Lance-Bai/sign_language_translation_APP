package com.example.slt_project.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;

public class TextOutputViewHolder extends RecyclerView.ViewHolder {
    private Button itembutton;
    private TextView textOutput;

    public TextOutputViewHolder(@NonNull View itemView) {
        super(itemView);
        textOutput = itemView.findViewById(R.id.s2t_recyclerItem);
        itembutton = itemView.findViewById(R.id.s2t_item_button);
    }

    public void bind(String text) {
        textOutput.setText(text);
    }

}

