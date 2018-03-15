package com.pxy.pangjiao.net;

import android.accounts.NetworkErrorException;

import com.pxy.pangjiao.common.ExpUtil;
import com.pxy.pangjiao.PangJiao;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by pxy on 2018/2/1.
 */

public class HttpEngine {


    public static final File file = new File("");


    public static String post(String url, String content, NetDefaultConfig config) {
        HttpURLConnection conn = null;
        PangJiao.info("请求--POST:API:" + url + "\r\n" + content);
        URL mURL = null;
        try {
            mURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            PangJiao.error("" + e);
        }
        try {
            conn = (HttpURLConnection) mURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        }
        try {
            conn.setRequestMethod("POST");// 设置请求方法为post
        } catch (ProtocolException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        }
        conn.setConnectTimeout(config.getConnectTimeOut());// 设置连接网络超时为10秒
        conn.setReadTimeout(config.getReadTimeOut());// 设置读取超时为5秒
        conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容
        conn.setRequestProperty("Content-Type", "application/json");
        try {
            conn.connect();
            OutputStream out = conn.getOutputStream();  // 获得一个输出流,向服务器写数据
            out.write(content.getBytes());
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                PangJiao.info("回复--POST:" + response);
                return response;
            } else {
                throw new NetworkErrorException("response status is " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        } catch (NetworkErrorException e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        } finally {
            conn.disconnect();// 关闭连接
        }
        return null;
    }

    public static String get(String url, NetDefaultConfig config) {
        HttpURLConnection conn = null;
        try {
            // 利用string url构建URL对象
            PangJiao.info("请求--GET:API" + url);
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(config.getReadTimeOut());
            conn.setConnectTimeout(config.getConnectTimeOut());
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                PangJiao.info("回复--GET:" + response);
                return response;
            } else {
                throw new NetworkErrorException("response status is " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            PangJiao.error(ExpUtil.getStackTrace(e));
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static void uploadFile(File uploadFile, String updateName, String api) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            /* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + updateName + "\"" + end);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8
        os.close();
        return state;
    }
}
