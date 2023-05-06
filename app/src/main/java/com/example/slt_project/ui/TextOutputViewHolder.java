package com.example.slt_project.ui;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.S2T.S2TContract;
import com.example.slt_project.ui.S2T.S2TPresenter;
import com.example.slt_project.ui.T2S.T2SContract;

public class TextOutputViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textOutput;
    private S2TContract.IS2TPresenter presenter;
    private T2SContract.IT2SPresenter it2SPresenter;
    private Button speakButton;


    public TextOutputViewHolder(@NonNull View itemView, S2TContract.IS2TPresenter presenter) {
        super(itemView);
        textOutput = itemView.findViewById(R.id.s2t_recyclerItem);
        speakButton = itemView.findViewById(R.id.s2t_item_button);
        speakButton.setOnClickListener(this);
        this.presenter = presenter;
    }

    public TextOutputViewHolder(@NonNull View itemView, T2SContract.IT2SPresenter presenter) {
        super(itemView);
        textOutput = itemView.findViewById(R.id.s2t_recyclerItem);
        speakButton = itemView.findViewById(R.id.s2t_item_button);
        speakButton.setOnClickListener(this);
        this.it2SPresenter = presenter;
    }

    public void bind(String text) {
        textOutput.setText(text);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.s2t_item_button&&presenter!=null){
            presenter.speak((String) textOutput.getText());
        }
    }
}

