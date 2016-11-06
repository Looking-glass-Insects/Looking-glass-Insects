package com.example.heyong.exercisesbase.StorageData;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FilesWriter {
    private static final String APPNAME = "QuestionBase";
    private Context context;
    protected String absolutePath;//app的文件夹下

    public FilesWriter(Context context) {
        this.context = context;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "磁盘已满", Toast.LENGTH_SHORT).show();
            return;
        }
        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
        File folder = new File(sdCardDir.getAbsolutePath() + File.separator + APPNAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        absolutePath = folder.getAbsolutePath();
    }

    /**
     * 保存于/data/data//files
     */
    public void writeHideFile(String filename, byte[] filedata) {
        File file = context.getFilesDir();
        FileOutputStream fos = null;
        File folder = new File(file.getAbsolutePath() + "/" + APPNAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            fos = new FileOutputStream(folder.getAbsolutePath() + "/" + filename);
            fos.write(filedata, 0, filedata.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存于SD卡
     * /storage/emulated/0/APPName/
     */
    public void writeFile(String filename, byte[] data) {
        File saveFile = new File(absolutePath + File.separator + filename);
        if (saveFile.exists()) {
            return;
        }
        FileOutputStream outStream = null;
        // Log.i("test", saveFile+"");
        //if(!saveFile.exists())return;
        try {
            outStream = new FileOutputStream(saveFile);
            //Log.i("test", saveFile+"");
            outStream.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "文件写入出错", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean writeFile(String filename, String path, String content) {
        File saveFile = new File(path + File.separator + filename);
        if (saveFile.exists()) return false;
        FileOutputStream outStream = null;
        OutputStreamWriter writer = null;
        try {
            outStream = new FileOutputStream(saveFile);
            writer = new OutputStreamWriter(outStream, "utf-8");
            //Log.i("test", saveFile+"");
            writer.write(content);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "文件写入出错", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //默认为app的文件夹下
    public boolean writeFile(String filename, String content) {
        return writeFile(filename, getAbsolutePath(), content);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
}
