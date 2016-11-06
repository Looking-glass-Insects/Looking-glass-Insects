package com.example.heyong.exercisesbase.StorageData;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 导入外部文件
 */
public class QuestionLoader {
    private Context context;
    private String absolutePath;
    private static String re = "\\{([A-D])\\|([\\s\\S]*?)\\|([\\s\\S]*?)\\}";
    private String fileName;
    private static String QUE_FILE_NAME = "question.txt";
    public final static String CHOOSE_QUE_TAG = "A";
    public final static String ANSWER_QUE_TAG = "B";
    static String TAG = "QuestionLoader";
    public static final int NOSUCHFILE = 0;
    public static final int FILEFORMATFAIL = 1;
    public static final int SUCCESS = 2;
    public QuestionLoader(Context context) {
        this.context = context;
        absolutePath = new FilesWriter(context).getAbsolutePath();
        fileName = absolutePath + File.separator + QUE_FILE_NAME;
    }

    public Integer getQuestions(@NonNull List<String[]> list) {
        final File f = new File(fileName);
        return getQuestions(list,f.getAbsolutePath());
    }

    public Integer getQuestions(@NonNull List<String[]> list,@NonNull String path) {
        final File f = new File(path);
        if (!f.exists()) {
            return NOSUCHFILE;//文件不存在
        }
        StringBuilder s = new StringBuilder();
        BufferedReader reader = null;
        FileInputStream fis;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                s.append(tempString);
                s.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String tag = matcher.group(1);
            String ques = matcher.group(2);
            String ans = matcher.group(3);
            list.add(new String[]{tag, ques, ans});
        }
        if(list.size() <= 0)return FILEFORMATFAIL;//并没有读到什么
        f.renameTo(new File(f.getParent() + File.separator + "_question.txt"));//文件改名
        return SUCCESS;
    }

}
