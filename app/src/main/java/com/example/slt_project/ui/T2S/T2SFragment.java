package com.example.slt_project.ui.T2S;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.TextOutputAdapter;
import com.example.slt_project.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class T2SFragment extends BaseFragment implements T2SContract.IT2SFragment, View.OnClickListener {
    List<String> textList = new ArrayList<>();
    boolean isListening;
    private TextOutputAdapter adapter;
    private EditText editText;

    private ImageView signImage;
    private LinearLayoutManager layoutManager;
    private T2SContract.IT2SPresenter presenter;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_t2s;
    }

    @Override
    protected void initViews() {
        presenter = new T2SPresenter(this);
        RecyclerView t2s_recyclerView = find(R.id.t2s_recycle);
        t2s_recyclerView.setOnClickListener(this);
        signImage = find(R.id.t2s_imageView);
        signImage.setOnClickListener(this);
//        t2s_imageRecycler = find(R.id.t2s_imageRecycler);
        Button sendButton = find(R.id.send_input_text_button);
        editText = find(R.id.input_text);
        Button voiceButton = find(R.id.voice);
        voiceButton.setOnClickListener(this);

        
        layoutManager = new LinearLayoutManager(getContext());
        t2s_recyclerView.setLayoutManager(layoutManager);
        adapter = new TextOutputAdapter(textList, presenter);
        t2s_recyclerView.setAdapter(adapter);
        isListening=false;
        sendButton.setOnClickListener(this);

    }
    
    private void hideKeyboard() {
        Activity activity = getActivity();
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_input_text_button) {
            String message = editText.getText().toString();
            String pinyin = presenter.toPinIn(message);
            Log.i("pinyin", pinyin);
            new Drawer(this).execute(pinyin);

            if (!message.isEmpty()) {
                adapter.addText(message);
                editText.setText("");
                int itemCount = adapter.getItemCount();
                layoutManager.scrollToPositionWithOffset(itemCount - 1, 0);
            }
        } else if (v.getId()==R.id.voice) {
            recorder_Intent();
        }
        hideKeyboard();

    }
    private final static int REQUEST_RECORDER = 107;

    public void recorder_Intent(){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent,REQUEST_RECORDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && REQUEST_RECORDER == requestCode){
            assert data != null;
            Uri uri = data.getData();
            presenter.Voice2Text(uri);
        }
    }

    @Override
    public void showImage(Character c){
        String imageName="sign_"+c.toString();
        @SuppressLint("DiscouragedApi") int imageId = getResources().getIdentifier(imageName, "drawable", this.getMainActivity().getPackageName());
        @SuppressLint("UseCompatLoadingForDrawables") Drawable image = getResources().getDrawable(imageId);
        signImage.setImageDrawable(image);
    }
    @Override
    public void setText(String s){
        editText.setText(s);
    }

}