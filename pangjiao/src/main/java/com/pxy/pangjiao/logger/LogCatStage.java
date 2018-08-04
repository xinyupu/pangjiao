package com.pxy.pangjiao.logger;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Administrator on 2018/6/21.
 */

public class LogCatStage implements LogStage {
    private static final int JSON_INDENT = 2;

    @Override
    public void log(int level, String tag, String msg) {
        switch (level) {
            case Logger.VERBOSE:
                Log.v(tag, msg);
                break;
            case Logger.DEBUG:
                Log.d(tag, msg);
                break;
            case Logger.INFO:
                Log.i(tag, msg);
                break;
            case Logger.WARN:
                Log.w(tag, msg);
            case Logger.ERROR:
                Log.e(tag, msg);
            case Logger.ASSERT:
                Log.e(tag, msg);
            default:
                break;
        }
    }

    @Override
    public void logJson(@Nullable String json, String tag) {
        if (Utils.isEmpty(json)) {
            log(Logger.DEBUG, tag, "Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                log(Logger.DEBUG, tag, message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                log(Logger.DEBUG, tag, message);
                return;
            }
            log(Logger.ERROR, tag, "Invalid Json");
        } catch (JSONException e) {
            log(Logger.ERROR, tag, "Invalid Json");
        }
    }

    @Override
    public void logXml(@Nullable String xml, String tag) {
        if (Utils.isEmpty(xml)) {
            log(Logger.DEBUG, tag, "Empty/Null logXml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://logXml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            log(Logger.DEBUG, tag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            log(Logger.ERROR, tag, "Invalid logXml");
        }
    }
}
