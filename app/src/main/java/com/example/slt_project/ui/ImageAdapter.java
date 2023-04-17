package com.example.slt_project.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.slt_project.R;

import java.util.List;

// ImageAdapter.java
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<String> imageUrls; // 图片 URL 集合

    // 构造方法，传入图片 URL 集合
    public ImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    // 创建 ViewHolder
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.t2s_image_item, parent, false);
        return new ImageViewHolder(itemView);
    }

    // 绑定数据到 ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        // 使用 Glide 加载图片到 ImageView
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imageView);
    }

    // 获取数据项的数量
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    // 定义 ViewHolder
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.t2s_imageView);
        }
    }
}
