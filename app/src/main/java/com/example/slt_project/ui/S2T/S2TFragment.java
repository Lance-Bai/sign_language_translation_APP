package com.example.slt_project.ui.S2T;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.slt_project.R;
import com.example.slt_project.ui.base.BaseFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class S2TFragment extends BaseFragment implements S2TContract.IS2TFragment, View.OnClickListener, TextureView.SurfaceTextureListener {
    private TextureView textureView;
    private Button takePhotoORVideo, changeCamera;
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

    @Override
    protected void initViews() {
        presenter = new S2TPresenter(this);
        textureView = find(R.id.previewSurfaceView);
        textureView.setSurfaceTextureListener(this);
        takePhotoORVideo = find(R.id.s2t_take_photo_or_video);
        takePhotoORVideo.setOnClickListener(this);

        changeCamera = find(R.id.s2t_changeCamera);
        changeCamera.setOnClickListener(this);
        mThumbnail = find(R.id.s2t_thumbnail);
        mThumbnail.setOnClickListener(this);

        Context ctx = this.getMainActivity();
        sp = ctx.getSharedPreferences("SLTSetting", MODE_PRIVATE);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.s2t_take_photo_or_video:
                String t = sp.getString("videoOrPhoto", "video");
                if(true){
                    if(!bStop){
                        presenter.takeVideo();
                        bStop = true;
                    }else{
                        presenter.stopVideo();
                        bStop = false;
                    }
                }else{
                    presenter.takePhoto();
                }

                break;
            case R.id.s2t_thumbnail:
                presenter.goGallery();
                break;

            case R.id.s2t_changeCamera:
                presenter.changeCamera();
            default:
                break;
        }
    }

    @Override
    public void onResume() {
//        super.onResume();
//        presenter.checkPermission();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            presenter.checkPermission();
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
        }catch (CameraAccessException e) {
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
        String path="";
        for(int i=1;i<imageList.size();i++){
            path = imageList.get(imageList.size() - i);
            long size = 0;
            try {
                size = new FileInputStream(path).available();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(size>0)break;
        }

        if (path.contains("jpg")) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mThumbnail.setImageBitmap(bitmap);
//            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
//            mThumbnail.setRotation(ORIENTATION.get(rotation));
        } else if(path.contains("mp4")){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            Bitmap bitmap = retriever.getFrameAtTime(1);
            mThumbnail.setImageBitmap(bitmap);
        }
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
}
