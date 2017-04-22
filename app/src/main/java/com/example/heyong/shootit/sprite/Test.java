package com.example.heyong.shootit.sprite;

import com.example.heyong.shootit.Bullets;
import com.example.heyong.shootit.Config;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * Created by Heyong on 2017/4/20.
 */
@Deprecated
public class Test extends CCSprite {

    public Test() {
        super(Config.Bullet3);
        this.setTextureRect(CGRect.make(0, 0, 32, 32), false);
        this.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.runAnimate(Bullets.getInstance().getBullet());
    }


    public boolean isTouched(CGPoint point) {
        float x = point.x - this.position_.x;
        float y = point.y - this.position_.y;
        if (x * x + y * y >= 32 * 32)
            return false;
        else return true;
    }

    public void runAnimate(CCAction animate) {
        this.runAction(animate);
    }

}
