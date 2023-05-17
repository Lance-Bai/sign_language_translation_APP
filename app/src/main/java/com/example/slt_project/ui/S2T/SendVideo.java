package com.example.slt_project.ui.S2T;

import android.os.AsyncTask;
import android.util.Log;

import com.example.slt_project.ui.SendAble;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public class SendVideo extends AsyncTask<File,Void,String> {
    S2TContract.IS2TPresenter PRESENTER;

    public SendVideo(S2TContract.IS2TPresenter PRESENTER){this.PRESENTER=  PRESENTER;}



    @Override
    protected String doInBackground(File... files) {
        String post_result = null;
        try {
            post_result = submitPostData(files[0], "http://192.168.137.47:8000/upload");
            Log.i("POST_RESULT", post_result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return post_result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("network", "video send");
        PRESENTER.getFragment().getAdapter().addText(s);
    }




    public static String submitPostData(File file, String serverUrl) throws IOException {
        String boundary = "---------------------------" + System.currentTimeMillis();
        String end = "\r\n";

        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(serverUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(600 * 1000);
            httpURLConnection.setConnectTimeout(600 * 1000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // 添加文件部分
            writer.append("--").append(boundary).append(end);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append(end);
            writer.append("Content-Type: video/mp4").append(end);
            writer.append(end).flush();

            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            fileInputStream.close();

            writer.append(end).flush();
            writer.append("--").append(boundary).append("--").append(end).flush();

            int response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                return dealResponseResult(inputStream); // 处理服务器响应结果
            } else {
                return "Error: " + response;
            }
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    private static String dealResponseResult(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(data)) != -1) {
            byteArrayOutputStream.write(data, 0, len);
        }
        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }

}
