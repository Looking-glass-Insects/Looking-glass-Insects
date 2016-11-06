package com.example.heyong.exercisesbase;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.StorageData.ModelFileManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    private static final String DBname = "question";
    public void test() {
        getContext().deleteDatabase(DBname);
        Note n = new Note("test1_que","test1_ans");
        Note n2 = new Note("test2_que","test2_ans");
        Note n3 = new Note("test3_que","test3_ans");
        List<Note> l = new ArrayList<>();
        l.add(n);
        l.add(n2);
        l.add(n3);
        ModelFileManager manager = new ModelFileManager(getContext());
        manager.writeModel(l);
    }
}