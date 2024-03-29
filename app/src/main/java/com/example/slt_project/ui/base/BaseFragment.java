package com.example.slt_project.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseFragment extends Fragment {

    protected View contentView;
    protected abstract int getLayoutID();
    protected abstract void initViews() ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutID(), container,false);
        initViews();
        return contentView;
    }

    protected <T extends View> T find(@IdRes int id){
        return contentView.findViewById(id);
    }

    public FragmentActivity getMainActivity() {
        return this.getActivity();
    }

}
