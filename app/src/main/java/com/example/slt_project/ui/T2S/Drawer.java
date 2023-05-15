package com.example.slt_project.ui.T2S;

import android.os.AsyncTask;



public class Drawer extends AsyncTask<String,Character,Void> {
    T2SContract.IT2SFragment fragment;

    public Drawer(T2SContract.IT2SFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(String... strings) {
        for (Character c: strings[0].toCharArray()) {
            publishProgress(c);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Character... characters) {
        fragment.showImage(characters[0]);

    }
}