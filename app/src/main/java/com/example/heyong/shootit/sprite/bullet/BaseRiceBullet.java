package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 *  米弹，bullet2 第五行
 */

public class BaseRiceBullet extends BaseItem {

    public static final int GRAY = 0;
    public static final int RED = 32;
    public static final int PINK = 32 * 2;
    public static final int BLUE = 32 * 3;
    public static final int SKY_BLUE = 32 * 4;
    public static final int GREEN = 32 * 5;
    public static final int YELLOW = 32 * 6;


//    public int radius = 5;
    public float rotate = 0;//旋转角

    public BaseRiceBullet(int color) {
        super(Config.Bullet2);
        this.radius = 5;
        setTextureRect(CGRect.make(color, 128, 32, 32), false);
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
    }
}
