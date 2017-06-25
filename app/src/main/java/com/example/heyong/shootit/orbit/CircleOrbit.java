package com.example.heyong.shootit.orbit;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseCircleBullet;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * Created by Heyong on 2017/6/23.
 */

public class CircleOrbit extends BaseOrbitController {
    static String TAG = "CircleOrbit";
    private int i = 0;
    private float speed = 5.0f / 20;
    private final int INTERVAL = 10;//角度间隔
    private int flag = 1;
    private  int LONG_PERIOD = 60 * 30;
    private float startX = Config.WINDOW_WIDTH / 2;
    private float startY = Config.WINDOW_HEIGHT / 2;
    private int R = 100;
    private final int DELAY = 360;

    public CircleOrbit() {
    }

    public CircleOrbit(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void setLONG_PERIOD(int LONG_PERIOD) {
        this.LONG_PERIOD = LONG_PERIOD;
    }

    @Override
    public int getZ() {
        return 17;
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        for (int n = items.size() - 1; n >= 0; n--) {
            BaseItem item = items.get(n);
            if (item.isTouched(point)) {
                item.onHandleTouchEvent(point);
                break;
            }
        }
    }

    @Override
    public void onGetClock() {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            CGPoint point = item.getPosition();
            if (point.x < -32 || point.x > Config.WINDOW_WIDTH + 32 || point.y < -32 || point.y > Config.WINDOW_HEIGHT + 32) {
                if (item.getVisible()){
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
                }
            }
            item.onGetClock();
            if (item.timer == DELAY && (point.x - startX) * (point.x - startX) + (point.y - startY) * (point.y - startY) >= R * R) {
                item.replaceSpeed(0, 0);
                item.f();
            }
            if (item.timer > 0 && item.timer < DELAY) {
                item.f();
                Log.d(TAG, "-->" + item.timer);
                if (item.timer <= 0) {
                    item.resumeSpeed();
                }
            }
        }
        i++;
        if (i % 20 == 0 && i < LONG_PERIOD) {
            BaseCircleBullet b = new BaseCircleBullet() {
                @Override
                public void f() {
                    timer--;
                }
            };
            b.setPosition(startX, startY);
            int count = items.size();
            b.speedX = (float) (speed * Math.sin(INTERVAL * count));
            b.speedY = (float) (speed * Math.cos(INTERVAL * count));
            b.timer = DELAY;
            addItem(b);
        }
    }

    @Override
    public boolean isTouched(CGPoint point) {
        return true;
    }

    @Override
    public boolean canBeDestroyed() {
        int deadCount =0;
        for(int i=0;i<getItemCount();i++){
            if (!items.get(i).getVisible())
                deadCount++;
        }
        return i > LONG_PERIOD && deadCount >= getItemCount();
    }
}
