package com.example.slt_project.ui.T2S;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public interface T2SContract {
    interface IT2SFragment{

        void showImage(Character c);
    }

    interface IT2SPresenter{

        String toPinIn(String str);
    }

    interface IT2SModel{

    }
}
