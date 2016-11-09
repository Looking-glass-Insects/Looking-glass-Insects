package com.example.heyong.exercisesbase.MyThread;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Bean.QuestionType;
import com.example.heyong.exercisesbase.StorageData.DatabaseManager;
import com.example.heyong.exercisesbase.StorageData.QuestionLoader;

import java.util.ArrayList;
import java.util.List;

public class QuestionLoaderThread extends AsyncTask<String, Integer, Integer> {
    private DatabaseManager dbManager;
    private QuestionLoader loader;
    private Context context;
    //public static final int FAILTAG = -1;//导入文件失败，文件不符合规格
    static final String TAG = "QuestionLoaderThread";

    public QuestionLoaderThread(Context context,String tableName) {
        this.context = context;
        loader = new QuestionLoader(context);
        dbManager = new DatabaseManager(context,tableName);
//        if(!dbManager.checkIfTableExists(tableName)){
//            dbManager.writeNewTable(tableName);
//        }
    }

    /**
     * 运行在UI线程中，在调用doInBackground()之前执行
     */
    @Override
    protected void onPreExecute() {
        //Toast.makeText(context,"开始导入文件",Toast.LENGTH_SHORT).show();
    }

    /**
     * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
     */
    @Override
    protected Integer doInBackground(String... params) {
        String path = "";
        Integer flag;
        if (params.length > 0)
            path = params[0];
        List<String[]> list = new ArrayList<>();
        if (!path.equals("")) {
            flag = loader.getQuestions(list,path);
            Log.i(TAG,path);
        } else {
            flag = loader.getQuestions(list);
        }
        //if (list == null || list.size() == 0) return null;
        switch (flag){
            case QuestionLoader.FILEFORMATFAIL:
                return QuestionLoader.FILEFORMATFAIL;
            case QuestionLoader.NOSUCHFILE:
                return QuestionLoader.NOSUCHFILE;
            case QuestionLoader.SUCCESS:
                break;
        }
        for (String[] s : list) {
            if (s[0].equals(QuestionLoader.CHOOSE_QUE_TAG)) {
                dbManager.writeQuestion(s[1], s[2], QuestionType.CHOOSE,null);
            } else if (s[0].equals(QuestionLoader.ANSWER_QUE_TAG)) {
                dbManager.writeQuestion(s[1], s[2], QuestionType.ANSWER,null);
            } else {
                return QuestionLoader.FILEFORMATFAIL;//格式有问题
            }
        }
        return QuestionLoader.SUCCESS;
    }

    private boolean testAnser(String s) {
        char[] _s = s.toCharArray();
        if (_s.length < 1) {
            return false;
        } else if (_s[0] - 'A' < 0 || _s[0] - 'A' > 3) {
            return false;
        } else return true;
    }

    /**
     * 运行在ui线程中，在doInBackground()执行完毕后执行
     */
    @Override
    protected void onPostExecute(Integer integer) {
        if (integer == null) return;
        else if (integer.equals(QuestionLoader.FILEFORMATFAIL)) {
            Toast.makeText(context, "导入失败,文件不符合规格", Toast.LENGTH_SHORT).show();
        } else if (integer.equals(QuestionLoader.SUCCESS)) {
            Toast.makeText(context, "导入完毕,重启后生效", Toast.LENGTH_SHORT).show();
        } else if(integer.equals(QuestionLoader.NOSUCHFILE)){
            //Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
     */
    @Override
    protected void onProgressUpdate(Integer... values) {

    }
}
