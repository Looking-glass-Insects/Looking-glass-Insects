package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseCircleBullet;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * Created by Heyong on 2017/6/22.
 * 旋转
 */

public class SinOrbit extends BaseOrbitController {

    private float startX = Config.WINDOW_WIDTH / 2;
    private float startY = Config.WINDOW_HEIGHT / 2;
    private int i = 0;
    private float speed = 5.0f / 20;
    private final int INTERVAL = 5;//角度间隔
    private int flag = 1;
    private final int LONG_PERIOD = 3600;


    public SinOrbit() {
    }


    public SinOrbit(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public void onGetClock() {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            CGPoint point = item.getPosition();
            if (point.x < -32 || point.x > Config.WINDOW_WIDTH + 32 || point.y < -32 || point.y > Config.WINDOW_HEIGHT + 32) {
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
                }
            }
            item.onGetClock();
        }
        i++;
        if (i % 20 == 0) {
            BaseCircleBullet b = new BaseCircleBullet();
            b.setPosition(startX, startY);
            int count = items.size();
            b.speedX = (float) (flag * speed * Math.sin(INTERVAL * count));
            b.speedY = (float) (speed * Math.cos(INTERVAL * count));
            addItem(b);
        }
        if (i % 180 == 0) {
            flag *= -1;
        }
    }

    @Override
    public int getZ() {
        return 15;
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
    public boolean isTouched(CGPoint point) {
        return true;
    }

    @Override
    public boolean canBeDestroyed() {
        return i > LONG_PERIOD;
    }
}
