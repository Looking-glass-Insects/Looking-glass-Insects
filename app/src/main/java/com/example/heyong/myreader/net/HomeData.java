package com.example.heyong.myreader.net;

import android.support.annotation.NonNull;

import com.example.heyong.myreader.bean.Bean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Heyong on 2017/4/6.
 *
 * category 后面可接受参数 all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
 */

public class HomeData {

    private API api;
    private Call<Bean> call;

    public HomeData() {
        buildApi();
    }

    private void buildApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public void setParam(String category,int count,int page){
        if (api == null)
            buildApi();
        call = api.getData(category,count,page);
    }

    public void call(@NonNull Callback<Bean> callback){
        assert call != null;
        call.enqueue(callback);
    }

    public interface API{
        @GET("{category}/{count}/{page}")
        Call<Bean> getData(@Path("category") String category,
                           @Path("count") int count,
                           @Path("page") int page);
    }
}
