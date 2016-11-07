package com.example.heyong.exercisesbase.Bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Note implements Serializable {
    private String question;
    private String answer;
    private String TABLE_NAME;//从属题库名
    private int id = -1;
    public Note() {

    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public Note(String question, String answer, int id) {
        this.question = question;
        this.answer = answer;
        this.id = id;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public void setId(int id) {

        this.id = id;
    }
    public Note(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getId() {
        return id;
    }


}
