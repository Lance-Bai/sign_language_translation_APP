package com.example.slt_project.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.S2T.S2TContract;
import com.example.slt_project.ui.S2T.SendPhoto;
import com.example.slt_project.ui.activity.SendPreviousActivity;

import java.io.File;

public class PreviousHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textView;
    private ImageView imageView;
    private SendPreviousActivity activity;
    private S2TContract.IS2TPresenter presenter;
    public PreviousHolder(@NonNull View itemView, SendPreviousActivity activity) {
        super(itemView);
        this.activity=activity;
        textView=itemView.findViewById(R.id.previous_item_text);
        textView.setOnClickListener(this);
        imageView=itemView.findViewById(R.id.previous_imgView);


    }
    public void bind(String s){
        textView.setText(s);
        String path = activity.getExternalFilesDir(null)+"/"+s;
        if (path.contains("jpg")) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
//            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//            mThumbnail.setRotation(ORIENTATION.get(rotation));
        } else if (path.contains("mp4")) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            Bitmap bitmap = retriever.getFrameAtTime(1);
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.previous_item_text){
            Intent intent = new Intent();
            intent.putExtra("data_return", textView.getText().toString());
            activity.setResult(RESULT_OK, intent);
            activity.finish();
        }
    }
}
