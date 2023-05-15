package com.example.slt_project.ui.T2S;
import android.util.Log;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class T2SPresenter implements T2SContract.IT2SPresenter {
    private T2SContract.IT2SModel model;
    private T2SContract.IT2SFragment fragment;

    T2SPresenter(T2SContract.IT2SFragment fragment){
        this.fragment =  fragment;
    }


    @Override
    public String toPinIn(String str){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        String out="";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // is Chinese?
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                // all possible pinyin
                String[] pinyins = new String[0];
                try {
                    pinyins = PinyinHelper.toHanyuPinyinStringArray(c,format);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    throw new RuntimeException(e);
                }
                // get first
                if (pinyins != null && pinyins.length > 0) {
                    out+=pinyins[0];
                }
            } else {
                Log.e("Pinyin", "toPinIn: Failed");
            }
        }
        return out;
    }


}
