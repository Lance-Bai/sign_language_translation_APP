package com.example.slt_project.ui.T2S;

import android.content.Context;
import android.net.Uri;

public interface T2SContract {
    interface IT2SFragment{

        void showImage(String s);

        void setText(String s);
    }

    interface IT2SPresenter{

        String toPinIn(String str);

        void Voice2Text(Uri uri);

        IT2SFragment getFragment();
    }

    interface IT2SModel{

    }
}
