package com.example.slt_project.ui.S2T;

import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.widget.AutoScrollHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
//import com.example.slt_project.ui.DividerItemDecoration;
import com.example.slt_project.ui.TextOutputAdapter;
import com.example.slt_project.ui.base.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class S2TFragment extends BaseFragment implements S2TContract.IS2TFragment {
  //  private Button album;
  private RecyclerView s2t_recyclerView;
  private boolean isAutoPlay;
  private Handler autoScrollHandler;
  private TextOutputAdapter adapter;
  List<String> textList = new ArrayList<>();
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_s2t;
    }

    @Override
    protected void initViews() {
      s2t_recyclerView=find(R.id.s2t_recycle);
      LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
      s2t_recyclerView.setLayoutManager(layoutManager);
      adapter = new TextOutputAdapter(textList);
      s2t_recyclerView.setAdapter(adapter);

       autoScrollHandler=null;
      adapter.addText("Hello, world!");
      adapter.addText("11111111111111111111111111111111111111111111111111111! world!");
      adapter.addText("2! world!");
      adapter.addText("3! world!");
      adapter.addText("4! world!");
      adapter.addText("5! world!");
      adapter.addText("6! world!");
      adapter.addText("7! world!");
      adapter.addText("8! world!");
      adapter.addText("9! world!");
      adapter.addText("11! world!");
     // s2t_recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

//      ViewCompat.setOnApplyWindowInsetsListener(s2t_recyclerView, (v, insets) -> {
//        // 获取RecyclerView的垂直滚动条
//        View scrollBar = s2t_recyclerView.getChildAt(0);
//        if (scrollBar != null) {
//          // 设置滚动条可见性
//          scrollBar.setVisibility(View.VISIBLE);
//        }
//        return insets;
//      });
/*      s2t_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(s2t_recyclerView, dx, dy);
          // 更新滑动位置
          LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

          int firstVisibleItemIndex = linearLayoutManager.findFirstVisibleItemPosition();
          int lastVisibleItemIndex = linearLayoutManager.findLastVisibleItemPosition();
          int itemCount = adapter.getItemCount();
          float scrollPercentage = ((float) lastVisibleItemIndex / (float) itemCount) * 100;

          // 设置滚动条位置
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            s2t_recyclerView.setScrollbarFadingEnabled(false);
          }
          s2t_recyclerView.setVerticalScrollbarPosition(Math.round(scrollPercentage));
        }
      });*/
      // 给 RecyclerView 添加一个滚动监听器
      s2t_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
          // 在滚动时更新滚动条的位置
          int firstVisibleItemIndex = layoutManager.findFirstVisibleItemPosition();
          int lastVisibleItemIndex = layoutManager.findLastVisibleItemPosition();
          int itemCount = adapter.getItemCount();
          float scrollPercentage = ((float) lastVisibleItemIndex / (float) itemCount) * 100;
          recyclerView.setVerticalScrollbarPosition(Math.round(scrollPercentage));
        }
       /* @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);

          switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE: // 当前滚动状态为空闲状态
              int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
              int itemCount = recyclerView.getAdapter().getItemCount();
              if (lastVisibleItemPosition == itemCount - 1) {
                // 到达底部，可以加载更多数据
              } else if (lastVisibleItemPosition == 0) {
                // 到达顶部，可以执行一些操作
              }
//              if (!isAutoPlay) {
//                startAutoPlay();
//              }
              break;

            case RecyclerView.SCROLL_STATE_DRAGGING: // 当前滚动状态为正在拖拽状态
              // 停止自动滚动
              if (autoScrollHandler != null) {
                autoScrollHandler.removeCallbacksAndMessages(null);
              }
              // 暂停图片加载
              Picasso.get().pauseTag(this);
              break;
            case RecyclerView.SCROLL_STATE_SETTLING: // 当前滚动状态为正在滑行状态
              if (isAutoPlay) {
                stopAutoPlay();
              }
              break;
          }
        }*/
      });

// 给 RecyclerView 添加触摸事件监听器
      s2t_recyclerView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          // 判断是否点击了滚动条
          if (event.getAction() == MotionEvent.ACTION_DOWN && event.getX() >= s2t_recyclerView.getWidth() - 100) {
            // 点击滚动条，计算当前位置的内容位置,100是scrollbarWidth，但是不知道是什么单位
            float scrollBarPos = event.getY();
            int itemCount = adapter.getItemCount();
            int contentPos = (int) ((scrollBarPos / s2t_recyclerView.getHeight()) * itemCount);
            s2t_recyclerView.scrollToPosition(contentPos);
            return true;
          }
          return false;
        }
      });

      //  album.findViewById(R.id.album);
      //  album.setOnClickListener(this);

    }

    //@Override
    //public void onClick(View view) {
       // switch (view.getId()){
         //   case R.id.album:

       // }
   // }

 /* private void stopAutoPlay() {
    isAutoPlay = false;
    autoScrollHandler.removeCallbacks(autoScrollRunnable);
  }
  Runnable autoScrollRunnable = new Runnable() {
    @Override
    public void run() {
      int nextItem = s2t_recyclerView.getItemDecorationCount() + 1;
      if (nextItem >= adapter.getItemCount()) {
        nextItem = 0;
      }
      s2t_recyclerView.setItemViewCacheSize(nextItem);
      autoScrollHandler.postDelayed(this, 2000);
    }
  };
  private void startAutoPlay() {
    isAutoPlay = true;
    autoScrollHandler.postDelayed(autoScrollRunnable, 2000);
  }*/
}
