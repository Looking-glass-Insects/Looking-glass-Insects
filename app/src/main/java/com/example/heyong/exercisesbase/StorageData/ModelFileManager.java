package com.example.heyong.exercisesbase.StorageData;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.heyong.exercisesbase.Bean.Bean;
import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.StorageData.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelFileManager extends DatabaseManager{
    private Context context;
    private static final String FORMAT = "{<问题>|<答案>}";
    public ModelFileManager(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 写一条
     * @param notes
     */
    public void writeModel(@Nullable List<Note> notes,String title) {
        if (notes.size() <= 0) return;
        String content = makeContent(notes);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE-yyyy-MM-dd-hh:mm:ss");
        String dateTime = dateFormat.format(date);
        this.writeModel(content,dateTime + "&" + title);
    }
    public void readModel(@Nullable List<Bean> list){
        Cursor cursor
                = this.dbWritable.rawQuery("SELECT * FROM " + MODEL + " ORDER BY id DESC", null);
        try{
            while(cursor.moveToNext()){
                Bean bean = new Bean();
                bean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                bean.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(bean);
            }
        }catch(Exception e){

        }
    }
    public void delModel(String date){
        dbWritable.delete(MODEL, "date = ?", new String[]{date});
    }
    public void updateModel(Bean bean){
        ContentValues cv = new ContentValues();
        cv.put("date", bean.getDate());
        cv.put("content", bean.getContent());
        dbWritable.update(MODEL, cv, "date = ?", new String[]{bean.getDate()});
    }
    public String makeContent(List<Note> notes) {
        StringBuilder sb  = new StringBuilder();
        for(Note n:notes){
            sb.append("{");
            sb.append(n.getQuestion());
            sb.append("|");
            sb.append(n.getAnswer());
            sb.append("}");
        }
        return sb.toString();
    }
    public List<Note> getNotesList(String content){
        List<Note> notes = new ArrayList<>();
        String re = "\\{([\\s\\S]*?)\\|([\\s\\S]*?)\\}";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            // Get the matching string
            String ques = matcher.group(1);
            String ans = matcher.group(2);
            notes.add(new Note(ques,ans));
        }
        return notes;
    }
    public List<Note> getFirstNotes(){
        Cursor cursor
                = this.dbWritable.rawQuery("SELECT * FROM " + MODEL + " ORDER BY id DESC", null);
        List<Note> notes = null;
        try{
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            notes = getNotesList(content);
        }catch (Exception e){

        }
        //list.add(bean);
        return notes;
    }
}
