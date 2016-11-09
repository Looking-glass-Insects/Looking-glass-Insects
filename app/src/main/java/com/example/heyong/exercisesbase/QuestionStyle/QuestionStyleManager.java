package com.example.heyong.exercisesbase.QuestionStyle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.AdapterView;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.CustomView.StyleDialog;
import com.example.heyong.exercisesbase.MainActivity;
import com.example.heyong.exercisesbase.R;
import com.example.heyong.exercisesbase.StorageData.ModelFileManager;

import java.util.List;


public class QuestionStyleManager  {

    private SharedPreferences sp;
    private MainActivity activity;
    private StyleDialog dialog;//编辑试题模板
    private ModelFileManager manager;
    static final String TAG  = "QuestionStyleManager";
    public QuestionStyleManager(final MainActivity activity) {
        this.activity = activity;
        dialog = new StyleDialog(activity, R.style.StyleDialog ,this,activity.tableName);
        manager = new ModelFileManager(activity,activity.tableName);
    }
    public void freshUI(List<Note> l){
        activity.initUI(0,l);
    }
    public void saveData(List<Note> data,String title) {
        for(Note n:data){
            Log.i(TAG,n.getQuestion());
        }
        manager.writeModel(data, title);
    }



    public void showDialog() {
        dialog.show();
    }

}
