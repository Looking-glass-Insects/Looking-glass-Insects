package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseBigBullet;
import com.example.heyong.shootit.util.ContinuousTapManager;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * 重力自由落体
 */

public class guidao2 extends BaseOrbitController {

    static String TAG = "GravityOrbitController";

    /**
     * 下落起始x坐标
     */
//    protected float start_x;
    /**
     * 判定宽度
     */
    protected  float b1 = Config.WINDOW_HEIGHT/2;
    protected  float a1 = Config.WINDOW_WIDTH/2;
    protected float width;

    protected float g = 0;//重力加速度

    protected int deadItemCount = 0;

    public guidao2(float width) {
        this.width = width;
    }

    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
    }

    public void addItem(float x,float y) {
        BaseBigBullet b = new BaseBigBullet(){
            @Override
            public void setVisible(boolean v) {
                super.setVisible(v);
                if (!v){
                    deadItemCount++;
                }
            }
        };
        b.setPosition(x, y);
        addItem(b);
    }


    @Override
    public int getZ() {
        return 12;
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        Iterator<BaseItem> iterator1 = items.iterator();
        boolean flag1 = false;
        while (iterator1.hasNext()) {
            BaseItem item1 = iterator1.next();
            if (item1.isTouched(point)) {
                item1.onHandleTouchEvent(point);
                flag1 = true;
                break;
            }
        }
        ContinuousTapManager.getInstance().onGetTap(flag1);
    }

    @Override
    public boolean isTouched(CGPoint point) {
//        if (Math.abs(point.x - start_x) > width / 2) {
//            return false;
//        } else {
        return true;

    }


    @Override
    public boolean canBeDestroyed() {
        //当所有item不可见时该轨道可销毁
        return deadItemCount >= getItemCount();
    }

    @Override
    public void onGetClock() {
//        super.onGetClock();
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (!item.getVisible())
                continue;
            CGPoint point = item.getPosition();
            if (point.x>Config.WINDOW_WIDTH&&point.y < -32) {
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
//                    iterator.remove();
                }
                continue;
            }
            item.speedY += g;
            item.speedX = 1.0f;

            item.setPosition(point.x+item.speedX, -b1*(point.x-a1)*(point.x-a1)/(a1*a1)+b1);

        }
    }

}
