package com.example.heyong.eeyeswindow.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.heyong.eeyeswindow.Bean.HomeLectureBean;
import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.Tools.PxToDp;
import com.example.heyong.eeyeswindow.UI.Adapter.ActivityBeanRecyclerViewAdapter;
import com.example.heyong.eeyeswindow.UI.Adapter.LectureBeanRecyclerViewAdapter;
import com.example.heyong.eeyeswindow.UI.CustomView.EmptyRecyclerView;
import com.example.heyong.eeyeswindow.UI.CustomView.SearchDialog;
import com.example.heyong.lib.swipeBackActivity.SwipeBackActivity;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.heyong.eeyeswindow.UI.CustomView.SearchDialog.SUBMIT_TEXT;

public class SearchActivity extends SwipeBackActivity {
    static String TAG = "SearchActivity";


    String queryString;
    @BindView(R.id.tv_lecture)
    TextView tvLecture;
    @BindView(R.id.tv_activity)
    TextView tvActivity;
    @BindView(R.id.lv_lecture)
    EmptyRecyclerView lvLecture;
    @BindView(R.id.lv_activity)
    EmptyRecyclerView lvActivity;

    LectureBeanRecyclerViewAdapter lectureAdapter;
    ActivityBeanRecyclerViewAdapter activityAdapter;



    {
        lectureAdapter = new LectureBeanRecyclerViewAdapter(this);
        activityAdapter = new ActivityBeanRecyclerViewAdapter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setupHeader();
        setupContent();
    }

    private void setupContent() {
        tvLecture.setText("与\"" + queryString + "\"有关的讲座");
        tvActivity.setText("与\"" + queryString + "\"有关的活动");

        lvLecture.setLayoutManager(new LinearLayoutManager(this));
        lvLecture.setEmptyView(findViewById(R.id.empty1));
        lvLecture.setAdapter(lectureAdapter);
        lvLecture.setItemAnimator(new DefaultItemAnimator());

        lvActivity.setLayoutManager(new LinearLayoutManager(this));
        lvActivity.setEmptyView(findViewById(R.id.empty2));
        lvActivity.setAdapter(activityAdapter);
        lvActivity.setItemAnimator(new DefaultItemAnimator());

        List<HomeLectureBean> l = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            l.add(new HomeLectureBean());
        }
        lectureAdapter.addData(l);
    }

    private void setupHeader() {
        Intent i = getIntent();
        queryString = i.getStringExtra(SUBMIT_TEXT);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("搜索");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            int[] location = new int[2];
            View view = findViewById(R.id.toolbar);
            view.getLocationOnScreen(location);
            SearchDialog dialog = SearchDialog.buildInstance(this);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setWindowAnimations(R.style.dialogWindowAnim);
            window.setGravity(Gravity.LEFT | Gravity.TOP);
            params.x = location[0];
            params.y = location[1];
            params.height = PxToDp.dip2px(this,56);
            params.width = getWindowManager().getDefaultDisplay().getWidth();
            window.setAttributes(params);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }



}
