package com.example.slt_project.ui.S2T;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.slt_project.ui.TextOutputAdapter;

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

        void createSection() throws CameraAccessException;

        void goGallery();

        void initImageReader();

        void broadcast();


        CameraDevice getCameraDevice();

        CameraCaptureSession getCameraCaptureSession();

        IS2TFragment getFragment();

        Handler getCameraHandler();
        CaptureRequest.Builder getCaptureRequestBuilder();

        ArrayList<String> getImageFilePath();


        void showThumbnail();

        void takeVideo();

        void stopVideo();

        void configMediaRecorder();

        void changeCamera();
    }

    interface IS2TModel{
        void takePhoto();

        void initImageReader();

        ImageReader getImageReader();
    }

}
