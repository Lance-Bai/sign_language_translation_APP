package com.example.slt_project.ui.S2T;

import android.os.AsyncTask;
import android.util.Log;

import com.example.slt_project.ui.SendAble;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class PostData extends AsyncTask<Map<String, String>,Void,String> {
    SendAble PRESENTER;

    public PostData(SendAble PRESENTER){this.PRESENTER=  PRESENTER;}



    @Override
    protected String doInBackground(Map<String, String>... hashMaps) {
        String post_result = null;
        try {
            post_result = submitPostData(hashMaps[0], "utf-8");
            Log.i("POST_RESULT", post_result);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return post_result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PRESENTER.translateTo(s);
    }

    public static String submitPostData(Map<String, String> params, String encode) throws MalformedURLException {
        /**
         * 发送POST请求到服务器并返回服务器信息
         * @param params 请求体内容
         * @param encode 编码格式
         * @return 服务器返回信息
         */
        byte[] data = getRequestData(params, encode).toString().getBytes();
        URL url = new URL("http://10.19.35.46:5000");
        HttpURLConnection httpURLConnection = null;
        try{
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);  // 设置连接超时时间
            httpURLConnection.setDoInput(true);         // 打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);        // 打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); // 设置以POST方式提交数据
            httpURLConnection.setUseCaches(false);      // 使用POST方式不能使用缓存
            // 设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 获得输入流，向服务器写入数据
            OutputStream outputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
            outputStream.write(data);
            outputStream.flush();                       // 重要！flush()之后才会写入

            int response = httpURLConnection.getResponseCode();     // 获得服务器响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                return dealResponseResult(inputStream);             // 处理服务器响应结果
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return "";

    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();            //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);   // 删除最后一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }


}
