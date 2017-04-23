package com.example.heyong.shootit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.heyong.shootit.layer.StartLayer;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

public class MainActivity extends Activity {

    static String TAG = "MainActivity";

    private static SoundEngine engine;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            return false;
        }
    });

    private CCDirector director;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
        setContentView(surfaceView);
        director = CCDirector.sharedDirector();
        director.attachInView(surfaceView);
        director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
        director.setScreenSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        director.setDisplayFPS(true);
        setupMainWindow();
        initBgm();
    }

    private void setupMainWindow() {
        CCScene scene = CCScene.node();
        scene.addChild(new StartLayer());
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
        super.onDestroy();
    }

    public static SoundEngine getEngine() {
        if (engine == null) {
            throw new IllegalStateException("音乐播放器未加载完成");
        }
        return engine;
    }
}
