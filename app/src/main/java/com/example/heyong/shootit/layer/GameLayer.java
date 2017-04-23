package com.example.heyong.shootit.layer;

import android.util.Log;
import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.orbit.BaseOrbitController;
import com.example.heyong.shootit.sprite.item.BombEffect;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 主游戏界面
 */

public class GameLayer extends BaseLayer {

    static final String TAG = "GameLayer";
    protected List<BaseOrbitController> orbits = new ArrayList<>(10);

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
                orbit.onDestroy();
                Log.d(TAG, "dead");
            } else {
                orbit.onGetClock();
            }
        }
        bombEffect.onGetClock();
    }

    public void addOrbits(BaseOrbitController orbitController) {
        this.orbits.add(orbitController);
        orbitController.setParent(this);
    }


    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        int count = event.getPointerCount();
        float x = 0;
        float y = 0;
        for (int i = 0; i < count; i++) {
            x = event.getX(i);
            y = event.getY(i);
            CGPoint p1 = CGPoint.ccp(x, y);
            CGPoint p2 = CCDirector.sharedDirector().convertToGL(p1);
            for (BaseOrbitController orbit : orbits) {
                if (orbit.isTouched(p2)) {
                    orbit.onHandleTouchEvent(p2);
                    break;
                }
            }
        }

        //test
        //loadFreezeEffect();
        loadBombEffect();
        return true;
    }


    /**
     * 为所有子弹加冰冻效果
     */
    protected void loadFreezeEffect() {
        for (BaseOrbitController controller : orbits) {
            controller.onGetFreezeSpellCard();
        }
    }

    /**
     * 消弹
     */
    protected BombEffect bombEffect;

    protected void loadBombEffect() {
        if (bombEffect == null){
            prepareBombEffect();
        }
        bombEffect.fresh();
        CGPoint point = CGPoint.ccp(Config.WINDOW_WIDTH / 2,Config.WINDOW_HEIGHT / 2);
        bombEffect.setPosition(point);
        for (BaseOrbitController controller : orbits) {
            controller.onGetBombSpellCard(point);
        }
    }

    protected void prepareBombEffect() {
        bombEffect = new BombEffect();
        this.addChild(bombEffect, bombEffect.getZ());
    }


    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
//        int count = event.getPointerCount();
//        for (int i = 0; i < count; i++) {
//            float x = event.getX(i);
//            float y = event.getY(i);
//            CGPoint p1 = CGPoint.ccp(x, y);
//            CGPoint point = CCDirector.sharedDirector().convertToGL(p1);
//            float dx = point.x - Config.handle_x;
//            float dy = point.y - Config.handle_y;
//            if (dx * dx + dy * dy <= Config.handle_radius * Config.handle_radius) {
//                float sin = (float) (dy / Math.sqrt(dx * dx + dy * dy));
//                float cos = (float) (dx / Math.sqrt(dx * dx + dy * dy));
//                handle.setPosition(Config.handle_x + Config.handle_radius * cos, Config.handle_y + Config.handle_radius * sin);
//                if (Math.abs(dx) > Math.abs(dy)) {
//                    if (dx>0){
//                        player.onMove(BasePlayer.RIGHT);
//                    }else {
//                        player.onMove(BasePlayer.LEFT);
//                    }
//                    //向x轴
//                } else {
//                    if (dy>0){
//                        player.onMove(BasePlayer.UP);
//                    }else {
//                        player.onMove(BasePlayer.DOWN);
//                    }
//                }
//            }
//        }
        return true;
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        //handle.setPosition(Config.handle_x, Config.handle_y);
        //player.onMove(BasePlayer.NO_ACTION);
        return super.ccTouchesEnded(event);
    }
}
