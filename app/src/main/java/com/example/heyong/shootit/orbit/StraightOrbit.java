package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseBigBullet;
import com.example.heyong.shootit.sprite.bullet.BaseLittlePointBullet;
import com.example.heyong.shootit.util.ContinuousTapManager;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;
import java.util.Random;


/**
 * Created by aluprayz on 2017/5/30.
 */

public class StraightOrbit extends BaseOrbitController {

    static String TAG = "StraightOrbit";
    protected float start_x;
    protected float start_y;

    protected float seita = 0;
    protected double pi = 3.1415926;
    private Random random = new Random();
    /**
     * 判定宽度
     */
    protected float width;

    protected float g = 0;//重力加速度

    protected int deadItemCount = 0;

    public StraightOrbit() {


    }

    @Override
    public void addItem(BaseItem item) {
        super.addItem(item);
    }

    public void addItem1(float x, float y) {
        BaseLittlePointBullet b = new BaseLittlePointBullet(BaseLittlePointBullet.RED) {
            @Override
            public void setVisible(boolean v) {
                super.setVisible(v);
                if (!v) {
                    deadItemCount++;
                }
            }
        };
        start_x = x;
        start_y = y;
        b.setPosition(x, y);
        b.speedY = 0.0f;
        b.speedX = 0.0f;
        addItem(b);
    }

    public void addItem(float y) {
        BaseBigBullet b = new BaseBigBullet(BaseBigBullet.RED) {
            @Override
            public void setVisible(boolean v) {
                super.setVisible(v);
                if (!v) {
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
        return true;

    }


    @Override
    public boolean canBeDestroyed() {
        //当所有item不可见时该轨道可销毁
        return deadItemCount >= getItemCount();
    }



    int interval = 0;
    boolean flag = true;

    @Override
    public void onGetClock() {

        int p=0;
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (!item.getVisible()) {
                if(p!=items.size())p++;
                else p=0;
                continue;
            }
            CGPoint point = item.getPosition();
            if (point.y < -32) {
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
                    iterator.remove();
                }
                continue;
            }
            if (flag) {
                for (int i = 0; i < items.size(); i++)
                    if (i * 60 == interval) {
                        items.get(i).speedY = 5.0f;
                        items.get(i+items.size()/2).speedX=5.0f;
                    }
            }
            if (item.getPosition().y < 30) {
                item.speedY = -3.0f;
            }
            else if(item.getPosition().y > Config.WINDOW_HEIGHT){
                item.speedY = 5.0f;
            }
            if (item.getPosition().x > (Config.WINDOW_WIDTH-10)) {
                item.speedX = -3.0f;
            }
            else if(item.getPosition().x < 0){
                item.speedX = 5.0f;
            }
            item.setPosition(item.getPosition().x+item.speedX, item.getPosition().y - item.speedY);



            if (interval > 300) {
                flag = false;
            }
            if (flag)
                interval++;
            if(p!=items.size())p++;
            else p=0;

        }
    }
}
