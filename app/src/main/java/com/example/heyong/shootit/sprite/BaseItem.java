package com.example.heyong.shootit.sprite;

import com.example.heyong.shootit.OnHandleTouchEventListener;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * Created by Heyong on 2017/4/20.
 */

public abstract class BaseItem extends CCSprite implements OnHandleTouchEventListener{
//    /**
//     * 红色大弹 bullet2.png 最后一行第一个
//     */
//    public static final int BIG_BULLET_1 = 0;
//
//    public static BaseItem getItem(int type){
//        if (type == BIG_BULLET_1){
//            BaseItem baseItem = new BaseItem(Config.Bullet2);
//            baseItem.setTextureRect(CGRect.make(0, 194, 64, 64), false);
//            return baseItem;
//        }else return null;
//    }
//
    public BaseItem(String file) {
        super(file);
    }

    /**
     *
     * @return true if is touched
     */
    public abstract boolean isTouched(CGPoint point);
//
}
