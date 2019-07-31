package com.example.shan.mqtt;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    private Context mContext;

    public FileHelper() {
    }

    public FileHelper(Context mContext) {
        super();
        this.mContext = mContext;
    }
    public File getContentPath() {
        return mContext.getFilesDir();
    }
    /*
    * 这里定义的是一个文件保存的方法，写入到文件中，所以是输出流
    * */
    public void save(String filename, String filecontent) throws Exception {
        FileOutputStream output = mContext.openFileOutput(filename, Context.MODE_APPEND);
        output.write(filecontent.getBytes());
        output.close();
    }


    /*
    * 这里定义的是文件读取的方法
    * */
    public String read(String filename) throws IOException {
        //打开文件输入
        FileInputStream input = mContext.openFileInput(filename);
        byte[] temp = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        //读取文件内容:
        while ((len = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, len));
        }
        //关闭输入流
        input.close();
        return sb.toString();
    }

    public String[] getFileList() {
//        for (String ss: mContext.fileList()) {
//            System.out.println(ss);
//        }
        return mContext.fileList();
    }

}