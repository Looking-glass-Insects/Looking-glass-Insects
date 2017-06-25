package com.example.heyong.shootit.layer;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.orbit.DiffusedCircleOrbit;
import com.example.heyong.shootit.orbit.guidao3;
import com.example.heyong.shootit.orbit.guidao4;
import com.example.heyong.shootit.sprite.bg.Bg;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;

/**
 * 第二关
 */

public class Stage2 extends Stage1 {

    public Stage2() {
        super();
    }


    protected void showClear() {
        CCLabel singleStart = CCLabel.makeLabel("Next Stage", "Roboto_Thin.ttf", 24);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        singleStart.setTag(TAG_NEXT_STAGE);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(singleStart, 99);
    }


    protected void load() {
        guidao3 guidao30 = new guidao3(128);
        this.addOrbits(guidao30);
        guidao4 guidao40 = new guidao4(128);
        this.addOrbits(guidao40);
//        this.addOrbits(controller);
//        this.addOrbits(controller2);
        DiffusedCircleOrbit b = new DiffusedCircleOrbit(a1, b1 * 2, 0.0f, -b1 / 360, 0);
        this.addOrbits(b);
        for (int i = 0; i < 8; i++) {
            b.addItem1(a1, b1 * 2, (float) (1.0f * Math.cos(pi / 4 * i)), (float) (1.0f * Math.sin(pi / 4 * i)));
        }
        DiffusedCircleOrbit a = new DiffusedCircleOrbit(a1, b1 * 2, 0.0f, -b1 / 120, 0);
        this.addOrbits(a);
        for (int i = 0; i < 8; i++) {
            a.addItem1(a1, b1 * 2, (float) (1.0f * Math.cos(pi / 4 * i)), (float) (1.0f * Math.sin(pi / 4 * i)));
        }

        for (int i = 0; i < 30; i++) {
            guidao30.addItem(a1 + i * 24, (-4 * b1 * (a1 + i * 24 - a1 / 2) * (a1 + i * 24 - a1 / 2)) / (a1 * a1) + 2 * b1);
            guidao40.addItem(a1 - i * 24, (-4 * b1 * (a1 + i * 24 - (3 * a1) / 2) * (a1 + i * 24 - (3 * a1) / 2)) / (a1 * a1) + 2 * b1);
//            controller.addItem(Config.WINDOW_HEIGHT + i * 64);
//            controller2.addItem(Config.WINDOW_HEIGHT + i * 64);
        }
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
