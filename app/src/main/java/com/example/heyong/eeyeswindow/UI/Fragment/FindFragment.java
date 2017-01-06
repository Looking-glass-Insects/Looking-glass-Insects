package com.example.heyong.eeyeswindow.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.eeyeswindow.UI.CustomView.FlowLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.heyong.eeyeswindow.Tools.PxToDp.dip2px;

/**
 *
 *
 */
public class FindFragment extends Fragment {


    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        int ranHeight = dip2px(this.getContext(), 30);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, ranHeight);
        lp.setMargins(dip2px(this.getContext(), 10), 0, dip2px(this.getContext(), 10), 0);
        TextView tv = new TextView(this.getContext());
        tv.setPadding(dip2px(this.getContext(), 15), 0, dip2px(this.getContext(), 15), 0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        //int index = (int)(Math.random() * length);
        tv.setText("hello");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setLines(1);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FindFragment.this.getContext(), "hello", Toast.LENGTH_SHORT).show();
            }
        });
        tv.setBackgroundResource(R.drawable.flow_bg);
        flowlayout.addView(tv,lp);
    }


}
