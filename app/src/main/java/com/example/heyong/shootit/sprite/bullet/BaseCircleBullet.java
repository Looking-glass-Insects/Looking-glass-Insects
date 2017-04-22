package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.Random;

/**
 * bullet2 第二行 圆弹
 */

public class BaseCircleBullet extends BaseItem {
    public static final int GRAY = 0;
    public static final int RED = 32;
    public static final int PINK = 32 * 2;
    public static final int BLUE = 32 * 3;
    public static final int SKY_BLUE = 32 * 4;
    public static final int GREEN = 32 * 5;
    public static final int YELLOW = 32 * 6;


    public BaseCircleBullet(int color) {
        super(Config.Bullet2);
        this.radius = 15;
        setTextureRect(CGRect.make(color, 32, 32, 32), false);
    }

//    @Override
//    public boolean isTouched(CGPoint point) {
//        float dx = point.x - this.position_.x;
//        float dy = point.y - this.position_.y;
//        return dx * dx + dy * dy <= (radius - Config.USER_RADIUS) * (radius - Config.USER_RADIUS);
//    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
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
