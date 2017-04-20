package com.example.heyong.shootit.sprite;

import com.example.heyong.shootit.Config;

import org.cocos2d.types.CGRect;

/**
 * 大弹 bullet2.png 最后一行
 */

public abstract class BaseBigBullet extends BaseItem {
    public static final int RED = 0;
    public static final int BULE = 64;
    public static final int GREEN = 128;
    public static final int YELLOW = 192;

    public BaseBigBullet(int color) {
        super(Config.Bullet2);
        setTextureRect(CGRect.make(color, 194, 64, 64), false);
    }

}
