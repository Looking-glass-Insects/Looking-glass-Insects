package com.example.heyong.shootit.layer;

import com.example.heyong.shootit.MainActivity;
import com.example.heyong.shootit.R;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.transitions.CCFadeTransition;

/**
 * 第三关
 */

public class Stage3 extends Stage1 {

    public Stage3() {
        MainActivity.getEngine().playSound(getContext(), R.raw.bgm3, true);
    }

    @Override
    protected void showClear() {
        super.showClear();
    }

    @Override
    protected void nextStage() {
        MainActivity.getEngine().pauseSound();
        CCScene scene = CCScene.node();
        StartLayer startLayer = new StartLayer();
        scene.addChild(startLayer);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
    }

}
