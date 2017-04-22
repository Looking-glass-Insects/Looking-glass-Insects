package com.example.heyong.shootit.sprite;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.OnHandleTouchEventListener;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * 基础类,包括子弹，人物
 */

public abstract class BaseItem extends CCSprite implements OnHandleTouchEventListener {


    protected CCSprite freezeEffort = null;
    public float speedY = 2f;
    public float speedX = 0f;

    protected int radius = 0;

    public BaseItem(String file) {
        super(file);
        this.getTexture().setAntiAliasTexParameters();
        initFreezeEffort();
    }

    protected void initFreezeEffort() {
        if (freezeEffort != null)
            this.addChild(freezeEffort);
    }

    /**
     * @return true if is touched
     */
    public boolean isTouched(CGPoint point) {
        float dx = point.x - this.position_.x;
        float dy = point.y - this.position_.y;
        return dx * dx + dy * dy <= (radius + Config.USER_RADIUS) * (radius + Config.USER_RADIUS);
    }


    public void setFrozen(boolean frozen) {
        if (freezeEffort == null) {
            throw new IllegalStateException("冰冻效果未添加");
        }
        if (frozen) {
            freezeEffort.setVisible(true);
        } else {
            freezeEffort.setVisible(false);
        }
    }

}
