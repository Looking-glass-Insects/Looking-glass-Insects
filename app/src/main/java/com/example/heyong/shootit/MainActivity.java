package com.example.heyong.shootit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.heyong.shootit.layer.OnlineLayer;
import com.example.heyong.shootit.layer.StartLayer;
import com.example.heyong.shootit.online.GameClient;
import com.example.heyong.shootit.online.GameServer;
import com.example.heyong.shootit.online.SendThread;
import com.example.heyong.shootit.util.ScoreManager;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

public class MainActivity extends Activity {

    static String TAG = "MainActivity";

    private static SoundEngine engine;

    private CCDirector director;

    public static int GAME_START = 942;


    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == StartLayer.TAG_ONLINE_SERVER) {
                //开启服务器
                GameServer server = GameServer.getInstance();
                server.setHandler(handler);
                server.startServer(5251);
            } else if (msg.what == StartLayer.TAG_ONLINE_CLINET) {
                //连接服务器
                GameClient client = GameClient.getInstance();
                client.setHandler(handler);
                client.setContext(MainActivity.this);
                client.connect(5251);
            } else if (msg.what == SendThread.TAG_ON_CONNECTED) {
                //client已连接
                Toast.makeText(MainActivity.this, "已连接", Toast.LENGTH_SHORT).show();
                GameServer.getInstance().writeObj(SendThread.TAG_GAME_START);

                OnlineLayer.isConnected = true;
            } else if (msg.what == SendThread.TAG_GAME_START) {
                //服务器向client发送开始标志
                Toast.makeText(MainActivity.this, "已连接", Toast.LENGTH_SHORT).show();

                OnlineLayer.isConnected = true;
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeConfig();
        CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
        surfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);
        setContentView(surfaceView);
        director = CCDirector.sharedDirector();
        director.attachInView(surfaceView);
        director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
        director.setScreenSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        director.setDisplayFPS(true);
        setupMainWindow();
        initBgm();
        initGlobal();
    }

    private void changeConfig() {
        WindowManager wm = this.getWindowManager();

        float width = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();

        Config.WINDOW_HEIGHT = height / width * Config.WINDOW_WIDTH;
    }

    private void initGlobal() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.setContext(this);
        manager.loadHighScore();
    }

    /**
     * 初始化初始界面
     */
    private void setupMainWindow() {
        CCScene scene = CCScene.node();
        StartLayer layer = new StartLayer();
        scene.addChild(layer);
        layer.setHandler(handler);
        director.runWithScene(scene);
    }

    private void initBgm() {
        engine = SoundEngine.sharedEngine();
        engine.preloadSound(this, R.raw.bgm1);
        engine.preloadSound(this, R.raw.bgm2);
        engine.preloadSound(this, R.raw.bgm3);
    }

    @Override
    protected void onStart() {
        engine.resumeSound();
        super.onStart();
    }

    @Override
    protected void onPause() {
        engine.pauseSound();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SoundEngine.purgeSharedEngine();
        ScoreManager.getInstance().onDestroy();
        director.end();
        GameServer.getInstance().close();
        GameClient.getInstance().disconnect();
        super.onDestroy();
    }

    public static SoundEngine getEngine() {
        if (engine == null) {
            throw new IllegalStateException("音乐播放器未加载完成");
        }
        return engine;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
