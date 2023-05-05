package com.example.slt_project.ui.S2T;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slt_project.R;
import com.example.slt_project.ui.TextOutputAdapter;
import com.example.slt_project.ui.base.BaseFragment;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class S2TFragment extends BaseFragment implements S2TContract.IS2TFragment, View.OnClickListener, TextureView.SurfaceTextureListener {
    private TextureView textureView;
    private RecyclerView s2t_recyclerView;
    private TextOutputAdapter adapter;
    List<String> textList = new ArrayList<>();
    private Button takePhotoORVideo, changeCamera, playSound;
    public S2TContract.IS2TPresenter presenter;
    public Surface previewSurface;
    public SurfaceTexture surfaceTexture;
    private boolean bStop = false;

    protected ImageView mThumbnail;

    SharedPreferences sp;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_s2t;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initViews() {
        presenter = new S2TPresenter(this);

        takePhotoORVideo = find(R.id.s2t_take_photo_or_video);
        textureView = find(R.id.previewSurfaceView);
        changeCamera = find(R.id.s2t_changeCamera);
        mThumbnail = find(R.id.s2t_thumbnail);
        s2t_recyclerView = find(R.id.s2t_recycle);

        textureView.setSurfaceTextureListener(this);
        takePhotoORVideo.setOnClickListener(this);
        changeCamera.setOnClickListener(this);
        mThumbnail.setOnClickListener(this);

        Context ctx = this.getMainActivity();
        sp = ctx.getSharedPreferences("my_prefs", MODE_PRIVATE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        s2t_recyclerView.setLayoutManager(layoutManager);
        adapter = new TextOutputAdapter(textList, presenter);
        s2t_recyclerView.setAdapter(adapter);




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
        });

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
//        // 定义变量
//        final long[] startTime = {0};
//        long durationThreshold = 1000; // 长按时间阈值，单位为毫秒
//
//// 设置触摸监听器
//        takePhotoORVideo.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startTime[0] = System.currentTimeMillis();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        long duration = System.currentTimeMillis() - startTime[0];
//                        if (duration > durationThreshold) {
//                            // 进入录制模式
//                            presenter.takeVideo();
//                        } else {
//                            // 进入拍照模式
//                            presenter.takePhoto();
//                        }
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.s2t_take_photo_or_video:
                String t = sp.getString("videoOrPhoto", "video");
                if (!sp.getBoolean("photo_mode", false)) {
                    if (!bStop) {
                        presenter.takeVideo();
                        bStop = true;
                    } else {
                        try {
                            presenter.stopVideo();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        bStop = false;
                    }
                } else {
                    presenter.takePhoto();
                }

                break;
            case R.id.s2t_thumbnail:
                presenter.goGallery();
                break;

            case R.id.s2t_changeCamera:
                presenter.changeCamera();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //presenter.checkPermission();
//        createPreview();



    }

    @Override
    public void onStop() {
        if(bStop){
            presenter.endVideo();
            bStop = false;
        }
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            presenter.checkPermission();
            presenter.openCamera();
        }

    }



    @Override
    public void createPreview() {
        presenter.initImageReader();
        surfaceTexture = textureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(1920, 1080);
        previewSurface = new Surface(surfaceTexture);
        presenter.configMediaRecorder();
        try {
            presenter.createSection();
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        showThumbnail();

    }

    @Override
    public Surface getPreviewSurface() {
        return previewSurface;
    }


    @Override
    public ImageView getThumbnail() {
        return mThumbnail;
    }

    @Override
    public TextureView getTextureView() {
        return textureView;
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        presenter.checkPermission();
        showThumbnail();
        this.createPreview();

    }

    protected static final SparseIntArray ORIENTATION = new SparseIntArray();

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    public void showThumbnail() {
        ArrayList<String> imageList = presenter.getImageFilePath();
        String path = "";
        for (int i = 1; i <= imageList.size(); i++) {
            path = imageList.get(imageList.size() - i);
            long size = 0;
            try {
                size = new FileInputStream(path).available();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (size > 0) break;
        }

        if (path.contains("jpg")) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mThumbnail.setImageBitmap(toRoundBitmap(bitmap));
//            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//            mThumbnail.setRotation(ORIENTATION.get(rotation));
        } else if (path.contains("mp4")) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            Bitmap bitmap = retriever.getFrameAtTime(1);
            mThumbnail.setImageBitmap(toRoundBitmap(bitmap));
        }
    }


    @Override
    public TextOutputAdapter getAdapter() {
        return adapter;
    }


    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }
    private static Bitmap toRoundBitmap(Bitmap bitmap) {//bitmap图片
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src;
        src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }


}
