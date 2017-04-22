package com.example.heyong.shootit;

/**
 * Created by Heyong on 2017/4/20.
 */

public class Config {
    /**
     * 屏幕宽高
     */
    public static int WINDOW_HEIGHT = 1024;
    public static int WINDOW_WIDTH = 512;
    /**
     * 加载资源目录字符串
     */
    public static String Bullet1 = "bullet1.png";
    public static String Bullet2 = "bullet2.png";
    public static String Bullet3 = "bullet3.png";
    public static String Bullet4 = "bullet4.png";
    public static String Bullet5 = "bullet5.png";
    public static String item = "item.png";
    public static String eff_maple = "eff_maple.png";
    public static String bg_3 = "bg/bg03.png";
    public static String bg_2 = "bg/bg02.png";
    public static String bg_1 = "bg/bg01.png";
    public static String Ice = "ice00.png";
    /**
     * 默认判定点大小
     */
    public static float USER_RADIUS = 20f;
    public static float PLAYER_RADIUS = 5f;
    /**
     * 手柄位置
     */
    public static float handle_x = 64;
    public static float handle_y = 64;
    public static float handle_radius = 64;

    public static String BIG_CIRCLE = "bigCircle.png";
    public static String LITTLE_CIRCLE = "littleCircle.png";
    /**
     * 操控区位置
     */
    public static float CUT_LINE = handle_y + handle_radius;
    /**
     * 人物角色
     */
    public static String LING_MENG = "pl00.png";
    public static float start_y = 160;
    public static float start_x = WINDOW_WIDTH / 2;
    public static String EFFECT = "eff_sloweffect.png";
}
