package com.example.heyong.shootit.sprite.item;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.util.SpellCardManager;

import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 *  item
 */

public class SpellItem extends BaseItem{

    public SpellItem() {
        super(Config.item);
        this.radius = 16;
        setTextureRect(CGRect.make(128, 0, 32, 32), false);
    }
    @Override
    public void onHandleTouchEvent(CGPoint point) {
        this.setVisible(false);
        SpellCardManager.getInstance().onGetSpellCard();
    }


}
