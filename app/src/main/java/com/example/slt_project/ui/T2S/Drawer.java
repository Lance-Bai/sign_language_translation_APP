package com.example.slt_project.ui.T2S;

import android.os.AsyncTask;



public class Drawer extends AsyncTask<String,String,Void> {
    T2SContract.IT2SFragment fragment;

    public Drawer(T2SContract.IT2SFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Character label=null;
        for (Character c: strings[0].toCharArray()) {
            if(label!=null){
                if(((label.equals('z')||label.equals('c')||label.equals('s'))&&c.equals('h'))||(label.equals('n')&&c.equals('g'))){
                    publishProgress(label +c.toString());
                }else{
                    publishProgress(label.toString());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    publishProgress(c.toString());
                }
            label=null;
            }else{
                if(c.equals('z')||c.equals('c')||c.equals('s')||c.equals('n')){
                    label=c;
                    continue;
                }
                publishProgress(c.toString());
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... strings) {
        fragment.showImage(strings[0]);

    }
}