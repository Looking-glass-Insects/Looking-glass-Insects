package com.example.heyong.shootit.layer;

import android.app.Activity;
import android.os.Handler;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

/**
 * Created by Heyong on 2017/4/20.
 */

public class BaseLayer extends CCLayer {

    protected CGSize cgSize ;
    protected Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public BaseLayer(){
        cgSize = CCDirector.sharedDirector().getWinSize();
    }

    protected static Activity getContext() {
        return CCDirector.sharedDirector().getActivity();
    }
}
