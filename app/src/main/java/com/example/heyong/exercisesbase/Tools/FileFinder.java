package com.example.heyong.exercisesbase.Tools;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 找到数据文件
 */
public class FileFinder {
    static final String TAG = "FileFinder";
    public static List<String> findFile(){
        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
        Log.i(TAG,"findFile");
        return findFile(sdCardDir,".txt");
    }
    private static List<String> findFile(File file, String keyword) {
        List<String> _files = new ArrayList<>();
        if (!file.isDirectory()) {
            return null;
        }
        File[] files = new File(file.getPath()).listFiles();
        for (File f : files) {
            if (f.getName().indexOf(keyword) >= 0) {
                _files.add(f.getName());
            }
            Log.i(TAG,"findFile----ing");
        }
        Log.i(TAG,"findFile----finish");
        return _files;
    }
}
