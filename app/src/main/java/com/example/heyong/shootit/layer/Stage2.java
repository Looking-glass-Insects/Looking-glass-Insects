package com.example.heyong.shootit.layer;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.MainActivity;
import com.example.heyong.shootit.R;
import com.example.heyong.shootit.sprite.bg.Bg;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;

/**
 *  第二关
 */

public class Stage2 extends Stage1 {

    public Stage2() {
        MainActivity.getEngine().playSound(getContext(), R.raw.bgm2, true);
    }


    protected void showClear() {
        CCLabel singleStart = CCLabel.makeLabel("Next Stage", "Roboto_Thin.ttf", 24);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        singleStart.setTag(TAG_NEXT_STAGE);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(singleStart, 99);
    }


    protected void load() {

    }


    protected void nextStage() {
        CCScene scene = CCScene.node();
        Stage3 stage3 = new Stage3();
        scene.addChild(Bg.getBg(2));
        scene.addChild(stage3);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
    }
}
