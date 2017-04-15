package com.example.heyong.myreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.heyong.myreader.R;
import com.example.heyong.myreader.ui.ColorPickerDialog;
import com.example.heyong.myreader.ui.holder.ItemListHolder;

import static com.example.heyong.myreader.MyApplication.myApplication;

/**
 * Created by Heyong on 2017/4/8.
 * 主界面第三页
 */

public class ListRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private final int TYPE_1 = 0;//switch 可见
    private final int TYPE_2 = 1;//switch 不可见
    private final int DEFAULT_COUNT = 3;

    private static String[] titles = new String[]{
            "夜间模式",
            "主题",
            "..."
    };

    public ListRecyclerViewAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_main_list, parent, false);
        ItemListHolder holder = new ItemListHolder(itemView);
        if (viewType == TYPE_1) {
            holder.setSwitchVisible();
            holder.setSwitchChecked(myApplication.isNight());
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemListHolder listLolder = (ItemListHolder) holder;
        listLolder.setTitle(titles[position]);
        if (position == 0) {
            listLolder.setOnSwitchCheckedListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    myApplication.switchNightTheme();
                }
            });
        } else if (position == 1) {
            listLolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    context.startActivity(new Intent(context, ColorPickerActivity.class));
                    ColorPickerDialog dialog = new ColorPickerDialog(context,R.style.StyleDialog);
                    dialog.show();
                }
            });
        } else if (position == 2) {

        }
    }

    @Override
    public int getItemCount() {
        return DEFAULT_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_1;
        else return TYPE_2;
    }
}
