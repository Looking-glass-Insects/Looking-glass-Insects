package com.example.heyong.shootit.layer;

import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.Util;
import com.example.heyong.shootit.orbit.GravityOrbitController;
import com.example.heyong.shootit.sprite.bg.Bg;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;

/**
 * 初始界面
 */

public class StartLayer extends BaseLayer {

    private static final int TAG_START_SINGLE = 0;
    private static final int TAG_START_ONLINE = 1;
    private static final int TAG_INFO = 2;


    public StartLayer() {
        this.setIsTouchEnabled(true);
        setupStrings();
    }

    private void setupStrings() {
        CCLabel singleStart = CCLabel.makeLabel("单人游戏", "Roboto_Thin.ttf", 64);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 0));//初始值
        singleStart.setTag(TAG_START_SINGLE);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(singleStart, 1);
    }


    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        if (Util.isClicke(event, this, this.getChildByTag(TAG_START_SINGLE))) {
            setupGameLayer();
        }
        return super.ccTouchesEnded(event);
    }

    /**
     * 开启游戏界面
     */
    private void setupGameLayer() {
        CCScene scene = CCScene.node();
        Stage1 stage1 = new Stage1();
        scene.addChild(Bg.getBg(1));
        scene.addChild(stage1);

        GravityOrbitController controller = new GravityOrbitController(Config.WINDOW_WIDTH / 3, 128);
        GravityOrbitController controller2 = new GravityOrbitController(Config.WINDOW_WIDTH * 2 / 3, 128);


        stage1.addOrbits(controller);
        stage1.addOrbits(controller2);

        for (int i = 0; i < 5; i++) {
            controller.addItem(Config.WINDOW_HEIGHT + i * 64);
            controller2.addItem(Config.WINDOW_HEIGHT + i * 64);
        }

        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
    }

}
