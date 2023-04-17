package com.example.slt_project.ui.T2S;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.ImageAdapter;
import com.example.slt_project.ui.TextOutputAdapter;
import com.example.slt_project.ui.base.BaseFragment;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class T2SFragment extends BaseFragment implements T2SContract.IT2SFragment {
    List<String> textList = new ArrayList<>();
    private RecyclerView t2s_recyclerView, t2s_imageRecycler;
    private TextOutputAdapter adapter;
    private Button sendButton;
    private EditText editText;
    List<Term> terms = new ArrayList<>();
    private ImageAdapter imageAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_t2s;
    }

    @Override
    protected void initViews() {
        t2s_recyclerView = find(R.id.t2s_recycle);
        t2s_imageRecycler = find(R.id.t2s_imageRecycler);
        sendButton = find(R.id.send_input_text_button);
        editText = find(R.id.input_text);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        t2s_recyclerView.setLayoutManager(layoutManager);
        adapter = new TextOutputAdapter(textList);
        t2s_recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        t2s_imageRecycler.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(getImageUrls()); // 创建适配器，并传入图片 URL 集合
        t2s_imageRecycler.setAdapter(imageAdapter); // 设置适配器
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                String word[] = new String[100], natureStr;
                if (!message.isEmpty()) {
                    adapter.addText(message);
                    editText.setText("");
                    int itemCount = adapter.getItemCount();
                    layoutManager.scrollToPositionWithOffset(itemCount - 1, 0);

// 滚动到最后一条消息
                    terms = ToAnalysis.parse(message).getTerms();
                    if (terms.size() != 0) {
                        for (int i = 0; i < terms.size(); i++) {
                            word[i] = terms.get(i).getName();
                            natureStr = terms.get(i).getNatureStr();
                        }
                        String join = Arrays.stream(word)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining(" "));
                        adapter.addText(join);
                    }

                }

            }
        });
    }
    private List<String> getImageUrls() {
        // 根据需要动态生成图片 URL 集合
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://t7.baidu.com/it/u=825057118,3516313570&fm=193&f=GIF");
        imageUrls.add("https://t7.baidu.com/it/u=825057118,3516313570&fm=193&f=GIF");
        return imageUrls;
    }

}