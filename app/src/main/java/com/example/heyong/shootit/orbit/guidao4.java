package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseLittlePointBullet;
import com.example.heyong.shootit.util.ContinuousTapManager;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * 重力自由落体
 */

public class guidao4 extends BaseOrbitController {

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

    public guidao4(float width) {
        this.width = width;
    }

    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
    }

    public void addItem(float x,float y) {
        BaseLittlePointBullet b = new BaseLittlePointBullet(){
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
            if (point.x >Config.WINDOW_WIDTH) {
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
//                    iterator.remove();
                }
                continue;
            }
            item.speedY=0.5f;
            item.speedX = 0.7f;
            item.setPosition(point.x+item.speedX,-4*b1*(point.x-(3*a1)/2)*(point.x-(3*a1)/2)/(a1*a1)+2*b1);
//            item.setPosition(point.x,point.y);
        }
    }

}
