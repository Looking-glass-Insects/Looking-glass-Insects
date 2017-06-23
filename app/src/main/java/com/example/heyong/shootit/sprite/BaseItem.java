package com.example.heyong.shootit.sprite;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.interfaces.OnClockGetListener;
import com.example.heyong.shootit.interfaces.OnHandleTouchEventListener;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * 基础类,包括子弹，人物
 */

public abstract class BaseItem extends CCSprite implements OnHandleTouchEventListener, OnClockGetListener {


    protected CCSprite freezeEffort = null;

    public float speedY = 0;
    public float speedX = 0;

    public float oldSpeedX = 0;
    public float oldSpeedY = 0;

    protected int id;


    protected int radius = 0;




    public BaseItem(String file) {
        super(file);
        this.getTexture().setAntiAliasTexParameters();
        initFreezeEffort();
    }

    protected void initFreezeEffort() {
        if (freezeEffort != null)
            this.addChild(freezeEffort);
    }
    //计时器
    public int timer;
    //复写方法
    public void f(){

    }


    public void replaceSpeed(float speedX,float speedY){
        this.oldSpeedY = this.speedY;
        this.oldSpeedX = this.speedX;
        this.speedX = speedX;
        this.speedY = speedY;
    }


    public void resumeSpeed(){
        this.speedX = this.oldSpeedX;
        this.speedY = this.oldSpeedY;
    }

    /**
     * @return true if is touched
     */
    public boolean isTouched(CGPoint point) {
        if (!this.getVisible())
            return false;
        float dx = point.x - this.position_.x;
        float dy = point.y - this.position_.y;
        return dx * dx + dy * dy <= (radius + Config.USER_RADIUS) * (radius + Config.USER_RADIUS);
    }

    protected int freezeLastTime = 0;

    /**
     * 处理item自身
     */
    @Override
    public void onGetClock() {
        if (freezeLastTime-- <= 0) {
            setFrozen(false);
        }
        CGPoint point = getPosition();
        setPosition(point.x + speedX, point.y + speedY);
    }

    public void loadFrozenEffert() {
        freezeLastTime = 3 * 60;// 3s
        setFrozen(true);
    }

    public int getRadius() {
        return radius;
    }

    protected void setFrozen(boolean frozen) {
        if (freezeEffort == null) {
            throw new IllegalStateException("冰冻效果未添加");
        }
        if (frozen) {
            if (freezeEffort.getVisible()) {
                //已经加载好了冰冻效果
                return;
            }
            freezeEffort.setVisible(true);
            this.oldSpeedX = speedX;
            this.oldSpeedY = speedY;
            this.speedX = 0;
            this.speedY = 0;
        } else {
            if (!freezeEffort.getVisible()) {
                return;
            }
            this.speedX = this.oldSpeedX;
            this.speedY = this.oldSpeedY;
            freezeEffort.setVisible(false);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
