package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * 方块弹，bullet1 8行
 */

public  class BaseCubeBullet extends BaseItem {
    public static final int GRAY = 0;
    public static final int RED = 16;
    public static final int PINK = 48;
    public static final int BLUE = 80;
    public static final int GREEN = 128 + 16 << 2;




    public BaseCubeBullet(int color) {
        super(Config.Bullet1);
        this.radius = 5;
        setTextureRect(CGRect.make(color, 112, 16, 16), false);
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
