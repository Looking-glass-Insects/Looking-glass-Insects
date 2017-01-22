package com.example.heyong.eeyeswindow.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.CustomView.SearchPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.heyong.lib.app.SwipeBackActivity;

public class SearchActivity extends SwipeBackActivity {
    static String TAG = "SearchActivity";


    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupHeader();
    }

    private void setupHeader() {
        Intent i = getIntent();
        String query = i.getStringExtra(SearchPopupWindow.SUBMIT_TEXT);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(query);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        // Log.i(TAG,query);
    }


}
