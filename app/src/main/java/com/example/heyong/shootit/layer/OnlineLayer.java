package com.example.heyong.shootit.layer;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.online.GameClient;
import com.example.heyong.shootit.online.GameServer;
import com.example.heyong.shootit.online.NetBean;
import com.example.heyong.shootit.orbit.GravityOrbitController;
import com.example.heyong.shootit.orbit.RandomOrbit;
import com.example.heyong.shootit.sprite.BaseItem;
import com.example.heyong.shootit.sprite.bullet.BaseBigBullet;
import com.example.heyong.shootit.sprite.bullet.BaseBigLightBullet;
import com.example.heyong.shootit.sprite.bullet.BaseCircleBullet;
import com.example.heyong.shootit.sprite.bullet.BaseCubeBullet;
import com.example.heyong.shootit.sprite.bullet.BaseLightBullet;
import com.example.heyong.shootit.sprite.bullet.BaseLittlePointBullet;
import com.example.heyong.shootit.sprite.bullet.BaseMusicBullet;
import com.example.heyong.shootit.sprite.bullet.BaseRiceBullet;
import com.example.heyong.shootit.sprite.bullet.BaseStarBullet;
import com.example.heyong.shootit.sprite.bullet.BaseTailBullet;

import org.cocos2d.types.CGPoint;

import java.util.Iterator;
import java.util.Random;

/**
 * 联机界面
 */

public class OnlineLayer extends GameLayer {

    static String TAG = "OnlineLayer";

    public static boolean isConnected = false;
    private static final int DEFAULT_CLOCK_COUNT = 180;

    private int clockCount = DEFAULT_CLOCK_COUNT;

    public static final int STATUS_CLIENT = 3242;
    public static final int STATUS_SERVER = 322;
    public static int status = -1;

    private int tapCount = 0;
    private GravityOrbitController orbitController;

    private RandomOrbit randomOrbit;

    public OnlineLayer() {
        super();
        orbitController = new GravityOrbitController(Config.WINDOW_WIDTH / 2, 128) {
            @Override
            public void onHandleTouchEvent(CGPoint point) {
                Iterator<BaseItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    BaseItem item = iterator.next();
                    if (item.isTouched(point)) {
                        tapCount++;//点击到的弹幕数加一
                        item.onHandleTouchEvent(point);
                        break;
                    }
                }
            }
        };

        randomOrbit = new RandomOrbit();
        randomOrbit.setOnlineLayer(this);

        this.addOrbits(orbitController);
        this.addOrbits(randomOrbit);

        for (int i = 0; i < 5; i++)
            orbitController.addItem(Config.WINDOW_HEIGHT + i * 64);
    }

    @Override
    public void update(float dt) {
        if (!isConnected)
            return;
        super.update(dt);
        clockCount--;
        if (clockCount <= 0) {
            onExchangeInfo();
            clockCount = DEFAULT_CLOCK_COUNT;
        }
    }


    private void onExchangeInfo() {
        if (status == STATUS_SERVER) {
            onServer();
        } else if (status == STATUS_CLIENT) {
            onClient();
        } else {
            throw new IllegalStateException("未知身份：" + status);
        }
    }


    private static String[] classNames = {
            "BaseBigLightBullet",
            "BaseBigBullet",
            "BaseCircleBullet",
            "BaseCubeBullet",
            "BaseLightBullet",
            "BaseLittlePointBullet",
            "BaseMusicBullet",
            "BaseRiceBullet",
            "BaseStarBullet",
            "BaseTailBullet"
    };

    private static String getRandomClassName() {
        return classNames[new Random().nextInt(classNames.length)];
    }


    private void onClient() {
        GameClient client = GameClient.getInstance();
        if (tapCount != 0) {
            NetBean bean = new NetBean();
            bean.setCount(tapCount);
            bean.setBulletClass(getRandomClassName());
            client.writeObj(bean);
            tapCount = 0;
        }
        Object o = client.readObj();
        readObj(o);
    }

    /**
     * 服务端进行数据读取及写出
     */
    private void onServer() {
        GameServer server = GameServer.getInstance();
        if (tapCount != 0) {
            NetBean bean = new NetBean();
            bean.setCount(tapCount);
            bean.setBulletClass(getRandomClassName());
            server.writeObj(bean);
            Log.d(TAG, "tap-->" + tapCount);
            tapCount = 0;
        }
        Object o = server.readObj();
        readObj(o);
    }


    private void readObj(Object o) {
        if (o == null)
            return;
        NetBean b = (NetBean) o;
        String className = b.getBulletClass();
        int count = b.getCount();
        Log.d(TAG, "receive-->" + count);
        if (className.equals("BaseBigLightBullet")) {
            for (int i = 0; i < count; i++) {
                BaseBigLightBullet bigLightBullet = new BaseBigLightBullet();
                randomOrbit.addItem(bigLightBullet);
            }
        } else if (className.equals("BaseBigBullet")) {
            for (int i = 0; i < count; i++) {
                BaseBigBullet bigBullet = new BaseBigBullet();
                randomOrbit.addItem(bigBullet);
            }
        } else if (className.equals("BaseCircleBullet")) {
            for (int i = 0; i < count; i++) {
                BaseCircleBullet circleBullet = new BaseCircleBullet();
                randomOrbit.addItem(circleBullet);
            }
        } else if (className.equals("BaseCubeBullet")) {
            for (int i = 0; i < count; i++) {
                BaseCubeBullet cubeBullet = new BaseCubeBullet();
                randomOrbit.addItem(cubeBullet);
            }
        } else if (className.equals("BaseLightBullet")) {
            for (int i = 0; i < count; i++) {
                BaseLightBullet lightBullet = new BaseLightBullet();
                randomOrbit.addItem(lightBullet);
            }
        } else if (className.equals("BaseLittlePointBullet")) {
            for (int i = 0; i < count; i++) {
                BaseLittlePointBullet littlePointBullet = new BaseLittlePointBullet();
                randomOrbit.addItem(littlePointBullet);
            }
        } else if (className.equals("BaseMusicBullet")) {
            for (int i = 0; i < count; i++) {
                BaseMusicBullet musicBullet = new BaseMusicBullet();
                randomOrbit.addItem(musicBullet);
            }
        } else if (className.equals("BaseRiceBullet")) {
            for (int i = 0; i < count; i++) {
                BaseRiceBullet riceBullet = new BaseRiceBullet();
                randomOrbit.addItem(riceBullet);
            }
        } else if (className.equals("BaseStarBullet")) {
            for (int i = 0; i < count; i++) {
                BaseStarBullet starBullet = new BaseStarBullet();
                randomOrbit.addItem(starBullet);
            }
        } else if (className.equals("BaseTailBullet")) {
            for (int i = 0; i < count; i++) {
                BaseTailBullet tailBullet = new BaseTailBullet();
                randomOrbit.addItem(tailBullet);
            }
        }
    }


    @Override
    public void loadFreezeEffect() {

    }

    /**
     * 增加点击到的item数
     */
    public void addTap() {
        this.tapCount++;
    }
}
