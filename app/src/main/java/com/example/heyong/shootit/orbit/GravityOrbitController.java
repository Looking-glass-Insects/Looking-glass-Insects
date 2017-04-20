package com.example.heyong.shootit.orbit;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseBigBullet;
import com.example.heyong.shootit.sprite.BaseItem;

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
    private List<BaseItem> items = new ArrayList<>();
    /**
     * 下落起始x坐标
     */
    private int start_x;
    /**
     * 判定宽度
     */
    private int width;

    private float g = 9.8f / 120;//重力加速度


    public GravityOrbitController(int start_x, int width) {
        this.start_x = start_x;
        this.width = width;
    }

    @Override
    public void addItem(BaseItem item) {
        Random random = new Random();
        int ran = random.nextInt(3);
        BigGravityBullet b = null;
        if (ran == 0) {
            b = new BigGravityBullet(BaseBigBullet.BULE);
        } else if (ran == 1) {
            b = new BigGravityBullet(BaseBigBullet.RED);
        } else if (ran == 2) {
            b = new BigGravityBullet(BaseBigBullet.YELLOW);
        }
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
            BaseItem item = iterator.next();
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
            Log.d(TAG, "false");
            return false;
        } else {
            Log.d(TAG, "true");
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
            BigGravityBullet item = (BigGravityBullet) iterator.next();
            CGPoint point = item.getPosition();
            if (point.y < -64) {
                item.speed = 0;
                item.setPosition(start_x, Config.WINDOW_HEIGHT + 32);
                break;
            }
            item.speed += g;
            item.setPosition(start_x, point.y - item.speed);
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

        @Override
        public boolean isTouched(CGPoint point) {
            float dx = point.x - this.position_.x;
            float dy = point.y - this.position_.y;
            return dx * dx + dy * dy <= r * r;
        }
    }

}
