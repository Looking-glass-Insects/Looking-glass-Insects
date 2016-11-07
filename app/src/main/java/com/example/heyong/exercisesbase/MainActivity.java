package com.example.heyong.exercisesbase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heyong.exercisesbase.Bean.Note;
import com.example.heyong.exercisesbase.Bean.QuestionType;
import com.example.heyong.exercisesbase.Bean.UserAttr;
import com.example.heyong.exercisesbase.CustomView.MySlidingPaneLayout;
import com.example.heyong.exercisesbase.Fragment.StudentViewPagerFragment;
import com.example.heyong.exercisesbase.Interface.Adapter.ViewPagerAdapter;
import com.example.heyong.exercisesbase.Fragment.ViewPagerFragment;
import com.example.heyong.exercisesbase.MyThread.QuestionLoaderThread;
import com.example.heyong.exercisesbase.QuestionStyle.QuestionStyleManager;
import com.example.heyong.exercisesbase.StorageData.DatabaseManager;
import com.example.heyong.exercisesbase.StorageData.FilesWriter;
import com.example.heyong.exercisesbase.StorageData.ModelFileManager;
import com.example.heyong.exercisesbase.Tools.DemoFileWriter;
import com.example.heyong.exercisesbase.QuestionStyle.Typer;
import com.example.heyong.exercisesbase.Tools.FileFinder;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * viewPager
 * fragment
 * slildingPaneLayout
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener, MySlidingPaneLayout.PanelSlideListener {
    private final String TAG = "MainActivity";
    private SlidingPaneLayout slidingPaneLayout;
    //private ListView slidingList;
    private ViewPager viewPager;
    private List<Fragment> content;//the content viewPager loading
    private RelativeLayout relativeLayout;//used as button
    //private ViewPagerFragment viewPagerFragment_;
    //private DatabaseManager dbManager;//数据操作
    private List<Note> data;//问题数据
    //private int questionsCount;//显示问题数量
    //private SharedPreferences sp;//参数设置
    //private EditText qustionCountEditText;////
    private LinearLayout manage_relativeLayout;//隐藏布局
    private LinearLayout externalFileRule;//外部文件导入规则说明
    private LinearLayout externalLayout;
    private LinearLayout queStyleLayout;
    private QuestionStyleManager styleManager;//编辑试题
    private RelativeLayout modelRelativeLayout;
    //private int _position;//位置
    private RelativeLayout outputRL;//导出

    private LinearLayout userLL;//登陆用户的属性
    private UserAttr attr = UserAttr.MANAGER;//默认为管理员

    public String tableName = "试题库1";
    private LinearLayout tableNameLL;//试题库名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_pane_layout);
        init();
        startFindFileThread();
        setAdapter();
        setListener();
        QuestionLoaderThread loader = new QuestionLoaderThread(this,this.tableName);
        loader.execute();
        IntentFilter filter = new IntentFilter(ModelActivity.action);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //广播,接收更改的数据
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Note> l =
                    (List<Note>) intent.getSerializableExtra("Notes");
            if (l == null) {
                new NullPointerException("the list from ModelActivity is a NULL object");
            }
            initUI(0, l);
        }
    };


    private void loadPreferences(){
        SharedPreferences sp = getSharedPreferences("table_name", Context.MODE_PRIVATE);
        this.tableName = sp.getString("table_name", null);
        if(tableName == null || tableName.equals(""))
            tableName = "未命名";
    }

    private void savePreferences(String tableName){
        SharedPreferences sp = getSharedPreferences("table_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("table_name", tableName);
        editor.commit();
    }
    /**
     * 导入外部文件
     */
    public static final String rootPath = Environment.getExternalStorageDirectory().toString() + File.separator;

    private void startFindFileThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> files = FileFinder.findFile();
                ListView l = (ListView) MainActivity.this.findViewById(R.id.founded_files);
                l.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, files));
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String s = ((TextView) view).getText().toString();
                        s = rootPath + s;
                        alterDialog(s);
//                        Toast.makeText(MainActivity.this, "即将导入"+s, Toast.LENGTH_SHORT).show();
//                        new QuestionLoaderThread(MainActivity.this).execute(rootPath+s);
                    }
                });
                initUI(1);
                Log.i(TAG, "run");
            }
        }).start();
    }


    /**
     * 刷新UI
     */
    private void initUI(int i) {
        Message msg = new Message();
        //设置一个标志
        msg.what = i;
        //通过handler向UI线程发送一个消息
        handler.sendMessage(msg);
    }

    /**
     * @param flag 是否重新排版
     * @param i
     */
    private void initUI(int flag, int i) {
        Message msg = new Message();
        //设置一个标志
        msg.what = i;
        msg.arg1 = flag;//一定为非零,不排版
        //通过handler向UI线程发送一个消息
        handler.sendMessage(msg);
    }

    /**
     * @param i
     * @param dataToShow 要显示
     */
    public void initUI(int i, List<Note> dataToShow) {
        data.clear();
        for (Note n : dataToShow) {
            data.add(n);
        }
        initUI(i);
    }

    //Don't use it directly;
    private void addFragment(boolean flag) {
        if (flag) {
            Typer.typeSetting(data);
        }
        for (int i = 0; i < data.size(); i++) {
            if (attr == UserAttr.MANAGER) {
                ViewPagerFragment f = ViewPagerFragment.getInstance(data.get(i));
                content.add(f);
            } else {
                StudentViewPagerFragment f = StudentViewPagerFragment.getInstance(data.get(i));
                content.add(f);
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //设置adapter
                    content.clear();
                    if (msg.arg1 != 0)
                        addFragment(false);
                    else
                        addFragment(true);
                    viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), content, MainActivity.this));
                    break;
                case 1://外部文件
                    ListView l = (ListView) MainActivity.this.findViewById(R.id.founded_files);
                    BaseAdapter adapter = (BaseAdapter) l.getAdapter();
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    //初始化
    private void init() {
        DemoFileWriter writer = new DemoFileWriter(this);
        writer.write(); //写一个示例文件
        //dbManager = new DatabaseManager(this);
        ModelFileManager manager = new ModelFileManager(this,this.tableName);
        styleManager = new QuestionStyleManager(this);
        data = manager.getFirstNotes();
        if (data == null) {
            data = new ArrayList<>();
        }
        queStyleLayout = (LinearLayout) findViewById(R.id.que_style_linearLayout);
        //Typer.typeSetting(data);
        slidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slidingPanelLayout);
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);
        content = new ArrayList<>();
        addFragment(true);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout_1);
        manage_relativeLayout = (LinearLayout) findViewById(R.id.manage_relativeLayout);
        externalFileRule = (LinearLayout) findViewById(R.id.externalFileRule);
        externalLayout = (LinearLayout) findViewById(R.id.externalFile_LinearLayout);
        modelRelativeLayout = (RelativeLayout) findViewById(R.id.model_relativeLayout);
        outputRL = (RelativeLayout) findViewById(R.id.relativelayout_2);
        userLL = (LinearLayout) findViewById(R.id.ll_left_user);
        tableNameLL = (LinearLayout) findViewById(R.id.ll_left_table_name);
    }

    private void setAdapter() {
        //slidingList.setAdapter(new SlidingListAdapter(this,data));
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), content, this));
    }

    private void setListener() {
        slidingPaneLayout.setPanelSlideListener(this);
        relativeLayout.setOnClickListener(this);
        manage_relativeLayout.setOnClickListener(this);
        externalLayout.setOnClickListener(this);
        externalFileRule.setOnClickListener(this);
        queStyleLayout.setOnClickListener(this);
        modelRelativeLayout.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                //Toast.makeText(MainActivity.this, "Position" + position, Toast.LENGTH_SHORT).show();
//                if (position == 0) {
//                    LinearLayout containerLeft = (LinearLayout) MainActivity.this.findViewById(R.id.left_view_container);
//                    containerLeft.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        outputRL.setOnClickListener(this);
        userLL.setOnClickListener(this);
        tableNameLL.setOnClickListener(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
            if (externalFileRule.getVisibility() == View.VISIBLE)
                externalFileRule.setVisibility(View.GONE);
            else this.finish();
        return true;
    }


    public List<Note> getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativelayout_1://返回第一道题
                //底部
                if (data.size() != 0)
                    viewPager.setCurrentItem(0);
                break;
            case R.id.manage_relativeLayout://更改试题
                // Toast.makeText(this, "teacher_layout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, QuestionManage.class);
                //intent.putExtra("data", (Serializable) data);
                intent.putExtra("table_name",this.tableName);
                startActivity(intent);
                break;
            case R.id.externalFile_LinearLayout:
                externalFileRule.setVisibility(View.VISIBLE);
                break;
            //Toast.makeText(this, "externalFile_LinearLayout", Toast.LENGTH_SHORT).show();
            case R.id.externalFileRule:
                externalFileRule.setVisibility(View.GONE);
                break;
            case R.id.que_style_linearLayout://编辑试卷模板
                styleManager.showDialog();
                break;
            case R.id.model_relativeLayout:
                Intent i = new Intent(MainActivity.this, ModelActivity.class);
                //i.putExtra();
                i.putExtra("table_name",this.tableName);
                startActivity(i);
                break;
            case R.id.relativelayout_2://导出文件
                outputDialog();
                // Toast.makeText(MainActivity.this,"导出完毕",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_left_user:
                alterUserDialog();
                break;
            case R.id.ll_left_table_name:

                break;
            default:
                break;
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        // Toast.makeText(this,"onPanelSlide",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPanelOpened(View panel) {
        // Toast.makeText(this,"onPanelOpened",Toast.LENGTH_SHORT).show();
        setLeftClickable(true);
    }

    @Override
    public void onPanelClosed(View panel) {
        // Toast.makeText(this,"onPanelClosed",Toast.LENGTH_SHORT).show();
        externalFileRule.setVisibility(View.GONE);

        setLeftClickable(false);
    }

    private void setLeftClickable(boolean flag) {
        externalFileRule.setClickable(flag);
        externalLayout.setClickable(flag);
        queStyleLayout.setClickable(flag);
        modelRelativeLayout.setClickable(flag);
        manage_relativeLayout.setClickable(flag);
    }

    //导入外部文件
    private boolean alterDialog(final String path) {
        boolean flag = false;
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(MainActivity.this, "即将导入" + path, Toast.LENGTH_SHORT).show();
                        new QuestionLoaderThread(MainActivity.this,tableName).execute(path);
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认导入文件"); //设置内容
        builder.setIcon(R.drawable.new_note_icon);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
        return flag;
    }

    //导出文件
    private boolean outputDialog() {
        boolean flag = false;
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        String filename = "myQue.txt";
                        boolean flag = new FilesWriter(MainActivity.this).writeFile(filename, Environment.getExternalStorageDirectory().getAbsolutePath(), Typer.typeOutputQue(data));
                        if (flag)
                            Toast.makeText(MainActivity.this, "导出完毕", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(MainActivity.this, "导出错误，myQue.txt已经存在了", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认导出文件(文件为myQue.txt,放置在根目录下)"); //设置内容
        builder.setIcon(R.drawable.output);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
        return flag;
    }

    //登陆用户属性变更
    private boolean alterUserDialog() {
        boolean flag = false;
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        attr = UserAttr.converse(attr);
                        initUI(1,0);
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认将用户变更为： " + UserAttr.getOtherByAttr(this.attr)); //设置内容
        builder.setIcon(R.drawable.user);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
        return flag;
    }


    private void resultDialog(boolean flag){
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认将用户变更为： " + UserAttr.getOtherByAttr(this.attr)); //设置内容
        builder.setIcon(R.drawable.user);//设置图标，图片id即可
        builder.setPositiveButton("知道了", null);
       // builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}

