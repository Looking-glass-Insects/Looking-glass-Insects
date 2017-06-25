package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseLightBullet;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * Created by aluprayz on 2017/6/23.
 */

public class RotaryCircleOrbit extends BaseOrbitController {
    static String TAG = "RotaryCircleOrbit";
    protected float start_x;
    protected float start_y;
    protected float circle_x;
    protected float circle_y;
    protected float radius;

    protected float prespeed_x;
    protected float prespeed_y;
    protected float speed;
    protected float speed_x;
    protected float speed_y;
    protected int appeartime;
    protected int changetime;
    protected int changetime2;
    protected int changestyle;
    protected int deadItemCount = 0;

    public RotaryCircleOrbit(float start_x, float start_y, float prespeed_x, float prespeed_y, float speed, int appeartime, int changetime, int changetime2, int changestyle) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.prespeed_x = prespeed_x;
        this.prespeed_y = prespeed_y;
        this.speed = speed;
        this.appeartime = appeartime * 60;
        this.changetime = changetime * 60;
        this.changetime2 = changetime2 * 60;
        circle_x = start_x + prespeed_x * this.changetime;
        circle_y = start_y + prespeed_y * this.changetime;
        radius = (this.changetime2 - this.changetime) * speed;
        this.changestyle = changestyle;
    }

    @Override
    public void addItem(BaseItem item) {

        super.addItem(item);
    }

    public void addItem1(float start_x, float start_y, float speedx, float speedy, int seita) {
        BaseLightBullet b = new BaseLightBullet() {
            @Override
            public void setVisible(boolean v) {
                super.setVisible(v);
                if (!v) {
                    deadItemCount++;
                }
            }
        };
        this.start_x = start_x;
        this.start_y = start_y;
        b.setPosition(start_x, start_y);
        b.speedX = speedx;
        b.speedY = speedy;

        b.seita = seita * 45;


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
       // ContinuousTapManager.getInstance().onGetTap(flag);
    }

    @Override
    public boolean isTouched(CGPoint point) {
        return true;

    }

    public boolean canBeDestroyed() {
        //当所有item不可见时该轨道可销毁
        return deadItemCount >= getItemCount();
    }

    int interval = 0;

    @Override
    public void onGetClock() {
        appeartime--;
        if (appeartime >= 0)
            return;
        else if (appeartime == -1) {
            Iterator<BaseItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                BaseItem b = iterator.next();
                b.setVisible(true);
            }
        }

        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (!item.getVisible()) {
                continue;
            }
            CGPoint point = item.getPosition();
            if (point.y < -32) {
                if (item.getVisible()) {
                    LifeManager.getInstance().subLife(1);
                    item.setVisible(false);
                    //iterator.remove();
                }
                continue;
            }
            if (interval < changetime) {
                item.setPosition(item.getPosition().x + prespeed_x, item.getPosition().y + prespeed_y);
            } else if (interval < changetime2) {
                if (item.getPosition().x < 0) item.speedX = Math.abs(item.speedX);
                if (item.getPosition().x > Config.WINDOW_WIDTH)
                    item.speedX = -Math.abs(item.speedX);
                if (item.getPosition().y < 0) item.speedY = Math.abs(item.speedY);
                if (item.getPosition().y > Config.WINDOW_HEIGHT)
                    item.speedY = -Math.abs(item.speedY);
                item.setPosition(item.getPosition().x + item.speedX, item.getPosition().y + item.speedY);
            } else if(interval<(changetime2+300)){
                item.setPosition((float) (circle_x + radius * Math.cos((double) (item.seita / 180 * 3.1415916))), (float) (circle_y + radius * Math.sin((double) (item.seita / 180 * 3.1415926))));
                if (item.seita < 360)
                    item.seita += 0.5;
                else item.seita = 0;

            }
            else{
                Iterator<BaseItem> iterator1 = items.iterator();
                while (iterator1.hasNext()) {
                    BaseItem c = iterator1.next();
                    c.setVisible(false);
                }
            }

        }
        interval++;
    }
}
