package com.example.rkrde.awesometexteditor;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rkrde on 09-09-2017.
 */

public class Constants {

    public static void copyAndClose(InputStream inputStream, OutputStream outputStream) {
        try {
            int i;
            while ((i = inputStream.read()) != -1) {
                outputStream.write(i);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentTime(){
        Date date = new Date();
        String strDateFormat = "yyyy-MMM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }

    public class Async extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }

    public static void saveImageFromDisk(Context context, File sourceFile, String extension){

        String fileName = getNewFileName(context,extension);
        File rootMedia = context.getDir("media",Context.MODE_PRIVATE);
        File destFile = new File(rootMedia,fileName);

        try {
            destFile.createNewFile();
            destFile.mkdir();

            InputStream is = new FileInputStream(sourceFile);
            FileOutputStream fos  = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNewFileName(Context context,String extension)
    {
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);
        File rootDir  = createRootDir(context);

        String fileNameArray[] = rootDir.list();

        if (fileNameArray.length == 0) {
            return thisDate+"-"+String.valueOf(0)+"."+extension;

        } else {
            //Actual code 2016-09-19-3
            String lastFileName = fileNameArray[fileNameArray.length - 1];
            int i = Integer.parseInt((lastFileName.split("-")[3]).split("\\.")[0]);
            i = i+1;
            return thisDate+"-"+String.valueOf(i)+"."+extension;
        }

    }

    static File createRootDir(Context context)
    {
        File root = context.getDir("media",Context.MODE_PRIVATE);
        return  root;
    }
}
