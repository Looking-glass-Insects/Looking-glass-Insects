package com.example.heyong.shootit.layer;

import com.example.heyong.shootit.MainActivity;
import com.example.heyong.shootit.R;
import com.example.heyong.shootit.sprite.bg.Bg;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.transitions.CCFadeTransition;

/**
 *  第二关
 */

public class Stage2 extends Stage1 {

    public Stage2() {
        MainActivity.getEngine().playSound(getContext(), R.raw.bgm2, true);
    }

    @Override
    protected void showClear() {
        super.showClear();
    }

    @Override
    protected void nextStage() {
        CCScene scene = CCScene.node();
        Stage3 stage3 = new Stage3();
        scene.addChild(Bg.getBg(3));
        scene.addChild(stage3);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
    }
}
