package com.example.heyong.exercisesbase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.heyong.exercisesbase.Bean.Bean;
import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.CustomView.ModelActivityDialog;
import com.example.heyong.exercisesbase.CustomView.RightSlideCloseFrameLayout;
import com.example.heyong.exercisesbase.Interface.Adapter.ModelSimpleAdapter;
import com.example.heyong.exercisesbase.StorageData.ModelFileManager;
import com.example.heyong.exercisesbase.Tools.PopupList;
import com.example.heyong.exercisesbase.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 模板库
 */
public class ModelActivity extends Activity {
    public static final String action = "freshUI";//广播
    @BindView(R.id.model_lv)
    ListView modelListView;
    @BindView(R.id.model_root)
    RightSlideCloseFrameLayout modelRoot;
    private ModelFileManager manager;
    private ModelActivityDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        modelRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startx = 0, endx = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    startx = event.getX();
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    endx = event.getX();
                    if (endx - startx > 50)
                        ModelActivity.this.finish();
                }
                return false;
            }
        });
        manager = new ModelFileManager(this);
        List<Bean> list = new ArrayList<>();
        manager.readModel(list);
        ModelSimpleAdapter adapter = new ModelSimpleAdapter(this, list);
        modelListView.setAdapter(adapter);
        bindPop(modelListView);
        List<Note> l = new ArrayList<>();
        dialog = new ModelActivityDialog(this,l,R.style.StyleDialog);
        modelListView.setOnItemClickListener(new MainListViewOnItemClickListener(modelListView));
    }


    private void bindPop(ListView lv_main) {
        PopupList popupList = new PopupList();
        List<String> popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("delete");
        popupList.init(this, lv_main, popupMenuItemList, new PopupList.OnPopupListClickListener() {
            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
//                sheet.setVisibility(View.GONE);
                //setSheetGone();
                deleteDialog(contextPosition);
            }
        });
    }
    //deleteDialog
    private void deleteDialog(final int position) {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        deletNote(position);
                        //Toast.makeText(getContext(), "确认" + which, Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        //Toast.makeText(getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
                        break;
//                    case Dialog.BUTTON_NEUTRAL:
//                        Toast.makeText(getContext(), "忽略" + which, Toast.LENGTH_SHORT).show();
//                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认删除(删除后不可恢复)"); //设置内容
        builder.setIcon(R.drawable.wood_delete);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        // builder.setNeutralButton("忽略", dialogOnclicListener);
        builder.create().show();
    }




    //数据存储
    private void deletNote(int position) {
        ModelSimpleAdapter adapter = (ModelSimpleAdapter) modelListView.getAdapter();
        Bean bean = adapter.remove(position);
        manager.delModel(bean.getDate());
    }

    public void updateModelData(String content,int position){
        ModelSimpleAdapter adapter = (ModelSimpleAdapter)modelListView.getAdapter();
        adapter.updateData(content,position);
        manager.updateModel((Bean)adapter.getItem(position));
    }





    class MainListViewOnItemClickListener implements AdapterView.OnItemClickListener{
        private ListView lv;

        public MainListViewOnItemClickListener(ListView lv) {
            this.lv = lv;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ModelSimpleAdapter adapter = (ModelSimpleAdapter)modelListView.getAdapter();
            Bean b = (Bean)adapter.getItem(position);
            //Log.i("Test",b.getContent());
            List<Note> data = manager.getNotesList(b.getContent());
            dialog.freshData(data);
            dialog.show(position);
        }
    }
}
