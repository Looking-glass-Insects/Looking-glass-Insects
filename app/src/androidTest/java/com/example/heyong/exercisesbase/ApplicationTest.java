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

    }
}