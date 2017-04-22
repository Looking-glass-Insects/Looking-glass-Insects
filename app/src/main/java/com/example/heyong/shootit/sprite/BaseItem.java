package com.example.heyong.shootit.sprite;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.OnHandleTouchEventListener;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * 基础类,包括子弹，人物
 */

public abstract class BaseItem extends CCSprite implements OnHandleTouchEventListener {

    public float speedY = 0.05f;
    public float speedX = 0f;

    protected int radius = 0;

    public BaseItem(String file) {
        super(file);
        this.getTexture().setAntiAliasTexParameters();
    }

    /**
     * @return true if is touched
     */
    public boolean isTouched(CGPoint point) {
        float dx = point.x - this.position_.x;
        float dy = point.y - this.position_.y;
        return dx * dx + dy * dy <= (radius + Config.USER_RADIUS) * (radius + Config.USER_RADIUS);
    }


}
