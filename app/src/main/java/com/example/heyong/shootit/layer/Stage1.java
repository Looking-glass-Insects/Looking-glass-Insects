package com.example.heyong.shootit.layer;

import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.MainActivity;
import com.example.heyong.shootit.R;
import com.example.heyong.shootit.Util;
import com.example.heyong.shootit.sprite.bg.Bg;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;

/**
 * 第一关
 */

public class Stage1 extends GameLayer {
    protected int time = 0;
    protected boolean isFin = false;
    protected static int NEXT = 0;

    public Stage1() {
        MainActivity.getEngine().playSound(getContext(), R.raw.bgm1, true);
    }

    /**
     * 回调
     * @param dt
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        //time++;
        if (time >= 60 * 10) {
            if (!isFin) {
                isFin = true;
                showClear();
            }
        }
    }

    protected void showClear() {
        CCLabel singleStart = CCLabel.makeLabel("Next Stage", "Roboto_Thin.ttf", 24);//创建字体，中间参数为ttf文件，20为字体大小
        singleStart.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        singleStart.setTag(NEXT);
        singleStart.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(singleStart, 99);
    }

    protected void nextStage() {
        CCScene scene = CCScene.node();
        Stage2 gameLayer = new Stage2();
        scene.addChild(Bg.getBg(2));
        scene.addChild(gameLayer);
        CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
        CCDirector.sharedDirector().replaceScene(transition);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        if (isFin) {
            if (Util.isClicke(event, this, this.getChildByTag(NEXT))) {
                nextStage();
            }
            return true;
        } else {
            return super.ccTouchesBegan(event);
        }
    }
}
