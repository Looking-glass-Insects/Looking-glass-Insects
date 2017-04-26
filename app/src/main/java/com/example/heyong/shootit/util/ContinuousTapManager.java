package com.example.heyong.shootit.util;

import android.util.Log;

import com.example.heyong.shootit.layer.GameLayer;

/**
 * 记录连击次数,为所有子弹增加冰冻效果
 */

public class ContinuousTapManager {
    static String TAG = "ContinuousTapManager";

    public static final int MAX_CONTINOUS_TIME = 10;


    private int continuousTapTime = 0;
    private GameLayer gameLayer;


    public static ContinuousTapManager getInstance() {
        return Factory.manager;
    }


    private ContinuousTapManager() {
    }

    public void registerGameLayer(GameLayer layer) {
        this.gameLayer = layer;
    }

    public void unregisterGameLayer() {
        this.gameLayer = null;
    }


    /**
     * 当用户点击屏幕时回调
     *
     * @param isTouched 用户是否点到item
     */
    public void onGetTap(boolean isTouched) {
        Log.d(TAG, "isTouched-->" + isTouched);
        if (isTouched) {
            continuousTapTime++;
            if (continuousTapTime == MAX_CONTINOUS_TIME) {
                Log.d(TAG, "sendMessage");
                if (gameLayer != null)
                    gameLayer.loadFreezeEffect();
                continuousTapTime = 0;
            }
        } else {
            continuousTapTime = 0;
        }
    }

    public void onDestroy() {
        unregisterGameLayer();
        this.continuousTapTime = 0;
    }

    private static class Factory {
        private static final ContinuousTapManager manager = new ContinuousTapManager();
    }
}
