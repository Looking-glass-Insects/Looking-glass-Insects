package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

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
 * 音符弹
 */

public  class BaseMusicBullet extends BaseItem {

    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final int PINK = 2;
    public static final int BLUE = 3;
    private ArrayList<CCSpriteFrame> frames = new ArrayList<>(3);

    public BaseMusicBullet(int color) {
        super(Config.Bullet5);
        this.radius = 5;
        CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(Config.Bullet5);

        CCSpriteFrame frame0 = null;
        CCSpriteFrame frame1 = null;
        CCSpriteFrame frame2 = null;
        if (color == RED) {
            setTextureRect(CGRect.make(0, 0, 32, 32), false);
            frame0 = CCSpriteFrame.frame(texture, CGRect.make(0, 0, 32, 32), CGPoint.ccp(0, 0));
            frame1 = CCSpriteFrame.frame(texture, CGRect.make(32, 0, 32, 32), CGPoint.ccp(0, 0));
            frame2 = CCSpriteFrame.frame(texture, CGRect.make(64, 0, 32, 32), CGPoint.ccp(0, 0));
        } else if (color == BLUE) {
            setTextureRect(CGRect.make(96, 0, 32, 32), false);
            frame0 = CCSpriteFrame.frame(texture, CGRect.make(96, 0, 32, 32), CGPoint.ccp(0, 0));
            frame1 = CCSpriteFrame.frame(texture, CGRect.make(96 + 32, 0, 32, 32), CGPoint.ccp(0, 0));
            frame2 = CCSpriteFrame.frame(texture, CGRect.make(96 + 64, 0, 32, 32), CGPoint.ccp(0, 0));
        } else if (color == YELLOW) {
            setTextureRect(CGRect.make(0, 32, 32, 32), false);
            frame0 = CCSpriteFrame.frame(texture, CGRect.make(0, 32, 32, 32), CGPoint.ccp(0, 0));
            frame1 = CCSpriteFrame.frame(texture, CGRect.make(32, 32, 32, 32), CGPoint.ccp(0, 0));
            frame2 = CCSpriteFrame.frame(texture, CGRect.make(64, 32, 32, 32), CGPoint.ccp(0, 0));
        } else {
            setTextureRect(CGRect.make(96, 32, 32, 32), false);
            frame0 = CCSpriteFrame.frame(texture, CGRect.make(96, 32, 32, 32), CGPoint.ccp(0, 0));
            frame1 = CCSpriteFrame.frame(texture, CGRect.make(96 + 32, 32, 32, 32), CGPoint.ccp(0, 0));
            frame2 = CCSpriteFrame.frame(texture, CGRect.make(96 + 64, 32, 32, 32), CGPoint.ccp(0, 0));
        }
        frames.add(frame0);
        frames.add(frame1);
        frames.add(frame2);
        CCAnimation animation = CCAnimation.animation("", 0.3f, frames);
        CCAnimate animate = CCAnimate.action(animation);
        CCAction action = CCRepeatForever.action(animate);
        this.runAction(action);
    }

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
