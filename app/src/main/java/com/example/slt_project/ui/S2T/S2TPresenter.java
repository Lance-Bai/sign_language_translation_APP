package com.example.slt_project.ui.S2T;

import static android.content.Context.CAMERA_SERVICE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class S2TPresenter implements S2TContract.IS2TPresenter {
    private S2TContract.IS2TFragment fragment;
    private S2TContract.IS2TModel model;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",

    };

    private Handler mCameraHandler;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private String cameraId = "0";
    private MediaRecorder mMediaRecorder;
    private long timestemp;

    private File SLTVideo;



    public S2TPresenter(S2TContract.IS2TFragment fragment){
        this.fragment = fragment;
        this.model= new S2TModel(this);
        startCameraThread();
    }

    @Override
    public void takePhoto() {
        model.takePhoto();
    }

    @Override
    public void checkPermission() {
        if (allPermissionsGranted()) {
            Log.d(null, "Permission Granted");
            openCamera();

        } else {
            Log.d(null, "Apply Permission");
            ((Fragment)fragment).requestPermissions(REQUIRED_PERMISSIONS, 1);
        }



    }

    public boolean allPermissionsGranted(){
        Log.d(null, "----- Check Permission");
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(fragment.getMainActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d(null,permission+" not granted");
                return false;
            }
        }
        return true;
    }

    protected void startCameraThread() {
        HandlerThread mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    @SuppressLint("MissingPermission")
    public void openCamera(){
        Log.d(null, "----------openCamera");
        CameraManager cameraManager = (CameraManager) fragment.getMainActivity().getSystemService(CAMERA_SERVICE);
        try{
            String[] cameraID = cameraManager.getCameraIdList();
            for (String s:cameraID){
                Log.d(null, "--------- cameraId = " + s);
            }
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(ImageFormat.JPEG);
            for (Size size : sizes) {
                Log.d(null,"size = "+size);
            }

            cameraManager.openCamera(valueOf(cameraId),cameraDeviceStateCallback,mCameraHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    CameraDevice.StateCallback cameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {

            mCameraDevice = cameraDevice;


        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {

        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {

        }
    };

    public void createSection() throws CameraAccessException {

        mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mCaptureRequestBuilder.addTarget(fragment.getPreviewSurface());
        mCaptureRequestBuilder.addTarget(mMediaRecorder.getSurface());
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        mCameraDevice.createCaptureSession(Arrays.asList(fragment.getPreviewSurface(), model.getImageReader().getSurface(), mMediaRecorder.getSurface()), cameraCaptureSessionStateCallback, mCameraHandler);
    }

    @Override
    public void goGallery() {
        ArrayList<String> temp = getImageFilePath();
        String lastPath = temp.get(0);
        Uri uri = getMediaUriFromPath(this, lastPath);
        Intent intent = new Intent("com.android.camera.action.REVIEW", uri);
        intent.setData(uri);
        fragment.getMainActivity().startActivity(intent);
    }

    @SuppressLint("Range")
    private Uri getMediaUriFromPath(S2TPresenter s2TPresenter, String path) {
        Uri uri = null;
        if (path.contains("jpg")) {
            Uri picUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = fragment.getMainActivity().getContentResolver().query(picUri,
                    null,
                    MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                    new String[]{path.substring(path.lastIndexOf("/") + 1)},
                    null);
            if (cursor.moveToFirst()) {
                uri = ContentUris.withAppendedId(picUri,
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
            }
            cursor.close();
        } else if (path.contains("mp4")) {
            Uri mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor =  fragment.getMainActivity().getContentResolver().query(mediaUri,
                    null,
                    MediaStore.Video.Media.DISPLAY_NAME + "= ?",
                    new String[]{path.substring(path.lastIndexOf("/") + 1)},
                    null);
            if (cursor.moveToFirst()) {
                uri = ContentUris.withAppendedId(mediaUri,
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
            }
            cursor.close();
        }
        return uri;

    }

    @Override
    public void initImageReader() {
        model.initImageReader();
    }

    @Override
    public void broadcast() {
        Log.d(null, "--------------------broadcast");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +  "/DCIM/Camera";
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        fragment.getMainActivity().sendBroadcast(intent);
    }




    @Override
    public CameraDevice getCameraDevice() {
        return mCameraDevice;
    }

    @Override
    public CameraCaptureSession getCameraCaptureSession() {
        return mCameraCaptureSession;
    }

    @Override
    public S2TContract.IS2TFragment getFragment() {
        return fragment;
    }

    @Override
    public Handler getCameraHandler() {
        return mCameraHandler;
    }

    @Override
    public CaptureRequest.Builder getCaptureRequestBuilder() {
        return mCaptureRequestBuilder;
    }

    @Override
    public void showThumbnail() {
        fragment.showThumbnail();
    }

    @Override
    public void takeVideo() {

        Log.d(null, "takeVideo: start");
        mMediaRecorder.start();
    }

    @Override
    public void stopVideo() {
        Log.d(null, "stopVideo: stop");
        mMediaRecorder.setOnErrorListener(null);
        mMediaRecorder.setOnInfoListener(null);
        mMediaRecorder.setPreviewDisplay(null);
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder=null;


        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SLTVideo.renameTo(new File(Environment.getExternalStorageDirectory() +
                "/DCIM/Camera/SLTV_" + System.currentTimeMillis() + ".mp4"));
        broadcast();
        fragment.createPreview();
        // TODO: 2023-04-04 connect with deep learning module 
        fragment.getAdapter().addText("你好");
        model.speak("你好");


    }

    public void configMediaRecorder() {
//
//        File file = new File(Environment.getExternalStorageDirectory() +
//                "/DCIM/Camera/" + timestemp + ".mp4");
        SLTVideo = new File(Environment.getExternalStorageDirectory() +
                "/DCIM/Camera/.SLTVideoCache.mp4");
        if (SLTVideo.exists()) {
            SLTVideo.delete();
        }
        if (mMediaRecorder==null) {
            mMediaRecorder = new MediaRecorder();
            if (cameraId.equals("1")) {
                mMediaRecorder.setOrientationHint(90);
            } else {
                mMediaRecorder.setOrientationHint(90);
            }
            //audio source
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //video source
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            //output format
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //Audio coding format, using default AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //video coding format, using H264
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //Bit rate
            mMediaRecorder.setVideoEncodingBitRate(10 * 1080 * 1920);
            //fragment rate
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoSize(1920, 1080);
            Surface surface = new Surface(fragment.getTextureView().getSurfaceTexture());
            mMediaRecorder.setPreviewDisplay(surface);
            mMediaRecorder.setOutputFile(SLTVideo.getAbsolutePath());
            Log.d(null,"configure video taker");

            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                Log.e(null, "prepare() failed");
            }
        }

    }


    CameraCaptureSession.StateCallback cameraCaptureSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            mCameraCaptureSession = cameraCaptureSession;
            try {
                cameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

        }
    };
    public ArrayList<String> getImageFilePath() {
        ArrayList<String> imageList = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
        File[] dirEpub = file.listFiles();
        Arrays.sort(dirEpub,new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;
            }

            public boolean equals(Object obj) {
                return true;
            }
        });

        if (dirEpub.length != 0) {
            for (int i = 0; i < dirEpub.length; i++) {
                String fileName = dirEpub[i].toString();
                if(!fileName.contains(".jpg")&&!fileName.contains(".mp4"))continue;
                imageList.add(fileName);
                Log.i("File", "File name = " + fileName);
            }
        }
        return imageList;
    }

    public void changeCamera() {
        Log.d(null, "changeCamera: success");
        if (cameraId.equals(String.valueOf(CameraCharacteristics.LENS_FACING_BACK))) {
            Toast.makeText(fragment.getMainActivity(), "前置转后置", Toast.LENGTH_SHORT).show();
            cameraId = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
        } else {
            Toast.makeText(fragment.getMainActivity(), "后置转前置", Toast.LENGTH_SHORT).show();
            cameraId = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);
        }
        mCameraDevice.close();
        openCamera();
        fragment.createPreview();

    }




}
