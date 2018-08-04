package com.pxy.pangjiao.logger;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.pxy.pangjiao.logger.Utils.checkNotNull;

/**
 * Created by Administrator on 2018/6/21.
 */

public class DiskLogStage implements LogStage {

    private static final int JSON_INDENT = 2;
    private int maxFileSize = 20;
    private String folder;

    public DiskLogStage(int maxFileSize, String folder) {
        this.maxFileSize = maxFileSize;
        this.folder = folder;
    }

    @Override
    public void log(int level, String tag, String msg) {
        checkNotNull(msg);
        write(msg, level, tag);
    }

    @Override
    public void logJson(String json, String tag) {
        if (Utils.isEmpty(json)) {
            log(Logger.DEBUG, tag, "Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                write(message, Logger.DEBUG, tag);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                write(message, Logger.DEBUG, tag);
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
            write(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"), Logger.DEBUG, tag);
        } catch (TransformerException e) {
            log(Logger.ERROR, tag, "Invalid logXml");
        }
    }

    public void write(String content, int level, String tag) {
        FileWriter fileWriter = null;
        File logFile = getLogFile(folder, TimeFormat.getTimeEndDay() + "_" + Utils.logLevel(level) + "_logs");

        try {
            fileWriter = new FileWriter(logFile, true);

            writeLog(fileWriter, tag + "\r\n" + content);

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e1) { /* fail silently */
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
        checkNotNull(fileWriter);
        checkNotNull(content);
        fileWriter.append(content);
    }

    private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
        checkNotNull(folderName);
        checkNotNull(fileName);

        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int newFileCount = 0;
        File newFile;
        File existingFile = null;

        newFile = new File(folder, String.format("%s_%s.text", fileName, newFileCount));
        while (newFile.exists()) {
            existingFile = newFile;
            newFileCount++;
            newFile = new File(folder, String.format("%s_%s.text", fileName, newFileCount));
        }

        if (existingFile != null) {
            if (existingFile.length() >= maxFileSize) {
                return newFile;
            }
            return existingFile;
        }

        return newFile;
    }
}
