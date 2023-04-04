/*
package com.example.slt_project.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public DividerItemDecoration(Context context) {
        divider = ContextCompat.getDrawable(context, R.drawable.divider); // divider是你自己定义的drawable资源
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int dividerHeight = divider.getIntrinsicHeight();

        int childCount = parent.getChildCount();
        int width = parent.getWidth();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + dividerHeight;

            divider.setBounds(0, top, width, bottom);
            divider.draw(c);
        }
    }
}

*/
//没用的东西