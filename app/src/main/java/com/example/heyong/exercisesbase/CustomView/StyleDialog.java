package com.example.heyong.exercisesbase.CustomView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Bean.QuestionType;
import com.example.heyong.exercisesbase.Interface.Adapter.AddQueAdapter;
import com.example.heyong.exercisesbase.Interface.Adapter.DragListViewAdapter;
import com.example.heyong.exercisesbase.QuestionStyle.QuestionStyleManager;
import com.example.heyong.exercisesbase.R;
import com.example.heyong.exercisesbase.StorageData.DatabaseManager;
import com.example.heyong.exercisesbase.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StyleDialog extends Dialog {
    static final String TAG = "StyleDialog";
    private PagerTitleStrip styleTitle;
    private ViewPager mainViewPager;

    private Context context;
    private Button btn;//保存
    private List<View> viewList;// view数组
    private List<String> titleList;  //标题列表数组
    private View choose_view;//
    private View answer_view;
    private ListView choose_listView;
    private ListView answer_listView;
    private Button add_que;
    private DragSortListView choose_sdlv;
    private DragSortListView answer_sdlv;
    private QuestionStyleManager manager;


    private ViewHolder sheet;

    public StyleDialog(Context context, QuestionStyleManager manager) {
        super(context);
        build(context);
        this.manager = manager;
    }

    @Override
    public void show() {
        reBuild();
        super.show();
    }

    public StyleDialog(Context context, int theme, QuestionStyleManager manager) {
        super(context, theme);
        build(context);
        this.manager = manager;
    }

    public void reBuild() {
        DragListViewAdapter adapter = (DragListViewAdapter) choose_sdlv.getInputAdapter();
        adapter.clear();
        DragListViewAdapter adapter1 = (DragListViewAdapter) answer_sdlv.getInputAdapter();
        adapter1.clear();
        mainViewPager.setCurrentItem(0);

        DatabaseManager dbManager = new DatabaseManager(context);
        List<Note> choose_temp = new ArrayList<>();
        dbManager.readData(choose_temp, QuestionType.CHOOSE);

        List<Note> answer_temp = new ArrayList<>();
        dbManager.readData(answer_temp, QuestionType.ANSWER);

        AddQueAdapter choose = (AddQueAdapter) choose_listView.getAdapter();
        choose.clear();
        choose.add(choose_temp);

        AddQueAdapter answer = (AddQueAdapter) answer_listView.getAdapter();
        answer.clear();
        answer.add(answer_temp);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.reBuild();
    }

    private void build(Context context) {
        this.context = context;
        View layout = LayoutInflater.from(context).inflate(R.layout.style_dialog, null);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        this.setContentView(layout, new ViewGroup.LayoutParams(
                width, height));//添加布局

        initEle(layout);
        PagerAdapter pagerAdapter = new MyViewPagerAdapter();
        mainViewPager.setAdapter(pagerAdapter);
        addListener();
    }

    private void initEle(View layout) {
        btn = (Button) layout.findViewById(R.id.button);
        styleTitle = (PagerTitleStrip) layout.findViewById(R.id.style_title);
        mainViewPager = (ViewPager) layout.findViewById(R.id.main_viewPager);
        viewList = new ArrayList<>();
        choose_view = LayoutInflater.from(context).inflate(R.layout.style_list, null);
        answer_view = LayoutInflater.from(context).inflate(R.layout.style_list, null);
        choose_listView = (ListView) choose_view.findViewById(R.id.left_listView);
        choose_sdlv = (DragSortListView) choose_view.findViewById(R.id.right_dslv);
        answer_sdlv = (DragSortListView) answer_view.findViewById(R.id.right_dslv);
        answer_listView = (ListView) answer_view.findViewById(R.id.left_listView);

        choose_listView.setAdapter(new AddQueAdapter(new ArrayList<Note>(), context));

        answer_listView.setAdapter(new AddQueAdapter(new ArrayList<Note>(), context));

        viewList.add(choose_view);
        viewList.add(answer_view);
        titleList = new ArrayList<>();
        titleList.add("选择题");
        titleList.add("解答题");
        add_que = (Button) layout.findViewById(R.id.add_que);


        choose_sdlv.setAdapter(new DragListViewAdapter(context, new ArrayList<Note>()));
        answer_sdlv.setAdapter(new DragListViewAdapter(context, new ArrayList<Note>()));

        sheet = new ViewHolder(layout);

        //addTitleDialog = new AddTitleDialog(this.context,R.style.StyleDialog);
        reBuild();
    }

    private void addListener() {
        add_que.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = mainViewPager.getCurrentItem();
                if (position == 0) {
                    //Toast.makeText(context,"position" + 0,Toast.LENGTH_SHORT).show();
                    AddQueAdapter addQueAdapter = (AddQueAdapter) choose_listView.getAdapter();
                    List<Note> list = addQueAdapter.getCheckedNotes();
                    DragListViewAdapter adapter = (DragListViewAdapter) choose_sdlv.getInputAdapter();
                    for (Note n : list) {
                        adapter.add(n);
                    }
                } else if (position == 1) {
                    //Toast.makeText(context,"position" + 1,Toast.LENGTH_SHORT).show();
                    AddQueAdapter addQueAdapter = (AddQueAdapter) answer_listView.getAdapter();
                    List<Note> list = addQueAdapter.getCheckedNotes();
                    DragListViewAdapter adapter = (DragListViewAdapter) answer_sdlv.getInputAdapter();
                    for (Note n : list) {
                        adapter.add(n);
                    }
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        choose_sdlv.setDropListener(new MyDropListener(choose_sdlv));
        choose_sdlv.setRemoveListener(new MyRemoveListener(choose_sdlv));
        answer_sdlv.setDropListener(new MyDropListener(answer_sdlv));
        answer_sdlv.setRemoveListener(new MyRemoveListener(answer_sdlv));
        choose_sdlv.setOnItemClickListener(new MyOnItemClickListener(choose_sdlv));
        answer_sdlv.setOnItemClickListener(new MyOnItemClickListener(answer_sdlv));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加标题
                List<Note> temp = new ArrayList<>();
                List<Note> l = ((DragListViewAdapter) choose_sdlv.getInputAdapter()).getData();
                List<Note> l1 = ((DragListViewAdapter) answer_sdlv.getInputAdapter()).getData();
                for (int i = 0; i < l.size(); i++) {
                    temp.add(l.get(i));
                }
                for (int i = 0; i < l1.size(); i++) {
                    temp.add(l1.get(i));
                }
                StringBuilder title = new StringBuilder();
                addTitleDialog(title,temp);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            sheet.dismiss();
            alterDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean alterDialog() {
        boolean flag = false;
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        StyleDialog.this.dismiss();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认放弃编辑"); //设置内容
        builder.setIcon(R.drawable.new_note_icon);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
        return flag;
    }


    private void addTitleDialog(final StringBuilder title,final List temp){
        LayoutInflater factory = LayoutInflater.from(this.context);//提示框
        final View view = factory.inflate(R.layout.dialog_add_title, null);//这里必须是final的
        final EditText edit=(EditText)view.findViewById(R.id.et_title);//获得输入框对象
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        title.append(edit.getText()+"");
                        manager.saveData(temp,title.toString());
                        manager.freshUI(temp);
                        StyleDialog.this.dismiss();
                        StyleDialog.this.dismiss();
                        break;
                }
            }
        };
        new AlertDialog.Builder(context)
                .setTitle("请输入试卷标题")//提示框标题
                .setView(view)
                .setPositiveButton("确定",dialogOnclicListener).create().show();
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        DragSortListView dslv;

        public MyOnItemClickListener(DragSortListView dslv) {
            this.dslv = dslv;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DragListViewAdapter adapter = (DragListViewAdapter) dslv.getInputAdapter();
            Note n = adapter.getData().get(position);
            sheet.show(n);
        }
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SpannableStringBuilder ssb = new SpannableStringBuilder("  " + titleList.get(position));
            ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.orangered));// 字体颜色设置为绿色
            Drawable image = context.getResources().getDrawable(R.drawable.leaf);
            image.setBounds(0, 0, 50, 50);
            ImageSpan is = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
            ssb.setSpan(is, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(fcs, 1, ssb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);// 设置字体颜色
            ssb.setSpan(new RelativeSizeSpan(1.2f), 1, ssb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }
    }

    class MyDropListener implements DragSortListView.DropListener {
        DragSortListView dslv;

        public MyDropListener(DragSortListView dslv) {
            this.dslv = dslv;
        }

        @Override
        public void drop(int from, int to) {
            if (from == to) return;
            DragListViewAdapter adapter = (DragListViewAdapter) dslv.getInputAdapter();
            Note item = adapter.remove(from);//在适配器中”原位置“的数据。
            adapter.add(to, item);//在目标位置中插入被拖动的控件。
            //adapter.switchItem(from,to);
            //choose_sdlv.moveCheckState(from,to);
            //Toast.makeText(context,"drop-->from"+from+"to"+to,Toast.LENGTH_SHORT).show();
        }
    }

    class MyRemoveListener implements DragSortListView.RemoveListener {
        DragSortListView dslv;

        public MyRemoveListener(DragSortListView dslv) {
            this.dslv = dslv;
        }

        @Override
        public void remove(int which) {
            DragListViewAdapter adapter = (DragListViewAdapter) dslv.getInputAdapter();
            adapter.remove(which);
        }
    }

    class ViewHolder {
        LinearLayout sheet;
        EditText queET;
        EditText ansET;
        ImageView arrow;

        public ViewHolder(View layout) {
            sheet = (LinearLayout) layout.findViewById(R.id.style_dialog_sheet_linearLayout);
            queET = (EditText) layout.findViewById(R.id.style_dialog_questionText);
            ansET = (EditText) layout.findViewById(R.id.style_dialog_answerText);
            arrow = (ImageView) layout.findViewById(R.id.arrow_imageView);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder.this.dismiss();
                }
            });
        }


        public void show(Note data) {
            queET.setText(data.getQuestion());
            ansET.setText(data.getAnswer());
            sheet.setVisibility(View.VISIBLE);
        }

        public void dismiss() {
            sheet.setVisibility(View.GONE);
            queET.setText("");
            ansET.setText("");
        }
    }
}
