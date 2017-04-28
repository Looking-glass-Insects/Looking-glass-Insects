package com.example.heyong.shootit.layer;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.MainActivity;
import com.example.heyong.shootit.R;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;

/**
 * 第三关
 */

public class Stage3 extends Stage1 {

    public Stage3() {
        MainActivity.getEngine().playSound(getContext(), R.raw.bgm3, true);
    }

    @Override
    protected void showClear() {
        CCLabel singleStart = CCLabel.makeLabel("返回主界面", "Roboto_Thin.ttf", 24);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        singleStart.setTag(TAG_NEXT_STAGE);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(singleStart, 99);
    }

    @Override
    protected void nextStage() {
        CCScene scene = CCScene.node();
        scene.addChild(StartLayer.getStartLayer());
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
        onGameFinal();
    }

    @Override
    protected void load() {

    }

    private void onGameFinal() {
        this.finish();
        MainActivity.getEngine().pauseSound();
    }

}
