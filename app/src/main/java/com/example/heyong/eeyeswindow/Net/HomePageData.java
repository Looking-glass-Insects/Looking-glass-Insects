package com.example.heyong.eeyeswindow.Net;

import android.support.annotation.Nullable;

import com.example.heyong.eeyeswindow.Bean.Bean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Heyong
 *
 */

public class HomePageData {
    private static final long DEFAULT_TIMEOUT = 5;

    public static void dataCallBack(Callback<Bean> callback){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.baidu.com/acman/zhaiyanapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        API api = retrofit.create(API.class);
        Call<Bean> call = api.get(null);
        call.enqueue(callback);
    }


}
interface API {
    @Headers({
            "apikey:2bfb5cd064a218db119285f119fe545b"
    })
    @GET("tcrand")
    Call<Bean> get(
            @Query("fangfa") @Nullable String fangfa
    );
}


