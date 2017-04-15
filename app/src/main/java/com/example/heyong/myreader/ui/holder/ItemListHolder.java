package com.example.heyong.myreader.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.heyong.myreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Heyong on 2017/4/8.
 */

public class ItemListHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rl_list_root)
    RelativeLayout root;
    @BindView(R.id.iv_list)
    ImageView ivListIcon;
    @BindView(R.id.tv_list)
    TextView tvListName;
    @BindView(R.id.item_switch)
    Switch itemSwitch;

    public ItemListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setOnClickListener(View.OnClickListener listener){
        root.setOnClickListener(listener);
    }

    public void setSwitchVisible(){
        itemSwitch.setVisibility(View.VISIBLE);
    }


    public void setOnSwitchCheckedListener(CompoundButton.OnCheckedChangeListener listener){
        itemSwitch.setOnCheckedChangeListener(listener);
    }
    public void setSwitchChecked(boolean checked){
        itemSwitch.setChecked(checked);
    }

    public void setTitle(String title){
        tvListName.setText(title);
    }

}
