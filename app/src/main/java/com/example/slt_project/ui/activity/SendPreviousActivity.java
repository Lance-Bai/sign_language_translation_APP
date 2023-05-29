package com.example.slt_project.ui.activity;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.PreviousAdapter;
import com.example.slt_project.ui.TextOutputAdapter;
import com.example.slt_project.ui.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SendPreviousActivity extends BaseActivity {

    private RecyclerView previous_recycleView;
    private PreviousAdapter adapter;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_previous;
    }

    @Override
    protected void initViews() {
        List<String> textList = getImageFilePath();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        previous_recycleView = find(R.id.previous_rv);
        previous_recycleView.setLayoutManager(layoutManager);
        adapter = new PreviousAdapter(textList, this);
        previous_recycleView.setAdapter(adapter);
    }

    public ArrayList<String> getImageFilePath() {
        ArrayList<String> imageList = new ArrayList<>();
        File file = new File(String.valueOf(this.getExternalFilesDir(null)));
        File[] dirEpub = file.listFiles();
        Arrays.sort(dirEpub,new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;
            }

            public boolean equals(Object obj) {
                return true;
            }
        });

        if (dirEpub.length != 0) {
            for (int i = 0; i < dirEpub.length; i++) {
                String fileName = dirEpub[i].toString();
                String fileNameSub = fileName.substring(fileName.lastIndexOf(File.separator)+1);
                if(!fileNameSub.contains(".jpg")&&!fileNameSub.contains(".mp4"))continue;
                imageList.add(fileNameSub);
            }
        }
        return imageList;
    }
}
