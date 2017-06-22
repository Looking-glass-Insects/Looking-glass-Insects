package com.example.heyong.shootit.layer;

import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.sprite.bg.Bg;
import com.example.heyong.shootit.util.Util;

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
    private static final int TAG_START_ONLINE_SERVER = 1;
    private static final int TAG_START_ONLINE_CLIENT = 4;
    private static final int TAG_INFO = 2;

    public static final int TAG_ONLINE_SERVER = 233;
    public static final int TAG_ONLINE_CLINET = 234;

    private StartLayer() {
        this.setIsTouchEnabled(true);
        setupStrings();
    }

    public static StartLayer getStartLayer() {
        return Factory.startLayer;
    }

    private static class Factory {
        private static StartLayer startLayer = new StartLayer();
    }

    private void setupStrings() {
        CCLabel singleStart = CCLabel.makeLabel("单人游戏", "Roboto_Thin.ttf", 32);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 0));//初始值
        singleStart.setTag(TAG_START_SINGLE);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2 + 48);

        CCLabel onlineStartServer = CCLabel.makeLabel("建立主机", "Roboto_Thin.ttf", 32);
        onlineStartServer.setColor(ccColor3B.ccc3(255, 228, 0));//初始值
        onlineStartServer.setTag(TAG_START_ONLINE_SERVER);
        onlineStartServer.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);

        CCLabel onlineStartClient = CCLabel.makeLabel("连接主机", "Roboto_Thin.ttf", 32);
        onlineStartClient.setColor(ccColor3B.ccc3(255, 228, 0));//初始值
        onlineStartClient.setTag(TAG_START_ONLINE_CLIENT);
        onlineStartClient.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2 - 48);

        this.addChild(singleStart, 1);
        this.addChild(onlineStartServer, 1);
        this.addChild(onlineStartClient, 1);
    }


    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        if (Util.isClicke(event, this, this.getChildByTag(TAG_START_SINGLE))) {
            setupGameLayer();
        } else if (Util.isClicke(event, this, this.getChildByTag(TAG_START_ONLINE_SERVER))) {
            this.handler.sendEmptyMessage(TAG_ONLINE_SERVER);

            CCScene scene = CCScene.node();
            OnlineLayer layer = new OnlineLayer();


            scene.addChild(Bg.getBg(0));
            scene.addChild(layer);

            layer.setHandler(handler);
            CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
            CCDirector.sharedDirector().pushScene(transition);

        } else if (Util.isClicke(event, this, this.getChildByTag(TAG_START_ONLINE_CLIENT))) {
            this.handler.sendEmptyMessage(TAG_ONLINE_CLINET);

            CCScene scene = CCScene.node();
            OnlineLayer layer = new OnlineLayer();

            scene.addChild(Bg.getBg(0));
            scene.addChild(layer);

            layer.setHandler(handler);
            CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
            CCDirector.sharedDirector().pushScene(transition);
        }
        return super.ccTouchesEnded(event);
    }

    /**
     * 开启游戏界面
     */
    private void setupGameLayer() {
        CCScene scene = CCScene.node();
        Stage1 stage1 = new Stage1();
        scene.addChild(Bg.getBg(0));
        scene.addChild(stage1);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
    }

}
