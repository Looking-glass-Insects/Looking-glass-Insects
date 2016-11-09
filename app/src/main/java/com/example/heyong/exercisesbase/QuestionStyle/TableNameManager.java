package com.example.heyong.exercisesbase.QuestionStyle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.TableName;
import com.example.heyong.exercisesbase.MainActivity;
import com.example.heyong.exercisesbase.R;
import com.example.heyong.exercisesbase.StorageData.DatabaseManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Heyong on 2016/11/8.
 */
public class TableNameManager {

    MainActivity context;
    DatabaseManager dbManager;
    AlertDialog.Builder dialogBuilder;
    List<String> data;

    public TableNameManager(MainActivity context,List<String> data) {
        this.context = context;
        dbManager = new DatabaseManager(context, context.tableName);
        this.data = data;
    }



    private void buildDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.
                setTitle("更改科目")
                .setView(view)
                .setNegativeButton("取消", null);
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_table_name, null);
        ViewHolder holder = new ViewHolder(view, this);
        buildDialog(holder.view);
        dialogBuilder.show();
    }




    class ViewHolder {
        ListView listView;
        LinearLayout linearLayout;
        TableNameManager manager;
        final View view;
        //List<String> data;
        public ViewHolder(View view, final TableNameManager manager) {
            this.view = view;
            this.manager = manager;
            listView = (ListView) view.findViewById(R.id.dialog_table_name_lv);
            linearLayout = (LinearLayout) view.findViewById(R.id.dialog_table_name_ll);
            //data = new LinkedList<>();
            //manager.dbManager.readAllTable(data);
            listView.setAdapter(new ArrayAdapter<String>(manager.context, android.R.layout.simple_list_item_activated_1, manager.data));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder builder = new StringBuilder();
                    addNameDialog(builder);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0 && TableName.getPositionByTable(manager.context.tableName) != 0){
                        changeDialog(TableName.TAVLE_1);
                    }else if(position == 1 && TableName.getPositionByTable(manager.context.tableName) != 1 ){
                        changeDialog(TableName.TAVLE_2);
                    }else if(position == 2 && TableName.getPositionByTable(manager.context.tableName) != 2){
                        changeDialog(TableName.TAVLE_3);
                    }
                }
            });
        }


        private void addNameDialog(final StringBuilder title){
            LayoutInflater factory = LayoutInflater.from(manager.context);//提示框
            final View view = factory.inflate(R.layout.dialog_add_title, null);//这里必须是final的
            final EditText edit=(EditText)view.findViewById(R.id.et_title);//获得输入框对象
            DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_POSITIVE:
                            title.append(edit.getText() + "");
                            manager.context.getUserNameTv().setText(title);
                            manager.context.savePreferences();
                            List<String> l = manager.context.loadPreferences();
                            manager.data.clear();
                            for(String s:l)
                                manager.data.add(s);
                            ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();
                            break;
                    }
                }
            };
            new AlertDialog.Builder(context)
                    .setTitle("请输入:")//提示框标题
                    .setView(view)
                    .setPositiveButton("确定",dialogOnclicListener)
                    .setNegativeButton("取消",null).create().show();
        }

        private void changeDialog(final String toTableName){
            //dialog参数设置
            AlertDialog.Builder builder = new AlertDialog.Builder(manager.context, android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
            builder.setTitle("提示"); //设置标题
            builder.setMessage("是否改变科目，改变后将重启"); //设置内容
            builder.setIcon(R.drawable.user);//设置图标，图片id即可
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //manager.context.tableName = ToTableName;
                    manager.context.saveNew(toTableName);
                    Intent intent = new Intent(manager.context,MainActivity.class);
                    manager.context.finish();
                    manager.context.startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();

        }
    }






}
