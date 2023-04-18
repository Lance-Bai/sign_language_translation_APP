package com.example.slt_project.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.S2T.S2TContract;
import com.example.slt_project.ui.S2T.S2TPresenter;

public class TextOutputViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textOutput;
    private S2TContract.IS2TPresenter presenter;
    private Button speakButton;


    public TextOutputViewHolder(@NonNull View itemView, S2TContract.IS2TPresenter presenter) {
        super(itemView);
        textOutput = itemView.findViewById(R.id.s2t_recyclerItem);
        speakButton = itemView.findViewById(R.id.s2t_item_button);
        speakButton = itemView.findViewById(R.id.speakButton);
        speakButton.setOnClickListener(this);
        this.presenter = presenter;
    }

    public void bind(String text) {
        textOutput.setText(text);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.speakButton){
            presenter.speak((String) textOutput.getText());
        }
    }
}

