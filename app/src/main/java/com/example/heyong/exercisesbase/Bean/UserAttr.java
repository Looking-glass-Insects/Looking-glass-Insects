package com.example.heyong.exercisesbase.Bean;

/**
 * Created by Heyong on 2016/11/7.
 */
public enum UserAttr {
    MANAGER,
    STUDENT;
    public static String getOtherByAttr(UserAttr attr){
        if(attr == UserAttr.MANAGER)
            return "学生";
        else
            return "管理员";
    }

    public static UserAttr converse(UserAttr attr){
        if(attr == UserAttr.MANAGER)
            return UserAttr.STUDENT;
        else
            return UserAttr.MANAGER;
    }

}