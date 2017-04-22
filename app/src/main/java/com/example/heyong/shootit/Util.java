package com.example.heyong.shootit;

import android.view.MotionEvent;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * Created by Heyong on 2017/4/22.
 */

public class Util {

    public static boolean isClicke(MotionEvent event, CCLayer layer, CCNode node) {
        CGPoint point = layer.convertTouchToNodeSpace(event);
        return CGRect.containsPoint(node.getBoundingBox(), point);
    }
}
