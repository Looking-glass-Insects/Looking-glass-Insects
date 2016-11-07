package com.example.heyong.exercisesbase;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Interface.Adapter.NoteAdapter;
import com.example.heyong.exercisesbase.StorageData.DatabaseManager;
import com.example.heyong.exercisesbase.Tools.PopupList;
import com.example.heyong.exercisesbase.Bean.QuestionType;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;


import java.util.ArrayList;
import java.util.List;



public class QuestionManage extends Activity {
    private TextView titleTV;
    private DatabaseManager dbManager;//数据操作
    private List<Note> data;//问题数据
    private ListView listView;
    private ImageButton addQuestion;
    private LinearLayout sheet;
    private int id = -1;

    private EditText questionText;
    private RadioGroup radioGroup;
    private EditText answerText;
    private FrameLayout root;//右滑关闭
    private ImageButton deleteAll;
    private QuestionType currentTag;//当前显示的问题类型
    private RelativeLayout choose_relative;//下方
    private RelativeLayout answer_relative;
    private ImageView arrow;//点击隐藏
    private Integer dismissPosition;
    static final String TAG = "QuestionManage";
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_manage);
        listView = (ListView) findViewById(R.id.manage_listView);
        addQuestion = (ImageButton) findViewById(R.id.addQuestion_imageButton);
        questionText = (EditText) findViewById(R.id.questionText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        answerText = (EditText) findViewById(R.id.answerText);
        sheet = (LinearLayout) findViewById(R.id.sheet_linearLayout);
        root = (FrameLayout) findViewById(R.id.root_frameLayout);
        deleteAll = (ImageButton) findViewById(R.id.deleteAll_imageButton);
        titleTV = (TextView) findViewById(R.id.tittle_TextView);
        choose_relative = (RelativeLayout) findViewById(R.id.que_manage_choose_relative);
        answer_relative = (RelativeLayout) findViewById(R.id.que_manage_answer_relative);
        arrow = (ImageView)findViewById(R.id.arrow_imageView);
        Intent intent = getIntent();
        String tableName = intent.getStringExtra("table_name");
        assert tableName != null : "题库名为null";
        dbManager = new DatabaseManager(this,tableName);
        data = new ArrayList<>();
        dbManager.readData(data, QuestionType.CHOOSE,null);
        currentTag = QuestionType.CHOOSE;
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSheetGone();
                alterDialog();
                //setSheetGone();
            }
        });
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//右滑关闭
                float startx = 0, endx = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    startx = event.getX();
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    endx = event.getX();
                    if (endx - startx > 50)
                        QuestionManage.this.finish();
                }
                return false;
            }
        });
        bindAdapter();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                questionText.setText(data.get((int) id).getQuestion());
                switch (data.get((int) id).getAnswer()) {
                    case "A":
                        RadioButton btn = (RadioButton) findViewById(R.id.radioA);
                        btn.setChecked(true);
                        break;
                    case "B":
                        RadioButton btnB = (RadioButton) findViewById(R.id.radioB);
                        btnB.setChecked(true);
                        break;
                    case "C":
                        RadioButton btnC = (RadioButton) findViewById(R.id.radioC);
                        btnC.setChecked(true);
                        break;
                    case "D":
                        RadioButton btnD = (RadioButton) findViewById(R.id.radioD);
                        btnD.setChecked(true);
                        break;
                    default:
                        answerText.setText(data.get((int) id).getAnswer());
                }
                setSheetVisible(currentTag);
                QuestionManage.this.id = (int) id;
            }
        });
        bindPop(listView);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionText.setText("");
                answerText.setText("");
                QuestionManage.this.id = -1;
                RadioButton btn = (RadioButton) findViewById(R.id.radioA);
                btn.setChecked(true);
//                sheet.setVisibility(View.VISIBLE);
                setSheetVisible(currentTag);
            }
        });
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllDialog(0);
            }
        });
        choose_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTag == QuestionType.CHOOSE) return;
                currentTag = QuestionType.CHOOSE;
                Toast.makeText(QuestionManage.this, "选择题，共" + dbManager.allCaseNum(currentTag,null) + "道", Toast.LENGTH_SHORT).show();
                data.clear();
                dbManager.readData(data, QuestionType.CHOOSE,null);
                initUI(0);

            }
        });
        answer_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTag == QuestionType.ANSWER) return;
                currentTag = QuestionType.ANSWER;
                Toast.makeText(QuestionManage.this, "简答题,共" + dbManager.allCaseNum(currentTag,null) + "道", Toast.LENGTH_SHORT).show();
                data.clear();
                dbManager.readData(data, QuestionType.ANSWER,null);
                initUI(0);
            }
        });
    }
    //绑定动态效果
    private void bindAdapter() {
        NoteAdapter noteAdapter = new NoteAdapter(this, data);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(noteAdapter);//外部依赖项
        OnDismissCallback myOnDismissCallback = new OnDismissCallback() {
            @Override
            public void onDismiss(ViewGroup viewGroup, int[] ints) {
                for (int i : ints) {
                    //Note t = data.remove(i);
                    //Log.i(TAG,t.getQuestion());
                    //initUI(0);
                    initUI(0);
                }
            }
        };
        SwipeDismissAdapter adapter = new SwipeDismissAdapter(animationAdapter, myOnDismissCallback);
        adapter.setAbsListView(listView);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(adapter);
    }

    private void setSheetVisible(QuestionType currentTag) {
        if (currentTag == QuestionType.CHOOSE)
            radioGroup.setVisibility(View.VISIBLE);
        else if (currentTag == QuestionType.ANSWER)
            answerText.setVisibility(View.VISIBLE);
        sheet.setVisibility(View.VISIBLE);
    }

    private void setSheetGone() {
        sheet.setVisibility(View.GONE);
        answerText.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (sheet.getVisibility() == View.GONE)
                this.finish();
            else if (sheet.getVisibility() == View.VISIBLE) {
                setSheetGone();
                alterDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initUI(int i) {
        Message msg = new Message();
        //设置一个标志
        msg.what = 0;
        //通过handler向UI线程发送一个消息
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    BaseAdapter adapter = (BaseAdapter) listView.getAdapter();
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    private void bindPop(ListView lv_main) {
        PopupList popupList = new PopupList();
        List<String> popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("delete");
        popupList.init(this, lv_main, popupMenuItemList, new PopupList.OnPopupListClickListener() {
            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
//                sheet.setVisibility(View.GONE);
                setSheetGone();
                deleteDialog(contextPosition);
            }
        });
    }



    private void deletNote(int position) {
        Note n = data.get(position);
        if (n.getId() == -1) {
            dbManager.deleteNote(n.getQuestion(), currentTag,null);
        } else {
            dbManager.deleteNote(n.getId(), currentTag,null);
        }
        SwipeDismissAdapter adapter = (SwipeDismissAdapter) listView.getAdapter();
        data.remove(position);
        adapter.dismiss(position);
        //initUI(0);
        //
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    }

    private boolean alterDialog() {
        boolean flag = false;
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        String question = questionText.getText().toString();
                        int _id = radioGroup.getCheckedRadioButtonId();
                        String answer = getAnswer(currentTag, _id);
                        if (id == -1) { // 新数据
                            Note n = new Note(question, answer);
                            data.add(n);
                            dbManager.writeQuestion(question, answer, currentTag,null);
                            initUI(0);
                        } else {
                            Note temp = data.get(QuestionManage.this.id);
                            int id = temp.getId();
                            temp.setQuestion(question);
                            temp.setAnswer(answer);
                            initUI(0);
                            dbManager.updateNote(id, question, answer, currentTag);
                        }
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认修改"); //设置内容
        builder.setIcon(R.drawable.new_note_icon);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
        return flag;
    }

    private String getAnswer(QuestionType currentTag, int _id) {
        String answer = "";
        if (currentTag == QuestionType.CHOOSE) {
            switch (_id) {
                case R.id.radioA:
                    answer = "A";
                    break;
                case R.id.radioB:
                    answer = "B";
                    break;
                case R.id.radioC:
                    answer = "C";
                    break;
                case R.id.radioD:
                    answer = "D";
                    break;
                default:
            }
        } else if (currentTag == QuestionType.ANSWER) {
            answer = answerText.getText() + "";
        }
        return answer;
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

    //删除全部
    private void deleteAllDialog(final int position) {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        dbManager.deleteAll(currentTag,null);
                        data.clear();
                        initUI(0);
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
        builder.setTitle("警告"); //设置标题
        builder.setMessage("是否确认删除所有题目(不可恢复)"); //设置内容
        builder.setIcon(R.drawable.wood_delete);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        // builder.setNeutralButton("忽略", dialogOnclicListener);
        builder.create().show();
    }
}


