package com.example.heyong.shootit.sprite.player;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.util.ArrayList;

/**
 * Created by Heyong on 2017/4/21.
 */
@Deprecated
public class BasePlayer extends BaseItem {

    private CCSprite effect;


    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int NO_ACTION = 4;


    private float speed = 3.0f;
    private CCAnimate rightAnimate;
    private CCAnimate leftAnimate;
    private CCAnimate goNormal;
    private boolean isOnRight = false;
    private boolean isOnLeft = false;

    public BasePlayer() {
        super(Config.LING_MENG);
        this.radius = 5;
        setTextureRect(CGRect.make(0, 0, 32, 48), false);
        effect = new CCSprite(Config.EFFECT);
        effect.setTextureRect(CGRect.make(0, 0, 64, 64), false);
        effect.setPosition(16, 24);
        this.addChild(effect, 2);
        this.setPosition(Config.start_x, Config.start_y);
        buildRightAnimate();
        buildLeftAnimate();
    }

    private void buildLeftAnimate() {
        CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(Config.LING_MENG);
        ArrayList<CCSpriteFrame> frames = new ArrayList<>(8);
        CCSpriteFrame frame0 = CCSpriteFrame.frame(texture, CGRect.make(0, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame1 = CCSpriteFrame.frame(texture, CGRect.make(32, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame2 = CCSpriteFrame.frame(texture, CGRect.make(32 * 2, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame3 = CCSpriteFrame.frame(texture, CGRect.make(32 * 3, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame4 = CCSpriteFrame.frame(texture, CGRect.make(32 * 4, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame5 = CCSpriteFrame.frame(texture, CGRect.make(32 * 5, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame6 = CCSpriteFrame.frame(texture, CGRect.make(32 * 6, 48, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame7 = CCSpriteFrame.frame(texture, CGRect.make(32 * 7, 48, 32, 48), CGPoint.ccp(0, 0));
        frames.add(frame0);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        frames.add(frame4);
        frames.add(frame5);
        frames.add(frame6);
        frames.add(frame7);
        CCAnimation animation = CCAnimation.animation("", 0.02f, frames);
        leftAnimate = CCAnimate.action(animation, false);
    }

    private void buildRightAnimate() {
        CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(Config.LING_MENG);
        ArrayList<CCSpriteFrame> frames = new ArrayList<>(8);
        CCSpriteFrame frame0 = CCSpriteFrame.frame(texture, CGRect.make(0, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame1 = CCSpriteFrame.frame(texture, CGRect.make(32, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame2 = CCSpriteFrame.frame(texture, CGRect.make(32 * 2, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame3 = CCSpriteFrame.frame(texture, CGRect.make(32 * 3, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame4 = CCSpriteFrame.frame(texture, CGRect.make(32 * 4, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame5 = CCSpriteFrame.frame(texture, CGRect.make(32 * 5, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame6 = CCSpriteFrame.frame(texture, CGRect.make(32 * 6, 96, 32, 48), CGPoint.ccp(0, 0));
        CCSpriteFrame frame7 = CCSpriteFrame.frame(texture, CGRect.make(32 * 7, 96, 32, 48), CGPoint.ccp(0, 0));
        frames.add(frame0);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);
        frames.add(frame4);
        frames.add(frame5);
        frames.add(frame6);
        frames.add(frame7);
        CCAnimation animation = CCAnimation.animation("", 0.02f, frames);
        rightAnimate = CCAnimate.action(animation, false);
        ArrayList<CCSpriteFrame> f = new ArrayList<>(1);
        f.add(frame0);
        animation = CCAnimation.animation("",0.02f,f);
        goNormal = CCAnimate.action(animation,false);
    }

    @Override
    public void setPosition(CGPoint pos) {
        super.setPosition(pos);
    }


    public void onMove(int direction) {
        CGPoint point = this.getPosition();
        if (direction == UP) {
            this.setPosition(point.x, point.y + speed);
        } else if (direction == DOWN) {
            this.setPosition(point.x, point.y - speed);
        } else if (direction == LEFT) {
            this.setPosition(point.x - speed, point.y);
            isOnRight = false;
            if (!isOnLeft){
                this.runAction(leftAnimate);
                isOnLeft = true;
            }
        } else if (direction == RIGHT) {
            this.setPosition(point.x + speed, point.y);
            isOnLeft = false;
            if (!isOnRight){
                this.runAction(rightAnimate);
                isOnRight = true;
            }
        } else {
            this.runAction(goNormal);
            isOnLeft = false;
            isOnRight = false;
        }
    }


    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
    }


}
