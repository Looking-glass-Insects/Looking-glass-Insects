package com.example.heyong.shootit.sprite.bullet;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;

/**
 * 带有小尾巴的弹， bullet3 第5，6行
 */

public class BaseTailBullet extends BaseItem {

    public static final int RED = 128;
    public static final int PURPLE = 160;
    public int radius = 10;

    public BaseTailBullet(int color) {
        super(Config.Bullet3);
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
