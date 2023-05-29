package com.example.slt_project.ui.Setting;

public interface SettingContract {
    interface ISettingFragment{

    }

    interface ISettingPresenter{
        void setNightMode(Boolean b);
        void setPhotoMode(Boolean b);

        void setLanguage(String s);

    }

    interface ISettingModel{

    }
}
