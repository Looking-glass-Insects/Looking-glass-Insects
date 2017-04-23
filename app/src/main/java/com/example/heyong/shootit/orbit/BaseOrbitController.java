package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.OnSpellCardListener;
import com.example.heyong.shootit.sprite.BaseItem;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.types.CGPoint;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础轨道类
 */

public abstract class BaseOrbitController implements IOrbitController, OnSpellCardListener {
    protected CCLayer parent;
    protected List<BaseItem> items = new LinkedList<>();


    /**
     * 添加item
     *
     * @param item
     */
    public void addItem(BaseItem item) {
        assertParent();
        parent.addChild(item, getZ());
        items.add(item);
    }

    protected void assertParent() {
        if (parent == null) {
            throw new IllegalStateException("先调用setParent()");
        }
    }

    /**
     * 移除item
     *
     * @param item
     */
    public void removeItemFromParent(BaseItem item) {
        assertParent();
        parent.removeChild(item, true);
    }

    public CCLayer getParent() {
        return parent;
    }


    /**
     * 销毁该轨道，将item从父容器移除
     */
    @Override
    public void onDestroy() {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            removeItemFromParent(item);
            iterator.remove();
        }
        items = null;
        parent = null;
    }

    public void setParent(CCLayer parent) {
        this.parent = parent;
    }

    /**
     * 调用所有item的 onGetClock()
     */
    @Override
    public void onGetClock() {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            item.onGetClock();
        }
    }

    /**
     * 响应冰冻效果
     */
    @Override
    public void onGetFreezeSpellCard() {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            item.loadFrozenEffert();
        }
    }

    /**
     * 响应消弹效果
     */
    @Override
    public void onGetBombSpellCard(CGPoint point) {
        Iterator<BaseItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            BaseItem item = iterator.next();
            if (!item.getVisible()) {
                continue;
            }
            CGPoint itemPoint = item.getPosition();
            float dx = point.x - itemPoint.x;
            float dy = point.y - itemPoint.y;
            boolean in = dx * dx + dy * dy <= (item.getRadius() + OnSpellCardListener.BOMB_RADIUS) * (item.getRadius() + OnSpellCardListener.BOMB_RADIUS);
            if (in) {
                item.setVisible(false);
            }
        }
    }
}
