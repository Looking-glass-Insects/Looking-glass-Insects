package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.ScoreManager;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.Random;

/**
 * 大光晕弹,bullet4
 */

public  class BaseBigLightBullet extends BaseItem {

    public static final int GRAY = 0;
    public static final int RED = 64;
    public static final int PINK = 64 * 2;
    public static final int BLUE = 64 * 3;
    public static final int SKY_BLUE = 0;
    public static final int GREEN = 64;
    public static final int YELLOW = 64 * 2;
    public static final int SILVER = 64 * 3;

    public BaseBigLightBullet(int color) {
        super(Config.Bullet4);
        this.radius = 16;
        if (color == GRAY || color == RED || color == PINK || color == BLUE)
            setTextureRect(CGRect.make(color, 0, 64, 64), false);
        else setTextureRect(CGRect.make(color, 64, 64, 64), false);
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
        ScoreManager.getInstance().onGetScore();
    }

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
