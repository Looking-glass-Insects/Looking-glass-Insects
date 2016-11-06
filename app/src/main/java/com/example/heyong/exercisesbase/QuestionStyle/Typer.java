package com.example.heyong.exercisesbase.QuestionStyle;

import com.example.heyong.exercisesbase.Bean.Note;

import java.util.List;

/**
 * 对问题进行排版
 */
public class Typer {
    public static void typeSetting(List<Note> list) {
        for (int i = 0; i < list.size(); i++) {
            String s = (i + 1) + "、" + list.get(i).getQuestion();
            list.get(i).setQuestion(s);
        }
    }

    public static String typeOutputQue(List<Note> data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Note n : data) {
            stringBuilder.append(n.getQuestion());
            stringBuilder.append("\n");
        }
        return  stringBuilder.toString();
    }
}
