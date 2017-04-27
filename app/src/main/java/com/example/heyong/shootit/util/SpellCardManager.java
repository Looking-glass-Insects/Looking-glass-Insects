package com.example.heyong.shootit.util;

/**
 * Created by Heyong on 2017/4/25.
 */

public class SpellCardManager {
    public static final int DEFAULT_COUNT = 3;
    private int count = DEFAULT_COUNT;


    private SpellCardManager() {
    }

    public void init() {
        this.count = DEFAULT_COUNT;
    }

    public int getCount() {
        return count;
    }

    public void onGetSpellCard() {
        this.count++;
    }

    public boolean requireSpellCard() {
        if (this.count > 0) {
            this.count--;
            return true;
        } else return false;
    }

    public static SpellCardManager getInstance() {
        return Factory.manager;
    }


    private static class Factory {
        private static final SpellCardManager manager = new SpellCardManager();
    }
}
