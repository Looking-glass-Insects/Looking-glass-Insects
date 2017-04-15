package com.example.heyong.myreader.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.heyong.myreader.R;
import com.github.danielnilsson9.colorpickerview.view.ColorPickerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.heyong.myreader.MyApplication.myApplication;

/**
 * Created by Heyong on 2017/4/14.
 */

public class ColorPickerDialog extends Dialog {

    @BindView(R.id.color_picker_view)
    ColorPickerView colorPickerView;
    @BindView(R.id.btn_positive)
    Button btnPositive;
    @BindView(R.id.btn_negative)
    Button btnNegative;

    public ColorPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null, false);
        this.setContentView(view);
        ButterKnife.bind(this, view);
        colorPickerView.setColor(myApplication.getThemeColor());
    }


    @OnClick({R.id.btn_positive, R.id.btn_negative})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_positive:
                this.dismiss();
                myApplication.changeThemeColor(Color.parseColor(String.format("#%06X", 0xFFFFFFFF & colorPickerView.getColor())));
                break;
            case R.id.btn_negative:
                this.dismiss();
                break;
        }
    }
}
