package com.example.heyong.shootit.layer;

import android.util.Log;
import android.view.MotionEvent;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.orbit.BaseOrbitController;
import com.example.heyong.shootit.sprite.item.BombEffect;
import com.example.heyong.shootit.sprite.item.LifeItem;
import com.example.heyong.shootit.sprite.item.SpellItem;
import com.example.heyong.shootit.util.ContinuousTapManager;
import com.example.heyong.shootit.util.LifeManager;
import com.example.heyong.shootit.util.ScoreManager;
import com.example.heyong.shootit.util.SpellCardManager;
import com.example.heyong.shootit.util.Util;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主游戏界面
 */

public class GameLayer extends BaseLayer {


    public static final int TAG_GET_FREEZE = 46;

    protected boolean gameOver = false;

    static final String TAG = "GameLayer";

    protected List<BaseOrbitController> orbits = new ArrayList<>(10);


    public GameLayer() {
        this.setIsTouchEnabled(true);
        this.scheduleUpdate();
        ContinuousTapManager.getInstance().registerGameLayer(this);
        LifeManager.getInstance().registerGameLayer(this);
        initScore();
        initLifeAndSpell();
    }

    /**
     * 不断回调,每帧一次
     */
    public void update(float dt) {
        Iterator<BaseOrbitController> iterator = orbits.iterator();
        while (iterator.hasNext()) {
            BaseOrbitController orbit = iterator.next();
            if (orbit.canBeDestroyed()) {
                iterator.remove();
                orbit.onDestroy();
                Log.d(TAG, "dead");
            } else {
                orbit.onGetClock();
            }
        }
        if (bombEffect != null)
            bombEffect.onGetClock();
        loadScore();
        loadLifeAndBomb();
    }

    public void addOrbits(BaseOrbitController orbitController) {
        this.orbits.add(orbitController);
        orbitController.setParent(this);
    }

    public void addOrbits(final BaseOrbitController orbitController,long delayTime){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                orbitController.setParent(GameLayer.this);
                orbits.add(orbitController);
            }
        },delayTime);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        if (gameOver) {
            if (Util.isClicke(event, this, gameOverLabel)) {
                CCScene scene = CCScene.node();
                scene.addChild(StartLayer.getStartLayer());
                CCFadeTransition transition = CCFadeTransition.transition(0.5F, scene);
                CCDirector.sharedDirector().replaceScene(transition);
                this.finish();
            }
        }
        float x = event.getX();
        float y = event.getY();
        CGPoint p1 = CGPoint.ccp(x, y);
        CGPoint p2 = CCDirector.sharedDirector().convertToGL(p1);
        for (BaseOrbitController orbit : orbits) {
            if (orbit.isTouched(p2)) {
                orbit.onHandleTouchEvent(p2);
            }
        }
        if (Util.isClicke(event, this, spellCardLogo)) {
            loadBombEffect();
        }

        return true;
    }


    /**
     * 为所有子弹加冰冻效果
     */
    public void loadFreezeEffect() {
        for (BaseOrbitController controller : orbits) {
            controller.onGetFreezeSpellCard();
        }
    }

    /**
     * 消弹
     */
    protected BombEffect bombEffect;

    protected void loadBombEffect() {
        if (!SpellCardManager.getInstance().requireSpellCard()) {
            return;
        }
        if (bombEffect == null) {
            prepareBombEffect();
        }
        bombEffect.fresh();
        CGPoint point = CGPoint.ccp(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        bombEffect.setPosition(point);
        for (BaseOrbitController controller : orbits) {
            controller.onGetBombSpellCard(point);
        }
    }

    /**
     * 分数加载
     */
    protected CCLabel highScoreLabel;
    protected CCLabel currScoreLabel;
    protected int textSize = 8;

    protected void initScore() {
        highScoreLabel = CCLabel.makeLabel("最高分数：", "Roboto_Thin.ttf", textSize);//创建字体，中间参数为ttf文件，20为字体大小
        highScoreLabel.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        highScoreLabel.setPosition(Config.WINDOW_WIDTH - 48, Config.WINDOW_HEIGHT - 10);
        currScoreLabel = CCLabel.makeLabel("当前分数：", "", textSize);
        currScoreLabel.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        currScoreLabel.setPosition(Config.WINDOW_WIDTH - 48, Config.WINDOW_HEIGHT - 24);
        this.addChild(highScoreLabel, 2);
        this.addChild(currScoreLabel, 2);
    }

    /**
     *
     */
    protected CCLabel gameOverLabel;

    public void gameOver() {
        gameOverLabel = CCLabel.makeLabel("游戏结束，点击返回", "Roboto_Thin.ttf", 24);
        gameOverLabel.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        gameOverLabel.setPosition(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT / 2);
        this.addChild(gameOverLabel, 99);
        gameOver = true;
    }

    public void loadScore() {
        ScoreManager manager = ScoreManager.getInstance();
        long highScore = manager.getHighScore();
        long score = manager.getScore();
        highScoreLabel.setString("最高分数： " + highScore);
        currScoreLabel.setString("当前分数： " + score);
    }

    /**
     * 生命值，SpellCard加载
     */
    protected CCLabel lifeLabel;
    protected CCLabel spellCardLabel;
    protected CCSprite spellCardLogo;

    protected void initLifeAndSpell() {
        lifeLabel = CCLabel.makeLabel("生命值：", "Roboto_Thin.ttf", textSize);//创建字体，中间参数为ttf文件，20为字体大小
        lifeLabel.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        lifeLabel.setPosition(Config.WINDOW_WIDTH - 96, 10);


        spellCardLabel = CCLabel.makeLabel("SpellCard：", "", textSize);
        spellCardLabel.setColor(ccColor3B.ccc3(255, 228, 255));//初始值
        spellCardLabel.setPosition(Config.WINDOW_WIDTH - 48, 10);

        spellCardLogo = new SpellItem();
        spellCardLogo.setPosition(Config.WINDOW_WIDTH - 48, 30);
        spellCardLogo.setScale(0.5);
        CCSprite lifeLogo = new LifeItem();
        lifeLogo.setPosition(Config.WINDOW_WIDTH - 96, 30);
        lifeLogo.setScale(0.5);
        this.addChild(lifeLabel, 2);
        this.addChild(spellCardLabel, 2);
        this.addChild(spellCardLogo, 2);
        this.addChild(lifeLogo, 2);
    }

    public void loadLifeAndBomb() {
        int life = LifeManager.getInstance().getLife();
        lifeLabel.setString("player：" + life);
        int count = SpellCardManager.getInstance().getCount();
        spellCardLabel.setString("Bomb：" + count);
    }

    protected void prepareBombEffect() {
        bombEffect = new BombEffect();
        this.addChild(bombEffect, bombEffect.getZ());
    }

    protected void finish() {
        ContinuousTapManager.getInstance().onDestroy();
        ScoreManager.getInstance().init();
        SpellCardManager.getInstance().init();
        LifeManager.getInstance().init();
    }

}
