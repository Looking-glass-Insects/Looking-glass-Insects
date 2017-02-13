package com.example.heyong.eeyeswindow;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.heyong.eeyeswindow.Cache.DiskLruCacheHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

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

//        DiskLruCacheHelper.init(appContext);

        DiskLruCacheHelper.getSize(new DiskLruCacheHelper.SizeCallBack() {
            @Override
            public void onGetSize(long size) {
                Log.d(TAG,"long-->"+size);
            }

            @Override
            public void onGetSize(String size) {
                Log.d(TAG,"String-->"+size);
            }
        });

        DiskLruCacheHelper.removeAllCache(new DiskLruCacheHelper.IRemoveListener() {
            @Override
            public void onRemoveFin() {
                Log.d(TAG,"onRemoveFin");
            }
        });

    }
}
