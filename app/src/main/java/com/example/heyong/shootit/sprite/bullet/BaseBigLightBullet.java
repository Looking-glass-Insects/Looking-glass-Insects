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

public class BaseBigLightBullet extends BaseItem {

    public static final int GRAY = 0;
    public static final int RED = 64;
    public static final int PINK = 64 * 2;
    public static final int BLUE = 64 * 3;
    public static final int SKY_BLUE = 0;
    public static final int GREEN = 64;
    public static final int YELLOW = 64 * 2;
    public static final int SILVER = 64 * 3;

    /**
     * 随机颜色
     */
    public BaseBigLightBullet() {
        super(Config.Bullet4);
        this.radius = 16;
        int i = new Random().nextInt(8);
        if (i == 0) {
            changeArtt(GRAY);
        } else if (i == 1) {
            changeArtt(RED);
        } else if (i == 2) {
            changeArtt(PINK);
        } else if (i == 3) {
            changeArtt(BLUE);
        } else if (i == 4) {
            changeArtt(SKY_BLUE);
        } else if (i == 5) {
            changeArtt(GREEN);
        } else if (i == 6) {
            changeArtt(YELLOW);
        } else {
            changeArtt(SILVER);
        }
    }


    public BaseBigLightBullet(int color) {
        super(Config.Bullet4);
        this.radius = 16;
        changeArtt(color);
    }

    public void changeArtt(int color) {
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
        freezeEffort.setPosition(32, 32);
        freezeEffort.setTextureRect(CGRect.make(160, 160, 96, 96), false);
        freezeEffort.setRotation(new Random().nextInt(360));
        freezeEffort.setVisible(false);
        super.initFreezeEffort();
    }
}
