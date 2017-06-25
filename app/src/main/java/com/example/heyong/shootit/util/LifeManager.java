package com.example.heyong.shootit.util;

import com.example.heyong.shootit.layer.GameLayer;

/**
 *
 */

public class LifeManager {
    public static final int INIT_LIFE = 10;

    private int life;
    private GameLayer layer;


    private LifeManager() {
        init();
    }

    public void init() {
        life = INIT_LIFE;
    }

    public void subLife(int sub) {
        if (life - sub > 0) {
            this.life -= sub;
        } else {
            //gg
            //layer.gameOver();
        }
    }

    /**
     *
     * @param layer
     */
    public void registerGameLayer(GameLayer layer){
        this.layer = layer;
    }

    public void addLife(int add) {
        life += add;
    }

    public int getLife() {
        return life;
    }

    public static LifeManager getInstance() {
        return Factory.manager;
    }

    private static class Factory {
        private static LifeManager manager = new LifeManager();
    }
}
