package com.example.heyong.myreader.net;

import com.example.heyong.myreader.bean.Bean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Heyong on 2017/4/6.
 */

public class RandomData {

    private int count;
    private String category;


    public RandomData() {
    }

    public RandomData setCount(int count){
        this.count = count;
        return this;
    }

    public RandomData setCategory(String category){
        this.category = category;
        return this;
    }

    public void call(Callback<Bean> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/random/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<Bean> call = api.getRandomData(this.category,this.count);
        call.enqueue(callback);
    }


    interface API {
        @GET("{category}/{count}")
        Call<Bean> getRandomData(@Path("category") String category,
                                 @Path("count") int count);
    }
}
