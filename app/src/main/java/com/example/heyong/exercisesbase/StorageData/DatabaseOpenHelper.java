package com.example.heyong.exercisesbase.StorageData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.heyong.exercisesbase.Bean.TableName;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DBname = "question";
    private static final String ChooseQuestion = "choose";//选择题
    private static final String AnswerQuestion = "answer";//解答题
    private static final String MODEL = "model";//试题模板
    private static final String TABLE_NAME = "table_name";//科目
    /**
     * id date content
     */
    private static final int version = 3;

    public DatabaseOpenHelper(Context context) {
        super(context, DBname, null, version);
    }

    public static String getChooseQuestion() {
        return ChooseQuestion;
    }

    public static String getAnswerQuestion() {
        return AnswerQuestion;
    }

    public static int getVersion() {
        return version;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table  " + ChooseQuestion + "(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "question text," +
                "answer text," +
                "table_name text"+
                ")";
        db.execSQL(sql);
        sql = "create table " + AnswerQuestion + "(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "question text," +
                "answer text," +
                "table_name text"+
                ")";
        db.execSQL(sql);
        sql = "create table "+ MODEL + "(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "content text," +
                "date text,"+
                "table_name text"+
                ")";
        db.execSQL(sql);
        sql = "create table "+ TABLE_NAME + "(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "table_name text"+
                ")";
        db.execSQL(sql);
        sql = "INSERT INTO "+TABLE_NAME +" (table_name) VALUES (\'"+ TableName.TAVLE_1 +"\')";
        db.execSQL(sql);
        sql = "INSERT INTO "+TABLE_NAME +" (table_name) VALUES (\'"+ TableName.TAVLE_2 +"\')";
        db.execSQL(sql);
        sql = "INSERT INTO "+TABLE_NAME +" (table_name) VALUES (\'"+ TableName.TAVLE_3 +"\')";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Note");
//        onCreate(db);
    }
}
