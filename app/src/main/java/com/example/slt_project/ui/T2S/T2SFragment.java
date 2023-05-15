package com.example.slt_project.ui.T2S;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    private RecyclerView t2s_recyclerView;
    private TextOutputAdapter adapter;
    private Button sendButton;
    private EditText editText;
    List<Term> terms = new ArrayList<>();

    private ImageView signImage;
    private LinearLayoutManager layoutManager;
    private T2SContract.IT2SPresenter presenter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_t2s;
    }

    @Override
    protected void initViews() {
        presenter = new T2SPresenter(this);
        t2s_recyclerView = find(R.id.t2s_recycle);
        signImage = find(R.id.t2s_imageView);
//        t2s_imageRecycler = find(R.id.t2s_imageRecycler);
        sendButton = find(R.id.send_input_text_button);
        editText = find(R.id.input_text);


        layoutManager = new LinearLayoutManager(getContext());
        t2s_recyclerView.setLayoutManager(layoutManager);
        adapter = new TextOutputAdapter(textList, presenter);
        t2s_recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(this);
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
}