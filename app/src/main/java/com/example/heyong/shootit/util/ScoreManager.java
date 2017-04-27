package com.example.heyong.shootit.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 分数记录
 */

public class ScoreManager {
    public static String TAG_HIGH_SCORE = "highScoreLabel";

    private long highScore = 0;
    private Context context;
    private long score = 0;

    public static ScoreManager getInstance() {
        return Factory.manager;
    }

    public void loadHighScore() {
        assertContext();
        SharedPreferences sp = context.getSharedPreferences(TAG_HIGH_SCORE, 0);
        highScore = sp.getLong(TAG_HIGH_SCORE, 0);
    }

    public void saveHighScore() {
        assertContext();
        SharedPreferences sp = context.getSharedPreferences(TAG_HIGH_SCORE, 0);
        sp.edit().putLong(TAG_HIGH_SCORE, highScore).apply();
    }


    public void onGetScore(int score) {
        this.score += score;
        replaceHighScore();
    }

    public void onGetScore() {
        onGetScore(1);
    }


    private void replaceHighScore() {
        if (score > highScore) {
            highScore = score;
        }
    }

    private void assertContext() {
        if (context == null)
            throw new IllegalStateException("context is null");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public long getHighScore() {
        return highScore;
    }

    public long getScore() {
        return score;
    }

    /**
     * 刷新记分板
     */
    public void init(){
        this.score = 0;
        loadHighScore();
    }

    public void onDestroy() {
        saveHighScore();
        context = null;
    }

    private ScoreManager() {

    }

    private static class Factory {
        private static ScoreManager manager = new ScoreManager();
    }
}
