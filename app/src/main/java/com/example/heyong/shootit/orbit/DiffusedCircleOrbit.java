package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseLittlePointBullet;
import com.example.heyong.shootit.util.LifeManager;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;

/**
 * Created by aluprayz on 2017/6/22.
 */

public class DiffusedCircleOrbit extends BaseOrbitController {
    static String TAG = "DiffusedCircleOrbit";
    protected float start_x;
    protected float start_y;
    protected float prespeed_x;
    protected float prespeed_y;
    protected float speed_x;
    protected float speed_y;
    protected int appeartime;
double pi=3.1415926;

    public DiffusedCircleOrbit(float start_x, float start_y, float prespeed_x, float prespeed_y, int appeartime) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.prespeed_x = prespeed_x;
        this.prespeed_y = prespeed_y;
        this.appeartime = appeartime * 60;
    }

    @Override
    public void addItem(BaseItem item) {
        item.allowSelfMoving(false);
        super.addItem(item);
    }

    public void addItem1(float x, float y, float speedx, float speedy) {
        BaseLittlePointBullet b = new BaseLittlePointBullet();
        start_x = x;
        start_y = y;
        b.setPosition(x, y);
        b.speedX = speedx;
        b.speedY = speedy;
        b.setVisible(false);
        addItem(b);
    }

    public void init(){
        for(int i=0;i<8;i++){
            this.addItem1(start_x,start_y,(float)(Math.cos(pi/4*i)),(float)(Math.sin(pi/4*i)));
        }
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
                break;
            }
        }
        //ContinuousTapManager.getInstance().onGetTap(flag);
    }

    @Override
    public boolean isTouched(CGPoint point) {
        return true;

    }

    @Override
    public boolean canBeDestroyed() {
        if (appeartime < -1){
            int deadCount = 0;
            Iterator<BaseItem> iterator = items.iterator();
            while (iterator.hasNext()){
                BaseItem b = iterator.next();
                if (!b.getVisible())
                    deadCount++;
            }
            return deadCount == items.size();
        }
        return false;
    }


    int interval = 0;
    boolean flag = false;

    @Override
    public void onGetClock() {
        super.onGetClock();
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
                    iterator.remove();
                }
                continue;

            }
            if (flag) {
                if (item.getPosition().x < 0) item.speedX = Math.abs(item.speedX);
                if (item.getPosition().x > Config.WINDOW_WIDTH)
                    item.speedX = -Math.abs(item.speedX);
                if (item.getPosition().y < 0) item.speedY = Math.abs(item.speedY);
                if (item.getPosition().y > Config.WINDOW_HEIGHT)
                    item.speedY = -Math.abs(item.speedY);
                item.setPosition(item.getPosition().x + item.speedX, item.getPosition().y + item.speedY);
            } else {
                item.setPosition(item.getPosition().x + prespeed_x, item.getPosition().y + prespeed_y);

            }

        }
        interval++;
        if (interval == 180) flag = true;
    }
}
