package com.example.slt_project.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.activity.SendPreviousActivity;

import java.io.File;

public class PreviousHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView textView;
    private final ImageView imageView;
    private final SendPreviousActivity activity;

    private String path;

    public PreviousHolder(@NonNull View itemView, SendPreviousActivity activity) {
        super(itemView);
        this.activity=activity;
        textView=itemView.findViewById(R.id.previous_item_text);
        textView.setOnClickListener(this);
        imageView=itemView.findViewById(R.id.previous_imgView);
        imageView.setOnClickListener(this);


    }
    public void bind(String s){
        textView.setText(s);
        path = activity.getExternalFilesDir(null)+"/"+s;
        if (path.contains("jpg")) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
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
        }else if (v.getId()==R.id.previous_imgView){
            goGallery(path);
        }
    }

    private void goGallery(String path){
        Uri uri = FileProvider.getUriForFile(activity, "com.example.slt_project.fileprovider", new File(path));
        String type="";
        if(path.contains(".mp4"))type="video/*";
        if(path.contains(".jpg"))type="image/*";

        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(uri, type);
        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
        activity.startActivity(it);
    }
}
