package com.example.heyong.exercisesbase.CustomView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Interface.Adapter.DragListViewAdapter;
import com.example.heyong.exercisesbase.ModelActivity;
import com.example.heyong.exercisesbase.R;
import com.example.heyong.exercisesbase.StorageData.ModelFileManager;
import com.example.heyong.exercisesbase.dslv.DragSortListView;

import java.io.Serializable;
import java.util.List;

import butterknife.OnClick;

public class ModelActivityDialog extends Dialog implements View.OnClickListener{
    private ModelActivity activity;
    private DragSortListView modelActivityDSLV;
    private RightSlideCloseFrameLayout root;
    private Button btn;//保存并预览
    private int currPosition = -1;//外部
    private LinearLayout sheet;
    private EditText queET;
    private EditText ansET;
    private ImageView arrowIV;
    private int currClickPosition = -1;
    //private List<Note> data;
    public ModelActivityDialog(ModelActivity activity,@Nullable List<Note> data ,int theme) {
        super(activity,theme);
        build(activity,data);
    }
    public void sendBrocast(){
        Intent intent = new Intent(ModelActivity.action);
        List<Note> data = ((DragListViewAdapter)modelActivityDSLV.getInputAdapter()).getData();
        intent.putExtra("Notes",(Serializable)data);
        activity.sendBroadcast(intent);
    }

    public void show(int currPosition) {
        this.currPosition = currPosition;
        super.show();
    }

    /**
     *
     * @param flag 是否保存
     */
    public void dismiss(boolean flag) {
        if(flag){
            ModelFileManager manager = new ModelFileManager(activity);
            List<Note> data = ((DragListViewAdapter)modelActivityDSLV.getInputAdapter()).getData();
            String content = manager.makeContent(data);
            activity.updateModelData(content,currPosition);
        }
        super.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            modelActivityDSLV.setDragEnabled(true);
            if(sheet.getVisibility() == View.VISIBLE){
                sheet.setVisibility(View.GONE);//不保存
                return true;
            }
            alterDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void build(ModelActivity activity,List<Note> data) {
        this.activity = activity;
        View layout = LayoutInflater.from(activity).inflate(R.layout.model_activity_dialog, null);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        this.setContentView(layout, new ViewGroup.LayoutParams(
                width, height));
        initEle(layout,data);
    }
    private boolean alterDialog() {
        boolean flag = false;
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ModelActivityDialog.this.dismiss(true);
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        ModelActivityDialog.this.dismiss(false);
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否保存修改"); //设置内容
        builder.setIcon(R.drawable.new_note_icon);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
        return flag;
    }
    private void initEle(View layout,List<Note> data) {
        modelActivityDSLV = (DragSortListView)layout.findViewById(R.id.model_dialog_dslv);
        modelActivityDSLV.setAdapter(new DragListViewAdapter(activity, data));
        modelActivityDSLV.setDropListener(new MyDropListener(modelActivityDSLV));
        modelActivityDSLV.setRemoveListener(new MyRemoveListener(modelActivityDSLV));
        modelActivityDSLV.setOnItemClickListener(new DSLVOnClickListener(modelActivityDSLV));
        root = (RightSlideCloseFrameLayout)layout.findViewById(R.id.model_dialog_root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startx = 0, endx = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    startx = event.getX();
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    endx = event.getX();
                    if (endx - startx > 50)
                        ModelActivityDialog.this.alterDialog();
                }
                return false;
            }
        });
        btn = (Button)layout.findViewById(R.id.model_dialog_OK);
        btn.setOnClickListener(new View.OnClickListener() {//保存并预览
            @Override
            public void onClick(View v) {
                sendBrocast();
                ModelActivityDialog.this.dismiss(true);
                activity.finish();
            }
        });


        sheet = (LinearLayout)layout.findViewById(R.id.model_dialog_sheet_linearLayout);
        queET = (EditText)layout.findViewById(R.id.model_dialog_questionText);
        ansET = (EditText)layout.findViewById(R.id.model_dialog_answerText);
        arrowIV = (ImageView)layout.findViewById(R.id.arrow_imageView);
        arrowIV.setOnClickListener(this);
    }




    public void freshData(List<Note> notes){
        DragListViewAdapter adapter = (DragListViewAdapter)modelActivityDSLV.getInputAdapter();
        adapter.clear();
        for(Note n:notes){
            adapter.add(n);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow_imageView://保存
                sheet.setVisibility(View.GONE);
                modelActivityDSLV.setDragEnabled(true);
                if(currClickPosition == -1){
                    new IllegalArgumentException("currClickPosition shouldn't be -1");
                }
                DragListViewAdapter adapter = (DragListViewAdapter)modelActivityDSLV.getInputAdapter();
                Note note = (Note)adapter.getItem(currClickPosition);
                note.setQuestion(queET.getText()+"");
                note.setAnswer(ansET.getText()+"");
                adapter.notifyDataSetChanged();
                currClickPosition = -1;
                break;
            default:
                break;
        }
    }

    class MyDropListener implements DragSortListView.DropListener {
        DragSortListView dslv;
        public MyDropListener(DragSortListView dslv){
            this.dslv = dslv;
        }
        @Override
        public void drop(int from, int to) {
            if(from == to)return;
            DragListViewAdapter adapter = (DragListViewAdapter)dslv.getInputAdapter();
            Note item = adapter.remove(from);//在适配器中”原位置“的数据。
            adapter.add(to,item);//在目标位置中插入被拖动的控件。
        }
    }
    class MyRemoveListener implements DragSortListView.RemoveListener{
        DragSortListView dslv;
        public MyRemoveListener(DragSortListView dslv){
            this.dslv = dslv;
        }
        @Override
        public void remove(int which) {
            DragListViewAdapter adapter = (DragListViewAdapter)dslv.getInputAdapter();
            adapter.remove(which);
        }
    }
    class DSLVOnClickListener implements AdapterView.OnItemClickListener{
        private DragSortListView dslv;

        public DSLVOnClickListener(DragSortListView dslv) {
            this.dslv = dslv;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(activity, "click"+position, Toast.LENGTH_SHORT).show();
            dslv.setDragEnabled(false);
            currClickPosition = position;
            Note note = (Note)(modelActivityDSLV.getInputAdapter()).getItem(position);
            queET.setText(note.getQuestion());
            ansET.setText(note.getAnswer());
            sheet.setVisibility(View.VISIBLE);
        }
    }

}
