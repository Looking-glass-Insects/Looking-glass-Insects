package com.example.heyong.shootit.orbit;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseBigBullet;
import com.example.heyong.shootit.sprite.bullet.BaseTailBullet;

import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 重力自由落体
 */

public class GravityOrbitController extends BaseOrbitController {
    static String TAG = "GravityOrbitController";
    protected List<BaseItem> items = new ArrayList<>();
    /**
     * 下落起始x坐标
     */
    protected int start_x;
    /**
     * 判定宽度
     */
    protected int width;

    protected float g = 0;//重力加速度


    public GravityOrbitController(int start_x, int width) {
        this.start_x = start_x;
        this.width = width;
    }

    @Override
    public void addItem(BaseItem item) {
        BaseTailBullet b = new BaseTailBullet(BaseTailBullet.RED);
        b.setFrozen(true);
        b.setPosition(start_x, Config.WINDOW_HEIGHT);
        super.addItem(b);
        items.add(b);
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
            //Log.d(TAG, "false");
            return false;
        } else {
            //Log.d(TAG, "true");
            return true;
        }
    }


    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    @Override
    public void onGetClock() {
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

    class BigGravityBullet extends BaseBigBullet {

        static final int r = 64;
        float speed = 0;
        Random random = new Random();

        public BigGravityBullet(int color) {
            super(color);
            this.position_.x = start_x;
            this.position_.y = Config.WINDOW_HEIGHT + 32 + random.nextInt(256);
        }

        @Override
        public void onHandleTouchEvent(CGPoint point) {
            this.speed = 0;
            this.position_.x = start_x;
            this.position_.y = Config.WINDOW_HEIGHT + random.nextInt(128);
        }
    }

}
