package com.example.slt_project.ui.S2T;

import android.annotation.SuppressLint;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.example.slt_project.ui.TextOutputAdapter;

import java.net.MalformedURLException;
import java.util.ArrayList;

public interface S2TContract {
    interface IS2TFragment{

        void createPreview();
        Surface getPreviewSurface();
        FragmentActivity getMainActivity();
        ImageView getThumbnail();

        TextureView getTextureView();

        void showThumbnail();

        TextOutputAdapter getAdapter();
    }

    interface IS2TPresenter{
        void takePhoto();
        void checkPermission();

        boolean allPermissionsGranted();

        @SuppressLint("MissingPermission")
        void openCamera();

        void createSection() throws CameraAccessException;

        void goGallery();

        void initImageReader();

        void broadcast();


        void broadcast(String path);

        void broadcastPhoto(String path);

        void broadcastVideo(String path);

        CameraDevice getCameraDevice();

        CameraCaptureSession getCameraCaptureSession();

        IS2TFragment getFragment();

        Handler getCameraHandler();
        CaptureRequest.Builder getCaptureRequestBuilder();

        ArrayList<String> getImageFilePath();


        void showThumbnail();

        void takeVideo();

        void stopVideo() throws MalformedURLException;

        void endVideo();

        void configMediaRecorder();

        void changeCamera();

        void speak(String s);

        void translateTo(String content);
    }

    interface IS2TModel{
        void translateTo(String content, String val);

        void takePhoto();

        void initImageReader();

        ImageReader getImageReader();

        void speak(String s);

        IS2TPresenter getPresenter();
    }

}
