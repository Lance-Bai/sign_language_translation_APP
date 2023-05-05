package com.example.slt_project.ui.S2T;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Map;

//wyt add
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//add complete


public class S2TModel implements S2TContract.IS2TModel, ImageReader.OnImageAvailableListener, TextToSpeech.OnInitListener {
    private ImageReader imageReader = null;
    private S2TContract.IS2TPresenter presenter;

    private TextToSpeech textToSpeech;

    S2TModel(S2TContract.IS2TPresenter presenter){
        this.presenter = presenter;
        textToSpeech = new TextToSpeech(presenter.getFragment().getMainActivity(), this);

    }

    protected static final SparseIntArray ORIENTATION = new SparseIntArray();

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }



    @Override
    public void takePhoto() {
        Log.d(null, "Taking a photo");
        try {
            CaptureRequest.Builder captureBuilder = presenter.getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            int rotation = presenter.getFragment().getMainActivity().getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(rotation));
            presenter.getCameraCaptureSession().stopRepeating();

            presenter.getCameraCaptureSession().capture(captureBuilder.build(), captureCallback, presenter.getCameraHandler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
            try {
                session.setRepeatingRequest(presenter.getCaptureRequestBuilder().build(), null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void initImageReader() {
        Log.d(null,"init image reader");
        imageReader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(this, null);


    }

    @Override
    public ImageReader getImageReader() {
        return imageReader;
    }

    @Override
    public void speak(String s) {
        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.setPitch(1.0f);
            textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        Log.d(null,"photo saved");
        Image image = imageReader.acquireNextImage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //字节缓冲
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                String path = Environment.getExternalStorageDirectory() +  "/DCIM/Camera/"
                        + System.currentTimeMillis() + ".jpg";
                File imageFile = new File(path);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(imageFile);
                    fos.write(data, 0, data.length);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    presenter.broadcast();
                    image.close(); // MUST!!!!!
                    presenter.showThumbnail();
                }
            }
        }).start();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("voice","data lost or not available");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


    //wyt add


}


