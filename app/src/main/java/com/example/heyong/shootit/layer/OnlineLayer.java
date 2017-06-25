package com.example.heyong.shootit.layer;

import android.util.Log;

import com.example.heyong.shootit.Config;
import com.example.heyong.shootit.online.GameClient;
import com.example.heyong.shootit.online.GameServer;
import com.example.heyong.shootit.online.NetBean;
import com.example.heyong.shootit.orbit.DiffusedCircleOrbit;
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
    private static final int DEFAULT_INTERVAL = 180 * 3;
    private int clockCount = DEFAULT_CLOCK_COUNT;
    private int interval = DEFAULT_INTERVAL;
    public static final int STATUS_CLIENT = 3242;
    public static final int STATUS_SERVER = 322;
    public static int status = -1;

    private int tapCount = 0;

    private RandomOrbit randomOrbit;


    private class InnerOrbitController extends DiffusedCircleOrbit {

        public InnerOrbitController(float start_x, float start_y, float prespeed_x, float prespeed_y, int appeartime) {
            super(start_x, start_y, prespeed_x, prespeed_y, appeartime);
        }

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
    }


    private void init() {
        DiffusedCircleOrbit orbitController = new InnerOrbitController(Config.WINDOW_WIDTH / 2, Config.WINDOW_HEIGHT, 0.0f, -1.0f, 0);
        this.addOrbits(orbitController);
        orbitController.init();
    }

    public OnlineLayer() {
        super();
        init();
        randomOrbit = new RandomOrbit();
        randomOrbit.setOnlineLayer(this);
        this.addOrbits(randomOrbit);
    }


    @Override
    public void update(float dt) {
        //Log.d(TAG,"-->update");
        if (!isConnected)
            return;
        super.update(dt);
        //Log.d(TAG,"update");
        clockCount--;
        interval--;
        if (clockCount <= 0) {
            Log.d(TAG, "onExchangeInfo");
            onExchangeInfo();
            clockCount = DEFAULT_CLOCK_COUNT;
        }
        if (interval <= 0) {
            init();
            interval = DEFAULT_INTERVAL;
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
        Log.d(TAG, "onClient-->" + tapCount);
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
        Log.d(TAG, "onServer-->" + tapCount);
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

        int count = b.getCount();
        loadFreezeEffect();

        Log.d(TAG, "receive-->" + count);

        for (int i = 0; i < count; i++) {
            String className = getRandomClassName();

            if (className.equals("BaseBigLightBullet")) {

                BaseBigLightBullet bigLightBullet = new BaseBigLightBullet();
                randomOrbit.addItem(bigLightBullet);

            } else if (className.equals("BaseBigBullet")) {

                BaseBigBullet bigBullet = new BaseBigBullet();
                randomOrbit.addItem(bigBullet);

            } else if (className.equals("BaseCircleBullet")) {

                BaseCircleBullet circleBullet = new BaseCircleBullet();
                randomOrbit.addItem(circleBullet);

            } else if (className.equals("BaseCubeBullet")) {

                BaseCubeBullet cubeBullet = new BaseCubeBullet();
                randomOrbit.addItem(cubeBullet);
            } else if (className.equals("BaseLightBullet")) {
                BaseLightBullet lightBullet = new BaseLightBullet();
                randomOrbit.addItem(lightBullet);
            } else if (className.equals("BaseLittlePointBullet")) {
                BaseLittlePointBullet littlePointBullet = new BaseLittlePointBullet();
                randomOrbit.addItem(littlePointBullet);
            } else if (className.equals("BaseMusicBullet")) {
                BaseMusicBullet musicBullet = new BaseMusicBullet();
                randomOrbit.addItem(musicBullet);
            } else if (className.equals("BaseRiceBullet")) {
                BaseRiceBullet riceBullet = new BaseRiceBullet();
                randomOrbit.addItem(riceBullet);
            } else if (className.equals("BaseStarBullet")) {
                BaseStarBullet starBullet = new BaseStarBullet();
                randomOrbit.addItem(starBullet);
            } else if (className.equals("BaseTailBullet")) {
                BaseTailBullet tailBullet = new BaseTailBullet();
                randomOrbit.addItem(tailBullet);
            } else {
                Log.d(TAG, "error");
            }
        }


    }


    /**
     * 增加点击到的item数
     */
    public void addTap() {
        this.tapCount++;
    }
}
