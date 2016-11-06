package com.example.heyong.exercisesbase.Bean;


public class Bean {
    private String date;
    private String content;

    public Bean(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public Bean() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}