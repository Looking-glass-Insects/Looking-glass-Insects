package com.example.heyong.shootit.layer;

import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.MainActivity;
import com.example.heyong.shootit.R;
import com.example.heyong.shootit.orbit.GravityOrbitController;
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

    protected boolean isFin = false;


    public Stage1() {
        super();
        load();
        MainActivity.getEngine().playSound(getContext(), R.raw.bgm1, true);
    }

    protected void load() {
        GravityOrbitController controller = new GravityOrbitController(Config.WINDOW_WIDTH / 3, 128);
        GravityOrbitController controller2 = new GravityOrbitController(Config.WINDOW_WIDTH * 2 / 3, 128);


        this.addOrbits(controller);
        this.addOrbits(controller2);

        for (int i = 0; i < 5; i++) {
            controller.addItem(Config.WINDOW_HEIGHT + i * 64);
            controller2.addItem(Config.WINDOW_HEIGHT + i * 64);
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
        if (this.orbits.size() == 0 && !isFin) {
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
        scene.addChild(Bg.getBg(2));
        scene.addChild(gameLayer);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
        ContinuousTapManager.getInstance().unregisterGameLayer();
    }


    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        if (isFin) {
            if (Util.isClicke(event, this, this.getChildByTag(TAG_NEXT_STAGE))) {
                nextStage();
            }
            return true;
        } else {
            return super.ccTouchesBegan(event);
        }
    }
}
