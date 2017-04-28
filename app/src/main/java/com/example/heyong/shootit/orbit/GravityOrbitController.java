package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseBigBullet;
import com.example.heyong.shootit.util.ContinuousTapManager;
import com.example.heyong.shootit.util.LifeManager;

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
    protected float start_x;
    /**
     * 判定宽度
     */
    protected float width;

    protected float g = 0;//重力加速度

    protected int deadItemCount = 0;

    public GravityOrbitController(float start_x, float width) {
        this.start_x = start_x;
        this.width = width;
    }

    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
    }

    public void addItem(float y) {
        BaseBigBullet b = new BaseBigBullet(BaseBigBullet.RED){
            @Override
            public void setVisible(boolean v) {
                super.setVisible(v);
                if (!v){
                    deadItemCount++;
                }
            }
        };
        b.setPosition(start_x, y);
        addItem(b);
    }


    @Override
    public int getZ() {
        return 11;
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        Iterator<BaseItem> iterator = items.iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (item.isTouched(point)) {
                item.onHandleTouchEvent(point);
                flag = true;
                break;
            }
        }
        ContinuousTapManager.getInstance().onGetTap(flag);
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
            if (!item.getVisible())
                continue;
            CGPoint point = item.getPosition();
            if (point.y < -32) {
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
                    iterator.remove();
                }
                continue;
            }
            item.speedY += g;
            item.setPosition(start_x, point.y - item.speedY);
        }
    }

}
