package com.example.heyong.shootit.layer;

import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.orbit.RotaryCircleOrbit;
import com.example.heyong.shootit.orbit.guidao1;
import com.example.heyong.shootit.orbit.guidao2;
import com.example.heyong.shootit.sprite.bg.Bg;
import com.example.heyong.shootit.util.ContinuousTapManager;
import com.example.heyong.shootit.util.Util;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;

/**
 * 第一关
 */

public class Stage1 extends GameLayer {
    protected static int TAG_NEXT_STAGE = 0;
    protected float b1 = Config.WINDOW_HEIGHT / 2;
    protected float a1 = Config.WINDOW_WIDTH / 2;
    protected double pi = 3.1415926;
    protected boolean isFin = false;


    public Stage1() {
        super();
        load();
    }

    protected void load() {
        guidao1 guidao = new guidao1(128);
        this.addOrbits(guidao);
        guidao2 guidao20 = new guidao2(128);
        this.addOrbits(guidao20);

        RotaryCircleOrbit a = new RotaryCircleOrbit(Config.WINDOW_WIDTH / 2, b1 * 2, 0.0f, -b1 / 120, 1.0f, 0, 3, 4, 1);
        a.setParent(this);
        this.addOrbits(a,1000);
        for (int i = 0; i < 8; i++) {
            a.addItem1(Config.WINDOW_WIDTH / 2, b1 * 2, (float) (1.0f * Math.cos(pi / 4 * i)), (float) (1.0f * Math.sin(pi / 4 * i)), i);
        }


        RotaryCircleOrbit b = new RotaryCircleOrbit(Config.WINDOW_WIDTH / 2, b1 * 2+32, 0.0f, -b1 / 360, 1.0f, 0, 3, 4, 1);
        b.setParent(this);
        this.addOrbits(b,2000);
        for (int i = 0; i < 8; i++) {
            b.addItem1(Config.WINDOW_WIDTH / 2, b1 * 2, (float) (1.0f * Math.cos(pi / 4 * i)), (float) (1.0f * Math.sin(pi / 4 * i)), i);
        }



        RotaryCircleOrbit c = new RotaryCircleOrbit(Config.WINDOW_WIDTH / 2, b1 * 2, 0.0f, -b1 / 180, 1.0f, 0, 3, 4, 1);
        c.setParent(this);
        this.addOrbits(c,3000);


        for (int i = 0; i < 8; i++) {
            c.addItem1(Config.WINDOW_WIDTH / 2, b1 * 2, (float) (1.0f * Math.cos(pi / 4 * i)), (float) (1.0f * Math.sin(pi / 4 * i)), i);
        }
        for (int i = 0; i < 20; i++) {
            guidao.addItem(Config.WINDOW_WIDTH + i * 32, b1 * (Config.WINDOW_WIDTH + i * 32 - a1) * (Config.WINDOW_WIDTH + i * 32 - a1) / (a1 * a1) + b1);
            guidao20.addItem(-32 - i * 64, -b1 * (Config.WINDOW_WIDTH + i * 64 - a1) * (Config.WINDOW_WIDTH + i * 64 - a1) / (a1 * a1) + b1);
        }


    }

    /**
     * 回调
     *
     * @param dt
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        if (this.orbits.size() == 0 && !isFin && !gameOver) {
            isFin = true;
            showClear();
        }
    }

    protected void showClear() {
        CCLabel singleStart = CCLabel.makeLabel("Next Stage", "Roboto_Thin.ttf", 24);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        singleStart.setTag(TAG_NEXT_STAGE);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(singleStart, 99);
    }

    /**
     * 跳转到下一场景
     */
    protected void nextStage() {
        CCScene scene = CCScene.node();
        Stage2 gameLayer = new Stage2();
        scene.addChild(Bg.getBg(1));
        scene.addChild(gameLayer);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
        ContinuousTapManager.getInstance().unregisterGameLayer();
    }


    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        if (isFin && !gameOver) {
            if (Util.isClicke(event, this, this.getChildByTag(TAG_NEXT_STAGE))) {
                nextStage();
            }
            return true;
        } else {
            return super.ccTouchesBegan(event);
        }
    }
}
