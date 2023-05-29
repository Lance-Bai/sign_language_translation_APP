package com.example.slt_project.ui.T2S;

import static android.app.Activity.RESULT_OK;

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

import org.ansj.domain.Term;

import java.util.ArrayList;
import java.util.List;

public class T2SFragment extends BaseFragment implements T2SContract.IT2SFragment, View.OnClickListener {
    List<String> textList = new ArrayList<>();
    boolean isListening;
    private RecyclerView t2s_recyclerView;
    private TextOutputAdapter adapter;
    private Button sendButton;
    private EditText editText;
    List<Term> terms = new ArrayList<>();

    private ImageView signImage;
    private LinearLayoutManager layoutManager;
    private T2SContract.IT2SPresenter presenter;
    private SpeechRecognizer speechRecognizer;
    private Button voiceButton;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_t2s;
    }

    @Override
    protected void initViews() {
        presenter = new T2SPresenter(this);
        t2s_recyclerView = find(R.id.t2s_recycle);
        t2s_recyclerView.setOnClickListener(this);
        signImage = find(R.id.t2s_imageView);
        signImage.setOnClickListener(this);
//        t2s_imageRecycler = find(R.id.t2s_imageRecycler);
        sendButton = find(R.id.send_input_text_button);
        editText = find(R.id.input_text);
        voiceButton=find(R.id.voice);
        voiceButton.setOnClickListener(this);

        
        layoutManager = new LinearLayoutManager(getContext());
        t2s_recyclerView.setLayoutManager(layoutManager);
        adapter = new TextOutputAdapter(textList, presenter);
        t2s_recyclerView.setAdapter(adapter);
        isListening=false;
        sendButton.setOnClickListener(this);


//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.getMainActivity());
//        speechRecognizer.setRecognitionListener(new RecognitionListener() {
//            @Override
//            public void onEvent(int eventType, Bundle params) {
//                // 当识别事件发生时调用
//                // 这里可以保持为空
//            }
//            @Override
//            public void onPartialResults(Bundle partialResults) {
//                // 当部分识别结果可用时调用
//                // 这里可以保持为空
//            }
//            @Override
//            public void onBufferReceived(byte[] buffer) {
//                // 当音频缓冲区可用时调用
//                // 这里可以保持为空
//            }
//            @Override
//            public void onReadyForSpeech(Bundle params) {
//                // 当识别准备就绪时调用
//            }
//
//            @Override
//            public void onBeginningOfSpeech() {
//                // 当用户开始说话时调用
//            }
//
//            @Override
//            public void onRmsChanged(float rmsdB) {
//                // 音量变化时调用
//            }
//
//            @Override
//            public void onEndOfSpeech() {
//                // 当用户停止说话时调用
//            }
//
//            @Override
//            public void onError(int error) {
//                // 当发生错误时调用
//            }
//
//            @Override
//            public void onResults(Bundle results) {
//                // 识别结果返回时调用
//                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                if (matches != null && !matches.isEmpty()) {
//                    String recognizedText = matches.get(0);
//                    editText.setText(recognizedText);
//                }
//            }
//
//        });
//        Log.i("t2s", String.valueOf(SpeechRecognizer.isRecognitionAvailable(this.getMainActivity())));
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
//            Drawable db = getResources().getDrawable(R.drawable.sign_a);
//            signImage.setImageDrawable(db);
            String message = editText.getText().toString();
            String pinyin = presenter.toPinIn(message);
            Log.i("pinyin", pinyin);
            new Drawer(this).execute(pinyin);

            String word[] = new String[100], natureStr;
            if (!message.isEmpty()) {
                adapter.addText(message);
                editText.setText("");
                int itemCount = adapter.getItemCount();
                layoutManager.scrollToPositionWithOffset(itemCount - 1, 0);
            }
        } else if (v.getId()==R.id.voice) {
//            if(isListening){
//                speechRecognizer.stopListening();
//                isListening=false;
//            }else{
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                // 启动语音识别
//                speechRecognizer.startListening(intent);
//                isListening=true;
//            }
            recorder_Intent();

        }
        hideKeyboard();

    }
    private final static int REQUEST_RECORDER = 107;
    private Uri uri;
    public void recorder_Intent(){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent,REQUEST_RECORDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && REQUEST_RECORDER == requestCode){
            uri = data.getData();
            presenter.Voice2Text(uri);
        }
    }

    @Override
    public void showImage(Character c){
        String imageName="sign_"+c.toString();
        String zz=this.getMainActivity().getPackageName();
        int imageId = getResources().getIdentifier(imageName, "drawable", this.getMainActivity().getPackageName());
        Drawable image = getResources().getDrawable(imageId);
        signImage.setImageDrawable(image);
    }
    @Override
    public void setText(String s){
        editText.setText(s);
    }

}