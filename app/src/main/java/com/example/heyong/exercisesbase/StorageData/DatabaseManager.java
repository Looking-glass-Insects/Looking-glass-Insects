
package com.example.heyong.exercisesbase.StorageData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Bean.QuestionType;

import java.util.List;

/**
 * 读写数据库操作
 */

public class DatabaseManager {
    static final String TAG = "DatabaseManager";
    private Context context;
    private DatabaseOpenHelper databaseOpenHelper;
    protected SQLiteDatabase dbReadable;
    protected SQLiteDatabase dbWritable;
    private String DBname;
    private String ChooseQuestion = "choose";//选择题
    private String AnswerQuestion = "answer";//解答题
    public static final String MODEL = "model";//试题模板
    private static final String TABLES = "table_name";//科目

    protected String TABLE_NAME;//本次的题库名

    private DatabaseManager(Context context) {
        this.context = context;
        databaseOpenHelper = new DatabaseOpenHelper(context);
        databaseOpenHelper.getWritableDatabase();
        dbReadable = databaseOpenHelper.getReadableDatabase();
        dbWritable = databaseOpenHelper.getWritableDatabase();
        DBname = databaseOpenHelper.getDatabaseName();
        ChooseQuestion = DatabaseOpenHelper.getChooseQuestion();
        AnswerQuestion = DatabaseOpenHelper.getAnswerQuestion();
    }

    public DatabaseManager(Context context, String tableName) {
        this(context);
        this.TABLE_NAME = tableName;
    }

    private String getCorrectType(QuestionType type) {
        String queType = null;
        if (type == QuestionType.CHOOSE)
            queType = ChooseQuestion;
        else if (type == QuestionType.ANSWER) {
            queType = AnswerQuestion;
        }
        return queType;
    }

    /**
     * 存取数据
     */
    @Deprecated
    public void writeQuestion(String question, String answer, QuestionType type) {
        ContentValues cv = new ContentValues();
        cv.put("question", question);
        cv.put("answer", answer);
        String queType = getCorrectType(type);
        dbWritable.insert(queType, null, cv);
    }

    public void writeQuestion(String question, String answer, QuestionType type, @Nullable String tableName) {
        ContentValues cv = new ContentValues();
        cv.put("question", question);
        cv.put("answer", answer);
        cv.put("table_name", this.TABLE_NAME);
        String queType = getCorrectType(type);
        dbWritable.insert(queType, null, cv);
    }
    @Deprecated
    public void writeNewTable(String tableName) {
        ContentValues cv = new ContentValues();
        cv.put("table_name", tableName);
        dbWritable.insert(TABLES, null, cv);
    }

    @Deprecated
    public void writeModel(String content, String date) {
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("date", date);
        dbWritable.insert(MODEL, null, cv);
    }

    public void writeModel(String content, String date, @Nullable String tableName) {
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        cv.put("date", date);
        cv.put("table_name", this.TABLE_NAME);
        dbWritable.insert(MODEL, null, cv);
    }


    /**
     * 将数据库中的数据读出来
     *
     * @return 读到的集合
     */
    @Deprecated
    public void readData(List<Note> noteList, QuestionType type) {
        String queType = getCorrectType(type);
        Cursor cursor
                = dbReadable.rawQuery("SELECT * FROM " + queType + " ORDER BY id DESC", null);
        try {
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("id")));
                note.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
                note.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
                noteList.add(note);
            }
        } catch (Exception e) {

        }
    }


    public void readData(List<Note> noteList, QuestionType type, @Nullable String tableName) {
        String queType = getCorrectType(type);
        //Log.i(TAG, "-->" + this.TABLE_NAME);
        Cursor cursor
                = dbReadable.rawQuery("SELECT * FROM " + queType + " WHERE table_name = \'" + this.TABLE_NAME + "\'  ORDER BY id DESC", null);

        try {
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("id")));
                note.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
                note.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
                noteList.add(note);
            }
        } catch (Exception e) {
            // Log.i(TAG,tableName);
        }
    }

    public void readAllTable(@NonNull List<String> tables) {
        Cursor cursor
                = dbReadable.rawQuery("SELECT * FROM " + TABLES + "  ORDER BY id DESC", null);
        try {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("table_name"));
//                if(s == null || s.equals("")){
//                    s = "未命名";
//                }
                tables.add(s);
            }
        } catch (Exception e) {

        }
    }

    @Deprecated
    public long allCaseNum(QuestionType type) {
        String queType = getCorrectType(type);
        String sql = "select count(*) from " + queType;
        Cursor cursor = dbReadable.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    public long allCaseNum(QuestionType type, @Nullable String tableName) {
        String queType = getCorrectType(type);
        String sql = "select count(*) from " + queType + " WHERE table_name = \'" + this.TABLE_NAME + "\'";
        Cursor cursor = dbReadable.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    public void readRandomData(List<Note> noteList, int count, QuestionType type) {
        long sum = allCaseNum(type, null);
        String queTyp = getCorrectType(type);
        if (sum <= 0) return;
        if (count > sum) count = (int) sum - 1;
        Cursor cursor
                = dbReadable.rawQuery("SELECT * FROM " + queTyp + " ORDER BY id DESC", null);
        int[] list = randomCommon(0, (int) sum, count);
        if (list == null) return;
        for (int i = 0; i < list.length; i++) {
            cursor.moveToPosition(list[i]);
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex("id")));
            note.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
            note.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
            noteList.add(note);
        }
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    private static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

    /**
     * 更新一条记录
     */
    public void updateNote(int noteID, String question, String answer, QuestionType type) {
        String queType = getCorrectType(type);
        ContentValues cv = new ContentValues();
        cv.put("question", question);
        cv.put("answer", answer);
        dbWritable.update(queType, cv, "id = ?", new String[]{noteID + ""});
    }

    @Deprecated
    public void updateNote(Note note, QuestionType type) {
        String queType = getCorrectType(type);
        ContentValues cv = new ContentValues();
        cv.put("question", note.getQuestion());
        cv.put("answer", note.getAnswer());
        int id = note.getId();
        dbWritable.update(queType, cv, "id = ?", new String[]{id + ""});
    }

    public void renameTable(String currName,String newName) {
        ContentValues cv = new ContentValues();
        cv.put("table_name", newName);
        dbWritable.update(TABLES, cv, "table_name = ?", new String[]{currName});
        dbWritable.update(ChooseQuestion, cv, "table_name = ?", new String[]{currName});
        dbWritable.update(AnswerQuestion, cv, "table_name = ?", new String[]{currName});
    }

    /**
     * 根据id查询数据
     *
     * @param noteID 要查询的日记的id
     * @return 所查询到的结果
     */
    @Deprecated
    public Note readData(int noteID, QuestionType type) {
        String queType = getCorrectType(type);
        Cursor cursor = dbReadable.rawQuery("SELECT * FROM " + queType + " WHERE id = ?", new String[]{noteID + ""});
        cursor.moveToFirst();
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex("id")));
        note.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
        note.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
        return note;
    }

    /**
     * 根据id删除该条记录
     *
     * @param noteID 要删除记录的id
     */
    @Deprecated
    public void deleteNote(int noteID, QuestionType type) {
        String queType = getCorrectType(type);
        dbWritable.delete(queType, "id = ?", new String[]{noteID + ""});
    }

    public void deleteNote(int noteID, QuestionType type, @Nullable String tableName) {
        String queType = getCorrectType(type);
        dbWritable.delete(queType, "id = ?", new String[]{noteID + ""});
    }

    @Deprecated
    public void deleteNote(String question, QuestionType type) {
        String queType = getCorrectType(type);
        dbWritable.delete(queType, "question = ?", new String[]{question + ""});
    }

    public void deleteNote(String question, QuestionType type, @Nullable String tableName) {
        String queType = getCorrectType(type);
        dbWritable.delete(queType, "question = ? AND table_name = ?", new String[]{question + "", this.TABLE_NAME});
    }

    @Deprecated
    public void deleteAll(QuestionType type) {
        String queType = getCorrectType(type);
        String sql = "delete from " + queType;
        dbWritable.execSQL(sql);
    }

    public void deleteAll(QuestionType type, @Nullable String tableName) {
        String queType = getCorrectType(type);
        String sql = "delete from " + queType + " WHERE table_name = \'" + this.TABLE_NAME + "\'";
        dbWritable.execSQL(sql);
    }

    public void deleteTable(String table_name) {
        String sql = "delete from " + ChooseQuestion + " WHERE table_name = \'" + table_name + "\'";
        dbWritable.execSQL(sql);
        sql = "delete from " + AnswerQuestion + " WHERE table_name = \'" + table_name + "\'";
        dbWritable.execSQL(sql);
        sql = "delete from " + TABLES + " WHERE table_name = \'" + table_name + "\'";
        dbWritable.execSQL(sql);
    }

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }


    public boolean checkIfTableExists(String tableName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = dbReadable.rawQuery("select * from " + TABLES + " where table_name = ?"
                    , new String[]{tableName});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {

        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}
