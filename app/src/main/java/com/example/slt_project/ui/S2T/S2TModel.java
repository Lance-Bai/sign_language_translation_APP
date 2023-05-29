package com.example.slt_project.ui.S2T;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
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

import com.huaweicloud.sdk.nlp.v2.model.RunTextTranslationRequest;
import com.huaweicloud.sdk.nlp.v2.model.TextTranslationReq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

public class S2TModel implements S2TContract.IS2TModel, ImageReader.OnImageAvailableListener, TextToSpeech.OnInitListener {
    private ImageReader imageReader = null;
    private S2TContract.IS2TPresenter presenter;

    private TextToSpeech textToSpeech;

    S2TModel(S2TContract.IS2TPresenter presenter){
        this.presenter = presenter;
        textToSpeech = new TextToSpeech(presenter.getFragment().getMainActivity(), this);

    }
    @Override
    public void translateTo(String content, String val) {
        TextTranslationReq req1 = new TextTranslationReq();
        req1.setFrom(TextTranslationReq.FromEnum.ZH);
        req1.setTo(TextTranslationReq.ToEnum.fromValue(val));
        req1.setText(content);
        req1.setScene(TextTranslationReq.SceneEnum.COMMON);
        RunTextTranslationRequest req2 = new RunTextTranslationRequest();
        req2.setBody(req1);
        new TranslateModel(this).execute(req2);

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

            if(presenter.getCameraDevice().getId().equals(String.valueOf(CameraCharacteristics.LENS_FACING_BACK))){
                rotation= (rotation+2)%4;
            }
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
        imageReader = ImageReader.newInstance(1080, 1080, ImageFormat.JPEG, 1);
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
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                String path = presenter.getFragment().getMainActivity().getExternalFilesDir(null) +
                        "/"
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
                    presenter.getFragment().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SendPhoto(presenter).execute(imageFile);
                        }
                    });
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

    @Override
    public S2TContract.IS2TPresenter getPresenter(){
        return presenter;
    }

}
