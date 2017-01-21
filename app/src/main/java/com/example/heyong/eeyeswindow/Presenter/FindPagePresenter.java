package com.example.heyong.eeyeswindow.Presenter;

import android.content.Context;

import com.example.heyong.eeyeswindow.Bean.HotPublisherBean;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Heyong on 2017/1/21.
 */

public class FindPagePresenter {
    private FindPageDataListener dataListener;
    private Context context;

    public FindPagePresenter(Context context, FindPageDataListener dataListener) {
        this.dataListener = dataListener;
        this.context = context;
    }

    public void getData(){
         String[] strings = {
                "The",
                "life",
                "is like",
                "a box of",
                "chocolates,",
                "you could not",
                "know",
                "what you're going",
                "to",
                "get.",
                "Victory",
                "won't",
                "come to me",
                "unless",
                "I go to it"
        };
        List<String> flowList = new LinkedList<>();
        flowList.addAll(Arrays.asList(strings));
        List<HotPublisherBean> publisherBeanList = new LinkedList<>();
        for(int i = 0;i<10;i++)
            publisherBeanList.add(new HotPublisherBean());
        dataListener.onGetData(flowList,publisherBeanList);
    }



  public interface FindPageDataListener{
      void onGetData(List<String> flow, List<HotPublisherBean> beans);
  }
}
