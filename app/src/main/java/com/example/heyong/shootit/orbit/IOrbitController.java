package com.example.heyong.shootit.orbit;

import com.example.heyong.shootit.interfaces.OnClockGetListener;
import com.example.heyong.shootit.interfaces.OnHandleTouchEventListener;

import org.cocos2d.types.CGPoint;

/**
 * 用于控制轨道
 */

public interface IOrbitController extends OnHandleTouchEventListener ,OnClockGetListener{
    /**
     * @return 该轨道的z坐标值
     */
    int getZ();

    /**
     * @param point 用户点击的点坐标
     */
    void onHandleTouchEvent(CGPoint point);

    /**
     * @return 该轨道上子弹总数
     */
    int getItemCount();

    /**
     * @param point 用户点击的点
     * @return true if touched
     */
    boolean isTouched(CGPoint point);


    /**
     * 该轨道是否可以销毁
     *
     * @return
     */
    boolean canBeDestroyed();

    /**
     * 销毁该轨道
     */
    void onDestroy();
}
