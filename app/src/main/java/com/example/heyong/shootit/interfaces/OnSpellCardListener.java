package com.example.heyong.shootit.interfaces;

import org.cocos2d.types.CGPoint;

/**
 * 响应各种技能
 */

public interface OnSpellCardListener {
    int BOMB_RADIUS = 128;

    /**
     * 响应冰冻效果
     */
    void onGetFreezeSpellCard();

    /**
     * 消弹
     */
    void onGetBombSpellCard(CGPoint point);
}
