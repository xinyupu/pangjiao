package com.pxy.pangjiao.net;

import android.accounts.NetworkErrorException;

import com.pxy.pangjiao.common.ExpUtil;
import com.pxy.pangjiao.PangJiao;
import com.pxy.pangjiao.mvp.MVPCore;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pxy on 2018/2/1.
 */

public class HttpEngine {


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
            MVPCore.getInstance().postMain(() -> {
                if (NetCore.getHttpManger().getListener() != null) {
                    NetCore.getHttpManger().getListener().onError(e);
                }
            });
            PangJiao.error(ExpUtil.getStackTrace(e));
        } catch (NetworkErrorException e) {
            e.printStackTrace();
            MVPCore.getInstance().postMain(() -> {
                if (NetCore.getHttpManger().getListener() != null) {
                    NetCore.getHttpManger().getListener().onError(e);
                }
            });
            PangJiao.error(ExpUtil.getStackTrace(e));
        } finally {
            conn.disconnect();// 关闭连接
        }
        return null;
    }

    public static String get(String url, NetDefaultConfig config) {
        HttpURLConnection conn = null;
        try {
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
            MVPCore.getInstance().postMain(() -> {
                if (NetCore.getHttpManger().getListener() != null) {
                    NetCore.getHttpManger().getListener().onError(e);
                }
            });
            PangJiao.error(ExpUtil.getStackTrace(e));
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public void upLodeImage(String path, String fileName, byte[] files) {
        HttpURLConnection conn = null;
        Map<String, byte[]> fileMap = new HashMap<>();
        fileMap.put("upFile", files);
        //分隔符
        String finalSplit = "---------------------------DmzWebApi";
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + finalSplit);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            if (fileMap.size() > 0) {
                Iterator<Map.Entry<String, byte[]>> inter = fileMap.entrySet().iterator();
                while (inter.hasNext()) {
                    Map.Entry<String, byte[]> entry = inter.next();
                    String inputName = entry.getKey();
                    byte[] inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    String contentType = "image/jpeg";
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(finalSplit).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"; filename=\"").append(fileName + ".jpg").append("\"\r\n");
                    strBuf.append("Content-Type:").append(contentType).append("\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new ByteArrayInputStream(inputValue));
                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + finalSplit + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
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
