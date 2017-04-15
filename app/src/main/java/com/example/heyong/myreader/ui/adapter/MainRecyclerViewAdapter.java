package com.example.heyong.myreader.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heyong.myreader.R;
import com.example.heyong.myreader.bean.Bean;
import com.example.heyong.myreader.net.HomeData;
import com.example.heyong.myreader.ui.WebActivity;
import com.example.heyong.myreader.ui.holder.BeanHolder;
import com.example.heyong.myreader.ui.holder.ItemTitleHolder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.heyong.myreader.MyApplication.myApplication;

/**
 * Created by Heyong on 2017/4/7.
 * 主界面第二页
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter {

    static final String TAG = "MainRecyclerViewAdapter";

    private HomeData dataManager;

    private Context context;
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;

    public static final int DEFAULT_SPAN = 6;

    private Bean[] data = new Bean[3];
    private int[] pages = new int[3];



    private ItemTitleHolder[] titleHolders = new ItemTitleHolder[3];
    private BeanHolder[][] beanHolders = new BeanHolder[3][4];
    static private String[] titles = new String[]{
            "Android",
            "iOS",
            "APP"
    };

    public MainRecyclerViewAdapter(Context context) {
        this.context = context;
        dataManager = new HomeData();
    }

    public int[] getPages() {
        return new int[]{pages[0] - 1, pages[1] - 1, pages[2] - 1};
    }

    public void setPages(int[] pages) {
        this.pages = pages;
    }


    public void initData() {
        initData(null);
    }

    public void initData(OnGetDataListener listener) {
        for (int i = 0; i < 3; i++) {
            pages[i] = 1;
            if (i == 2)
                requestData(i, listener);
            else
                requestData(i, null);
        }
    }


    /**
     * 网络数据
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int index = msg.what;
            bindDataToHolder(index * 5 + 1);
            bindDataToHolder(index * 5 + 2);
            bindDataToHolder(index * 5 + 3);
            bindDataToHolder(index * 5 + 4);
            titleHolderShowIcon(index);
            return true;
        }
    });

    private void titleHolderShowIcon(int index){
        ItemTitleHolder holder = titleHolders[index];
        if (holder != null)
            holder.showIncon(false);
    }
    public void requestData(final int index) {
        requestData(index, null);
    }

    public void requestData(final int index, @Nullable final OnGetDataListener listener) {
        if (index == 0) {
            dataManager.setParam("Android", 4, pages[0]++);
        } else if (index == 1) {
            dataManager.setParam("iOS", 4, pages[1]++);
        } else {
            dataManager.setParam("App", 4, pages[2]++);
        }
        dataManager.call(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                data[index] = response.body();
                handler.sendEmptyMessage(index);
                if (listener != null)
                    listener.onGetData(OnGetDataListener.SUCCESS);
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Log.d(TAG, "-->error");
                titleHolderShowIcon(index);
                if (listener != null)
                    listener.onGetData(OnGetDataListener.FAIL);
            }

        });
    }


    public interface OnGetDataListener {
        int SUCCESS = 1;
        int FAIL = 2;
        void onGetData(int code);
    }


    private void bindDataToHolder(int position) {
        BeanHolder holder = beanHolders[position / 5][position % 5 - 1];
        if (holder == null)
            return;
        if (data[position / 5] == null)
            return;
        List<Bean.ResultsBean> beans = data[position / 5].getResults();
        if (beans != null) {
            Bean.ResultsBean resultsBean = beans.get(position % 5 - 1);
            String desc = resultsBean.getDesc();
            holder.setDescString(desc);
            String source = resultsBean.getWho();
            holder.setSource(source);
            List<String> l = resultsBean.getImages();
            if (l != null)
                holder.setPhoto(l.get(0)+"?imageView2/0/w/100");
        }
    }


    /**
     * 改变item宽度
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == TYPE_TITLE)
                        return DEFAULT_SPAN;
                    else return DEFAULT_SPAN / 2;
                }
            });
        } else {
            throw new IllegalStateException("this manager should be GridLayoutManager");
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_TITLE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_title, parent, false);
            ItemTitleHolder titleHolder = new ItemTitleHolder(view);
            return titleHolder;
        } else if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bean, parent, false);
            BeanHolder beanHolder = new BeanHolder(view);
            beanHolder.setContext(context);
            return beanHolder;
        } else {
            throw new IllegalStateException("未知元素类型");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BeanHolder) {
            synchronized (beanHolders) {
                beanHolders[position / 5][position % 5 - 1] = (BeanHolder) holder;
            }
            bindDataToHolder(position);
            BeanHolder beanHolder = (BeanHolder) holder;
            beanHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, WebActivity.class);
                    String title = titles[position/5];
                    String url = "";
                    List<Bean.ResultsBean> beans = data[position / 5].getResults();
                    if (beans != null) {
                        Bean.ResultsBean resultsBean = beans.get(position % 5 - 1);
                        url = resultsBean.getUrl();
                    }
                    WebActivity.WebBean bean = new WebActivity.WebBean(title,url);
                    i.putExtra(WebActivity.WebView,bean);
                    context.startActivity(i);
                }
            });
        } else if (holder instanceof ItemTitleHolder) {
            synchronized (titleHolders) {
                titleHolders[position / 5] = (ItemTitleHolder) holder;
            }
            ((ItemTitleHolder) holder).setOnClickNextListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ItemTitleHolder) holder).showIncon(true);
                    requestData(position / 5);
                }
            });
            ((ItemTitleHolder) holder).setTitleIcon(myApplication.buildBitmap(R.mipmap.android));
            ((ItemTitleHolder) holder).setTitle(titles[position / 5]);
            int color = myApplication.getThemeColor();
            ((ItemTitleHolder) holder).setLoadingIconColor(color);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 0)
            return TYPE_TITLE;
        else return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return 15;
    }

}
