package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * 心弹
 */
@Deprecated
public class BaseHeartBullet extends BaseItem {
    public static final int GRAY = 0;
    public static final int RED = 32 ;
    public static final int PINK = 32 * 2;
    public static final int BLUE = 32 * 3;
    public static final int SKY_BLUE = 32 * 4;
    public static final int GREEN = 32 * 5;
    public static final int YELLOW = 32 * 6;




    public BaseHeartBullet(int color) {
        super(Config.Bullet3);
        this.radius = 9;
        setTextureRect(CGRect.make(color, 0, 32, 32), false);
    }

//    @Override
//    public boolean isTouched(CGPoint point) {
//        float dx = point.x - this.position_.x;
//        float dy = point.y - this.position_.y;
//        return dx * dx + dy * dy <= radius * radius;
//    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
    }
}
