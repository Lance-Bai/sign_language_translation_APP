package com.example.slt_project.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.S2T.S2TContract;
import com.example.slt_project.ui.T2S.T2SContract;

import java.util.List;

public class TextOutputAdapter extends RecyclerView.Adapter<TextOutputViewHolder> {
    private S2TContract.IS2TPresenter presenter;
    private T2SContract.IT2SPresenter it2SPresenter;
    private final List<String> textList;

    public TextOutputAdapter(List<String> textList, S2TContract.IS2TPresenter presenter) {
        this.presenter = presenter;
        this.textList = textList;

    }

    public TextOutputAdapter(List<String> textList, T2SContract.IT2SPresenter it2SPresenter) {
        this.it2SPresenter = it2SPresenter;
        this.textList = textList;
    }


    @NonNull
    @Override
    public TextOutputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s2t_recycler_item, parent, false);
        if(presenter!=null){
            return new TextOutputViewHolder(view, presenter);
        }else{
            return new TextOutputViewHolder(view);
        }
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

}
