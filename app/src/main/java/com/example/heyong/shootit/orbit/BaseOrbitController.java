package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.layers.CCLayer;

/**
 * Created by Heyong on 2017/4/20.
 */

public abstract class BaseOrbitController implements IOrbitController {
    protected CCLayer parent;

    public void addItem(BaseItem item) {
        assertParent();
        parent.addChild(item,getZ());
    }

    protected void assertParent() {
        if (parent == null) {
            throw new IllegalStateException("先调用setParent()");
        }
    }

    public void removeItem(BaseItem item) {
        assertParent();
        parent.removeChild(item, true);
    }

    public CCLayer getParent() {
        return parent;
    }

    public void setParent(CCLayer parent) {
        this.parent = parent;
    }
}
