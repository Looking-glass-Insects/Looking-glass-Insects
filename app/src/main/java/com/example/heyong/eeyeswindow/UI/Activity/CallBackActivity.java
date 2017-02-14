package com.example.heyong.eeyeswindow.UI.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heyong.eeyeswindow.R;
import com.example.heyong.lib.swipeBackActivity.SwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class CallBackActivity extends SwipeBackActivity implements View.OnTouchListener {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.card_submit)
    CardView cardSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back);
        ButterKnife.bind(this);
        setupHeader();
        etContent.setOnTouchListener(this);
    }


    private void setupHeader() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("意见反馈");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallBackActivity.this.finish();
            }
        });

    }

    @OnClick(R.id.card_submit)
    public void onClick() {
        final String content = etContent.getText().toString();
        Snackbar.make(cardSubmit, "提交成功（●´∀‘）ノ♡ ", Snackbar.LENGTH_SHORT)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CallBackActivity.this, content, Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if ((view.getId() == R.id.et_content && canVerticalScroll(etContent))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }


    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
