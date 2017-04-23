package com.example.heyong.shootit.orbit;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseBigBullet;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * 重力自由落体
 */

public class GravityOrbitController extends BaseOrbitController {
    static String TAG = "GravityOrbitController";

    /**
     * 下落起始x坐标
     */
    protected int start_x;
    /**
     * 判定宽度
     */
    protected int width;

    protected float g = 0;//重力加速度

    protected int deadItemCount = 0;

    public GravityOrbitController(int start_x, int width) {
        this.start_x = start_x;
        this.width = width;
    }

    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
    }

    public void addItem(float y) {
        BaseBigBullet b = new BaseBigBullet(BaseBigBullet.RED);
        b.setPosition(start_x, y);
        addItem(b);
    }


    @Override
    public int getZ() {
        return 2;
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            Log.d(TAG, "touch-->" + point.x + "," + point.y);
            BaseItem item = iterator.next();
            Log.d(TAG, "item-->" + item.getPosition().x + "," + item.getPosition().y);
            if (item.isTouched(point)) {
                deadItemCount++;
                item.onHandleTouchEvent(point);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean isTouched(CGPoint point) {
        if (Math.abs(point.x - start_x) > width / 2) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean canBeDestroyed() {
        //当所有item不可见时该轨道可销毁
        return deadItemCount >= getItemCount();
    }

    @Override
    public void onGetClock() {
        super.onGetClock();
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            CGPoint point = item.getPosition();
            if (point.y < -64) {
                item.speedY = 1.0f;
                item.setPosition(start_x, Config.WINDOW_HEIGHT + 32);
                break;
            }
            item.speedY += g;
            item.setPosition(start_x, point.y - item.speedY);
        }
    }

}
