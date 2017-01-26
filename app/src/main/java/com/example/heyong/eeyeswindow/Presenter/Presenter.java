package com.example.heyong.eeyeswindow.Presenter;

/**
 * Created by Heyong on 2017/1/26.
 */

public interface Presenter {
    void nextData(OnGetDataSuccessByNet get);
    void nextData(OnGetDataSuccessByNet get,int count);
}
