package com.example.heyong.eeyeswindow.Presenter;

import android.content.Context;

import com.example.heyong.eeyeswindow.Bean.HotPublisherBean;
import com.example.heyong.eeyeswindow.Cache.DiskLruCacheHelper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class FindPagePresenter implements Presenter {
    private FindPageDataListener dataListener;
    private Context context;

    static final String DIR = "object";
    static final String KEY_FLOW = "List<String>";
    static final String KEY_HOT = "List<HotPublisherBean>";

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

    @Override
    public void nextData(INetworkCallBack get) {
        List<String> flowList = new LinkedList<>();
        flowList.addAll(Arrays.asList(strings));
        List<HotPublisherBean> publisherBeanList = new LinkedList<>();
        for (int i = 0; i < 10; i++)
            publisherBeanList.add(new HotPublisherBean());
        dataListener.onGetData(flowList, publisherBeanList);
        get.onGetData(INetworkCallBack.SUCCESS);
    }

    @Override
    public void nextData(INetworkCallBack get, int count) {

    }

    public void startCache(final LinkedList<String> flow, final LinkedList<HotPublisherBean> hotList) {
        DiskLruCacheHelper.writeCache(new DiskLruCacheHelper.WriteCallBack() {
            @Override
            public String dir() {
                return DIR;
            }

            @Override
            public String key() {
                return KEY_FLOW;
            }

            @Override
            public long maxSize() {
                return 0;
            }

            @Override
            public boolean onGetStream(OutputStream os) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(flow);
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                   return false;
                }
                return true;
            }
        });
        DiskLruCacheHelper.writeCache(new DiskLruCacheHelper.WriteCallBack() {
            @Override
            public String dir() {
                return DIR;
            }

            @Override
            public String key() {
                return KEY_HOT;
            }

            @Override
            public long maxSize() {
                return 0;
            }

            @Override
            public boolean onGetStream(OutputStream os) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    oos.writeObject(hotList);
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
        });
    }

    public interface FindPageDataListener {
        void onGetData(List<String> flow, List<HotPublisherBean> beans);
    }
}
