package com.example.heyong.eeyeswindow.UI.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.CustomView.EmptyRecyclerView;
import com.example.heyong.lib.swipeBackActivity.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更多产品
 */
public class MoreProductActivity extends SwipeBackActivity {

    @BindView(R.id.rv)
    EmptyRecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_product);
        ButterKnife.bind(this);
        setupHeader();
        rv.setNestedScrollingEnabled(false);
        rv.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view,null,false));
    }

    private void setupHeader() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("e瞳更多产品");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreProductActivity.this.finish();
            }
        });
    }
}
