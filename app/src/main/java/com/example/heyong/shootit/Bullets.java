package com.example.heyong.shootit;

import android.util.Log;

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
 * Created by Heyong on 2017/4/20.
 */

public class Bullets {

    static String TAG = "Bullets";
    private ArrayList<CCSpriteFrame> frames = new ArrayList<>();

    private Bullets() {
    }

    public static Bullets getInstance(){
        return BulletsFactory.instance;
    }


    public CCAction getBullet(){
        CCTexture2D texture = CCTextureCache.sharedTextureCache().addImage(Config.Bullet3);
        CCSpriteFrame frame0 = CCSpriteFrame.frame(texture, CGRect.make(32,0,32,32), CGPoint.ccp(0,0));
        CCSpriteFrame frame1 = CCSpriteFrame.frame(texture, CGRect.make(0,0,32,32), CGPoint.ccp(0,0));
        frames.add(frame0);
        frames.add(frame1);
        Log.d(TAG,""+frame0.getRect().size.getWidth());
        CCAnimation animation = CCAnimation.animation("", 0.2f, frames);
        CCAnimate animate = CCAnimate.action(animation);
        return CCRepeatForever.action(animate);
    }


    private static class BulletsFactory{
        private static final Bullets instance = new Bullets();
    }
}
