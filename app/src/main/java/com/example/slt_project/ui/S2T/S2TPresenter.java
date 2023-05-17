package com.example.slt_project.ui.S2T;

import static android.content.Context.CAMERA_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.slt_project.ui.SendAble;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class S2TPresenter implements S2TContract.IS2TPresenter, SendAble {
    private S2TContract.IS2TFragment fragment;
    private S2TContract.IS2TModel model;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET",
            "android.permission.FOREGROUND_SERVICE",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE"

    };

    private Handler mCameraHandler;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private String cameraId = "0";
    private MediaRecorder mMediaRecorder;

    private File SLTVideo;

    private SharedPreferences sp;




    public S2TPresenter(S2TContract.IS2TFragment fragment){
        this.fragment = fragment;
        this.model= new S2TModel(this);
        startCameraThread();
        sp = this.getFragment().getMainActivity().getSharedPreferences("my_prefs", MODE_PRIVATE);
    }

    @Override
    public void takePhoto() {
        model.takePhoto();
    }

    @Override
    public void checkPermission() {
        if (allPermissionsGranted()) {
            openCamera();
            Log.d(null, "Permission Granted");
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
            Log.d(null, permission + " granted");
        }
        return true;
    }

    protected void startCameraThread() {
        HandlerThread mCameraThread = new HandlerThread("CameraThread");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
    }

    @SuppressLint("MissingPermission")
    @Override
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
        String lastPath = temp.get(temp.size()-1);

        Uri uri = FileProvider.getUriForFile(this.getFragment().getMainActivity(), "com.example.slt_project.fileprovider", new File(lastPath));
        String type="";
        if(lastPath.contains(".mp4"))type="video/*";
        if(lastPath.contains(".jpg"))type="image/*";

        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(uri, type);
        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
        fragment.getMainActivity().startActivity(it);


    }



    @Override
    public void initImageReader() {
        model.initImageReader();
    }

    @Override
    public void broadcast() {

    }

    @Override
    public void broadcast(String path) {

    }

    @Override
    public void broadcastPhoto(String path) {
        Log.d(null, "--------------------broadcast the photo");
        AlbumHelper AlbumNotifyHelper = new AlbumHelper();
        AlbumNotifyHelper.insertImageToMediaStore(this.getFragment().getMainActivity(), path, 0);
    }


    @Override
    public void broadcastVideo(String path) {
        Log.d(null, "--------------------broadcast the video");
        AlbumHelper AlbumNotifyHelper = new AlbumHelper();
        AlbumNotifyHelper.insertVideoToMediaStore(this.getFragment().getMainActivity(), path, 0, 5000);


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
    public void stopVideo() throws MalformedURLException {
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
        String newPath = this.getFragment().getMainActivity().getExternalFilesDir(null) +
                "/SLTV_" + System.currentTimeMillis() + ".mp4";
        SLTVideo.renameTo(new File(newPath));
        broadcastVideo(newPath);
        AlbumHelper.insertVideoToMediaStore(this.getFragment().getMainActivity(), newPath, 0, 5000);
        fragment.createPreview();
        // TODO: 2023-04-04 connect with deep learning module

        // wyt add
        Map<String, String> params = new HashMap<String, String>();

        params.put("uid", "001");
        params.put("name", "g2ex");
//        new PostData(this).execute(params);
//
        new SendVideo(this).execute(new File(newPath));





        //add complete

//        fragment.getAdapter().addText("你好");
//        model.speak("你好");
//        translateTo("你好");

    }

    @Override
    public void endVideo(){
        //without saving or translating it
        Log.d(null, "stopVideo: end");
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
    }



    public void configMediaRecorder() {
//
//        File file = new File(Environment.getExternalStorageDirectory() +
//                "/DCIM/Camera/" + timestemp + ".mp4");
        SLTVideo = new File(this.getFragment().getMainActivity().getExternalFilesDir(null) +
                "/.SLTVideoCache");
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
            Log.d("audio","check done");
            //audio source
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            //video source
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            //output format
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //Audio coding format, using default AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            //video coding format, using H264
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //Bit rate
            mMediaRecorder.setVideoEncodingBitRate(30 * 1080 * 1080);
            //fragment rate
            mMediaRecorder.setVideoFrameRate(30);
            mMediaRecorder.setVideoSize(1080, 1080);
            Surface surface = new Surface(fragment.getTextureView().getSurfaceTexture());
            mMediaRecorder.setPreviewDisplay(surface);
            mMediaRecorder.setOutputFile(SLTVideo.getAbsolutePath());
            Log.d(null,"configure video taker");

            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                Log.e(null, "prepare() failed"+ e);
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
        File file = new File(String.valueOf(this.getFragment().getMainActivity().getExternalFilesDir(null)));
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
                //Log.i("File", "File name = " + fileName);
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

    @Override
    public void speak(String s) {
        model.speak(s);
    }

    @Override
    public void translateTo(String content){
       String targetLanguage =  sp.getString("language","zh");
       if(targetLanguage.equals("zh")){
           this.getFragment().getAdapter().addText(content);
           model.speak(content);
       }else{
           model.translateTo(content,targetLanguage);
       }

    }
}
