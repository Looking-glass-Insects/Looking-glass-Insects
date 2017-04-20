package com.example.heyong.shootit.layer;

import android.util.Log;
import android.view.MotionEvent;

import com.example.heyong.shootit.orbit.BaseOrbitController;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  主游戏界面
 */

public class GameLayer extends BaseLayer {
    static final String TAG = "GameLayer";


    private List<BaseOrbitController> orbits = new ArrayList<>(10);

    public GameLayer() {
        this.setIsTouchEnabled(true);
        this.scheduleUpdate();
    }

    /**
     * 不断回调,每帧一次
     */
    public void update(float dt) {
        Iterator<BaseOrbitController> iterator = orbits.iterator();
        while (iterator.hasNext()) {
            BaseOrbitController orbit = iterator.next();
            if (orbit.canBeDestroyed()) {
                iterator.remove();
            } else {
                orbit.onGetClock();
            }
        }
    }

    public void addOrbits(BaseOrbitController orbitController) {
        this.orbits.add(orbitController);
        orbitController.setParent(this);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        CGPoint p1 = CGPoint.ccp(x, y);
        CGPoint p2 = CCDirector.sharedDirector().convertToGL(p1);
        Log.d(TAG, "p2.x: " + p2.x + ",p2.y: " + p2.y);//左下角原点
        for (BaseOrbitController orbit : orbits) {
            if (orbit.isTouched(p2)) {
                orbit.onHandleTouchEvent(p2);
                break;
            }
        }
        return super.ccTouchesBegan(event);
    }

    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        return super.ccTouchesMoved(event);
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        return super.ccTouchesEnded(event);
    }
}
