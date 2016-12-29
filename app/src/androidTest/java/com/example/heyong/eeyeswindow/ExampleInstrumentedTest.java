package com.example.heyong.eeyeswindow;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.heyong.eeyeswindow.Cache.CacheManager;
import com.example.heyong.eeyeswindow.Presenter.HomePagePresenter;
import com.example.heyong.eeyeswindow.Tools.GlideCacheUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    static String TAG = "ExampleInstrumentedTest";
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        CacheManager manager = new CacheManager(appContext);
        String s = manager.getAllSize();
        String s1 = GlideCacheUtil.getInstance().getCacheSize(appContext);
        Log.e(TAG,"-->"+s+"<>"+s1);
    }
}
