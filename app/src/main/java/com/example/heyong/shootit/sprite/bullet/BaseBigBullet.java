package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.ScoreManager;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.Random;

/**
 * 大弹 bullet2.png 最后一行
 */

public class BaseBigBullet extends BaseItem {
    public static final int RED = 0;
    public static final int BULE = 64;
    public static final int GREEN = 128;
    public static final int YELLOW = 192;


    public BaseBigBullet(int color) {
        super(Config.Bullet2);
        this.radius = 24;
        setTextureRect(CGRect.make(color, 192, 64, 64), false);
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
        ScoreManager.getInstance().onGetScore();
    }
//
    @Override
    protected void initFreezeEffort() {
        freezeEffort = new CCSprite(Config.Ice);
        freezeEffort.setOpacity(192);
        freezeEffort.setPosition(32,32);
        freezeEffort.setTextureRect(CGRect.make(160, 160, 96, 96), false);
        freezeEffort.setRotation(new Random().nextInt(360));
        freezeEffort.setVisible(false);
        super.initFreezeEffort();
    }
}
