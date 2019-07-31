package com.example.shan.mqtt;

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class ErrorLog {
    private FileHelper fileHelper = null;
    Vector<String> fileList = new Vector<>();
//    long lastModifyTime = 0;

    private static ErrorLog instance = null;
    private ErrorLog (Context context){
        fileHelper = new FileHelper(context);
        initFileList();
    }
    public static ErrorLog getInstance(Context context) {
        if (instance == null) {
            instance = new ErrorLog(context);
        }
        return instance;
    }

    public long getLastModiiedTime() {
        long lastModifyTime = 0;
        if (!fileList.isEmpty()) {
            String filename = fileHelper.getContentPath() + "/" + fileList.lastElement();
            File file = new File(filename);
            if (file.exists() &&  file.lastModified() > lastModifyTime) {
                lastModifyTime = file.lastModified();
            }
        }
        return lastModifyTime;
    }
/*
    public boolean isModified() {
        if (!fileList.isEmpty()) {
            String filename = fileHelper.getContentPath() + "/" + fileList.lastElement();
            File file = new File(filename);
//            System.out.println(fileList.lastElement() + file.exists() + file.lastModified() + " last:" + lastModifyTime);
            if (file.exists() &&  file.lastModified() > lastModifyTime) {
                lastModifyTime = file.lastModified();
                return true;
            }
        }
        return  false;
    }
*/
    public  int getLogCount() {
        return fileList.size();
    }

    public void addLog(String mainText) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss ");
        Date date = new Date();
        String datetimeString = simpleDateFormat.format(date);
        String firstLine = "";
        String filename = datetimeString.substring(0,8) + "_message.log";
        if (fileList.isEmpty() || !fileList.lastElement().equals(filename)) {
            firstLine = datetimeString.substring(0,8) + "\n";
            fileList.add(filename);
        }
        try {
            fileHelper.save(filename, String.format("%s%s %s\n", firstLine, datetimeString, mainText));
        } catch (Exception e) {

        }
    }

    public  String getLog(int index) {
        String content;
        if (index < 0 || index >= fileList.size()) {
            content = "empty";
        } else {
            try {
                content = fileHelper.read(fileList.elementAt(index));
            } catch (Exception e) {
                content = "read error" + e.getMessage();
            }
        }
        return content;
    }

    public String getNewLog() {
        return getLog(getLogCount() - 1);
    }

    private void initFileList() {
        String[] fileArray = fileHelper.getFileList();
        Arrays.sort(fileArray);
        fileList.clear();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].contains("_message.log") && fileArray[i].length() == 20) {
                fileList.add(fileArray[i]);
            }
        }
    }
}