package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.ScoreManager;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;
import java.util.Random;

/**
 * 带有小尾巴的弹， bullet3 第5，6行
 */

public class BaseTailBullet extends BaseItem {

    public static final int RED = 128;
    public static final int PURPLE = 160;

    public BaseTailBullet(int color) {
        super(Config.Bullet3);
        this.radius = 10;
        initByColor(color);
    }

    public BaseTailBullet() {
        super(Config.Bullet3);
        this.radius = 10;
        int i = new Random().nextInt(2);
        if (i == 0) {
            initByColor(RED);
        } else {
            initByColor(PURPLE);
        }
    }

    public void initByColor(int color) {
        setTextureRect(CGRect.make(0, color, 32, 32), false);
        CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(Config.Bullet3);
        ArrayList<CCSpriteFrame> frames = new ArrayList<>(4);
        CCSpriteFrame frame0 = CCSpriteFrame.frame(texture, CGRect.make(0, color, 32, 32), CGPoint.ccp(0, 0));
        CCSpriteFrame frame1 = CCSpriteFrame.frame(texture, CGRect.make(32, color, 32, 32), CGPoint.ccp(0, 0));
        CCSpriteFrame frame2 = CCSpriteFrame.frame(texture, CGRect.make(64, color, 32, 32), CGPoint.ccp(0, 0));
        CCSpriteFrame frame3 = CCSpriteFrame.frame(texture, CGRect.make(96, color, 32, 32), CGPoint.ccp(0, 0));
        frames.add(frame0);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        CCAnimation animation = CCAnimation.animation("", 0.2f, frames);
        CCAnimate animate = CCAnimate.action(animation);
        CCAction action = CCRepeatForever.action(animate);
        this.runAction(action);
    }

    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
        ScoreManager.getInstance().onGetScore(2);
    }

    @Override
    protected void initFreezeEffort() {
        freezeEffort = new CCSprite(Config.Ice);
        freezeEffort.setOpacity(192);
        freezeEffort.setPosition(16, 16);
        freezeEffort.setTextureRect(CGRect.make(0, 0, 64, 64), false);
        freezeEffort.setRotation(new Random().nextInt(360));
        freezeEffort.setVisible(false);
        super.initFreezeEffort();
    }
}
