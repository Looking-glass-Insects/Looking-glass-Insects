package com.example.heyong.shootit.orbit;

import android.support.v4.util.SparseArrayCompat;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.layer.OnlineLayer;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;
import java.util.Random;

/**
 * 随机轨道类
 */

public class RandomOrbit extends BaseOrbitController {

    private Random random = new Random();
    private OnlineLayer onlineLayer;
    private SparseArrayCompat<Function> map = new SparseArrayCompat();

    @Override
    public void addItem(BaseItem item) {
        float randX = random.nextInt((int) Config.WINDOW_WIDTH / 4) - Config.WINDOW_WIDTH / 8;

        item.speedX = 2.0f;
        item.speedY = 2.0f;

        float y = Config.WINDOW_HEIGHT + 32 + random.nextInt(64);
        float x = randX + Config.WINDOW_WIDTH / 2;
        item.setPosition(x, y);

        float gap = random.nextFloat() * 128 - 64;

        float gapA = random.nextFloat() / 400 - 1.0f / 800;

        Function f = new Function(gapA, Config.WINDOW_HEIGHT / 2 + gap);
        f.init(x, y);

        map.append(getItemCount(), f);
        item.setId(getItemCount());

        item.setRotation(random.nextFloat() * 360);

        super.addItem(item);
    }

    @Override
    public int getZ() {
        return 12;
    }

    public void setOnlineLayer(OnlineLayer onlineLayer) {
        this.onlineLayer = onlineLayer;
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (item.isTouched(point)) {
                onlineLayer.addTap();
                item.onHandleTouchEvent(point);
                break;
            }
        }
    }

    @Override
    public boolean isTouched(CGPoint point) {
        return true;
    }

    /**
     * 随机弹一直从在，不销毁该轨道
     *
     * @return
     */
    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    @Override
    public void onGetClock() {
        super.onGetClock();
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (!item.getVisible()) {
                iterator.remove();
                continue;
            }
            CGPoint point = item.getPosition();
            if (point.y < -32 && point.x < Config.WINDOW_WIDTH && point.x > 0) {
                //到板底还能看到说明没点到，扣血
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
                    iterator.remove();
                }
                continue;
            }
            Function f = map.get(item.getId());
            float x = f.f(point.y - item.speedY);
            item.setPosition(x, point.y - item.speedY);
            item.setRotation(1.0f + item.getRotation());
        }
    }

    /**
     * 提供二次曲线的轨道: x = a(y - h)^2 + c
     */
    public static class Function {
        private float a;
        private float h;
        private float c;

        public Function(float a, float h) {
            this.a = a;
            this.h = h;
        }

        /**
         * 用一个点来初始化具体的轨道
         *
         * @param x
         * @param y
         */
        public void init(float x, float y) {
            this.c = x - a * (y - h) * (y - h);
        }

        /**
         * @param y 下一时刻 y 坐标
         * @return 下一时刻 x 坐标
         */
        public float f(float y) {
            return a * (y - h) * (y - h) + c;
        }
    }
}

