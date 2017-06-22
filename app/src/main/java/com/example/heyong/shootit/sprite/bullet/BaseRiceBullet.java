package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.ScoreManager;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.Random;

/**
 *  米弹，bullet2 第五行
 */

public  class BaseRiceBullet extends BaseItem {

    public static final int GRAY = 0;
    public static final int RED = 32;
    public static final int PINK = 32 * 2;
    public static final int BLUE = 32 * 3;
    public static final int SKY_BLUE = 32 * 4;
    public static final int GREEN = 32 * 5;
    public static final int YELLOW = 32 * 6;



    public float rotate = 0;//旋转角

    public BaseRiceBullet(int color) {
        super(Config.Bullet2);
        this.radius = 5;
        setTextureRect(CGRect.make(color, 128, 32, 32), false);
    }

    public BaseRiceBullet() {
        super(Config.Bullet2);
        this.radius = 5;
        int i = new Random().nextInt(7);
        setTextureRect(CGRect.make(i*32, 64, 32, 32), false);
    }

//    @Override
//    public boolean isTouched(CGPoint point) {
//        float dx = point.x - this.position_.x;
//        float dy = point.y - this.position_.y;
//        return dx * dx + dy * dy <= radius * radius;
//    }

    @Override
    public void setRotation(float rot) {
        super.setRotation(rot);
        this.rotate = rot;
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
        ScoreManager.getInstance().onGetScore(3);
    }


    @Override
    protected void initFreezeEffort() {
        freezeEffort = new CCSprite(Config.Ice);
        freezeEffort.setOpacity(192);
        freezeEffort.setPosition(16,16);
        freezeEffort.setTextureRect(CGRect.make(0, 0, 64, 64), false);
        freezeEffort.setRotation(new Random().nextInt(360));
        freezeEffort.setVisible(false);
        super.initFreezeEffort();
    }
}
