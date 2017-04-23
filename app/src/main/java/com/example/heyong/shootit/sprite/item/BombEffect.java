package com.example.heyong.shootit.sprite.item;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.types.CGPoint;

/**
 *
 */

public class BombEffect extends BaseItem {
    static String TAG = "BombEffect";
    static final float LAST_TIME = 2 * 60;

    protected float lastTime = LAST_TIME;//2s

    public BombEffect() {
        super(Config.bomb);
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {

    }

    public void fresh() {
        this.setOpacity(100);
        this.lastTime = LAST_TIME;
    }


    @Override
    public void onGetClock() {
        if (lastTime == -1) {
            Log.d(TAG, "remove");
        } else {
            this.setOpacity((int) (lastTime / LAST_TIME * 255));
            lastTime--;
        }

    }

    public int getZ() {
        return 50;
    }
}
