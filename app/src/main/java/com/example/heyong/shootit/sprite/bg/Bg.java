package com.example.heyong.shootit.sprite.bg;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.OnClockGetListener;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * 提供背景图
 */

public abstract class Bg extends CCLayer implements OnClockGetListener {
    protected Bg() {
        this.setVertexZ(1);
    }

    public static Bg getBg(int stage) {
        CCLayer bg = CCLayer.node();
        if (stage == 1) {
            return new Bg1();
        } else if (stage == 2) {
            return new Bg2();
        } else if (stage == 3) {
            return new Bg3();
        }
        throw new IllegalStateException("No such bg");
    }


    static class Bg3 extends Bg {

        private CCSprite[][] sprites = new CCSprite[6][2];
        private float speed = 2.0f;

        public Bg3() {
            this.scheduleUpdate();
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 6; j++) {
                    sprites[j][i] = new CCSprite(Config.bg_3);
                    sprites[j][i].setPosition(256 * i + 128, 256 * j + 128);
                    this.addChild(sprites[j][i]);
                }
        }

        @Override
        public void onGetClock() {
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 2; j++) {
                    CGPoint points = sprites[i][j].getPosition();
                    if (points.y <= -128) {
                        sprites[i][j].setPosition(points.x, 1024 + 128);
                    } else {
                        sprites[i][j].setPosition(points.x, points.y - speed);
                    }
                }
        }

        public void update(float dt) {
            this.onGetClock();
        }
    }


    static class Bg2 extends Bg {
        private CCSprite[] sprites = new CCSprite[10];
        private float speed = 2.0f;

        public Bg2() {
            this.scheduleUpdate();
            for (int j = 0; j < sprites.length; j++) {
                sprites[j] = new CCSprite(Config.bg_2);
                sprites[j].setPosition(256, 128 * j + 64);
                this.addChild(sprites[j]);
            }
        }

        @Override
        public void onGetClock() {
            for (int i = 0; i < sprites.length; i++) {
                CGPoint points = sprites[i].getPosition();
                if (points.y <= -64) {
                    sprites[i].setPosition(points.x, 1024 + 64);
                } else {
                    sprites[i].setPosition(points.x, points.y - speed);
                }
            }
        }

        public void update(float dt) {
            this.onGetClock();
        }
    }


    static class Bg1 extends Bg {

        private CCSprite[][] sprites = new CCSprite[6][2];
        private float speed = 2.0f;

        public Bg1() {
            this.scheduleUpdate();
            for (int i = 0; i < 2; i++)
                for (int j = 0; j < 6; j++) {
                    sprites[j][i] = new CCSprite(Config.bg_1);
                    sprites[j][i].setPosition(256 * i + 128, 256 * j + 128);
                    this.addChild(sprites[j][i]);
                }
        }

        @Override
        public void onGetClock() {
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 2; j++) {
                    CGPoint points = sprites[i][j].getPosition();
                    if (points.y <= -128) {
                        sprites[i][j].setPosition(points.x, 1024 + 128);
                    } else {
                        sprites[i][j].setPosition(points.x, points.y - speed);
                    }
                }
        }

        public void update(float dt) {
            this.onGetClock();
        }
    }
}
