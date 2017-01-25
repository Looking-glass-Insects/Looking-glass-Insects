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

    static String[] strings;

    static {
        strings = new String[]{
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
                "I go to it",
                "中文测试",
                "寻寻觅觅，",
                "冷冷清清，凄凄惨惨戚戚。",
                "梧桐更兼细雨，",
                "到黄昏、点点滴滴。",
                "未若锦囊收艳骨，一抔净土掩风流",
                "质本洁来还洁去，强于污淖陷渠沟。",
                "尔今死去侬收葬，未卜侬身何日丧？",
                "侬今葬花人笑痴，他年葬侬知是谁？",
                "试看春残花渐落，便是红颜老死时。",
                "一朝春尽红颜老，花落人亡两不知！"
        };
    }

    public void getData() {
        List<String> flowList = new LinkedList<>();
        flowList.addAll(Arrays.asList(strings));
        List<HotPublisherBean> publisherBeanList = new LinkedList<>();
        for (int i = 0; i < 10; i++)
            publisherBeanList.add(new HotPublisherBean());
        dataListener.onGetData(flowList, publisherBeanList);
    }


    public interface FindPageDataListener {
        void onGetData(List<String> flow, List<HotPublisherBean> beans);
    }
}
