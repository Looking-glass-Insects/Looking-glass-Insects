package com.example.heyong.shootit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.heyong.shootit.layer.GameLayer;
import com.example.heyong.shootit.orbit.GravityOrbitController;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends Activity {


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
        GameLayer gameLayer = new GameLayer();
        gameLayer.setHandler(handler);
        director.setDisplayFPS(true);
        CCScene scene = CCScene.node();
        scene.addChild(gameLayer);
        GravityOrbitController orbitController = new GravityOrbitController(Config.WINDOW_WIDTH / 2, 64);
        gameLayer.addOrbits(orbitController);
        for (int i = 0; i < 3; i++)
            orbitController.addItem(null);
        director.runWithScene(scene);
    }

}
