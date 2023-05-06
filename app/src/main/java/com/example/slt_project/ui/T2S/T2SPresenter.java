package com.example.slt_project.ui.T2S;

import com.example.slt_project.ui.S2T.S2TContract;

public class T2SPresenter implements T2SContract.IT2SPresenter {
    private T2SContract.IT2SModel model;
    private T2SContract.IT2SFragment fragment;

    T2SPresenter(T2SContract.IT2SFragment fragment){
        this.fragment =  fragment;
    }
}
