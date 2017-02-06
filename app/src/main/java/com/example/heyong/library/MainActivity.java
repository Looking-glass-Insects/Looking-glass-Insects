package com.example.heyong.library;

import android.os.Bundle;
import android.widget.TextView;

import com.example.heyong.library.customView.flowLayout.FlowLayout;
import com.example.heyong.library.customView.swipeBackActivity.SwipeBackActivity;

public class MainActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow);
        TextView tv = new TextView(this);
        tv.setText("hello");
        flowLayout.addView(tv);
    }
}
