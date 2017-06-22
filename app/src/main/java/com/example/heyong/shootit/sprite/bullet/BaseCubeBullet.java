package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.ScoreManager;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.Random;

/**
 * 方块弹，bullet1 8行
 */

public class BaseCubeBullet extends BaseItem {
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

    public BaseCubeBullet() {
        super(Config.Bullet1);
        this.radius = 5;
        int i = new Random().nextInt(16);
        setTextureRect(CGRect.make(i * 16, 112, 16, 16), false);
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
        ScoreManager.getInstance().onGetScore(3);
    }

    @Override
    protected void initFreezeEffort() {
        freezeEffort = new CCSprite(Config.Ice);
        freezeEffort.setOpacity(192);
        freezeEffort.setPosition(8, 8);
        freezeEffort.setTextureRect(CGRect.make(0, 128, 32, 32), false);
        freezeEffort.setRotation(new Random().nextInt(360));
        freezeEffort.setVisible(false);
        super.initFreezeEffort();
    }
}
