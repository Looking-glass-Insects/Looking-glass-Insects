package com.example.heyong.game2048;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * 简易2048游戏，学习自定义view
 */
public class MainActivity extends AppCompatActivity {
    GridLayout container;
    int[][] dataMetrix;


    static class Point {
        public int i;
        public int j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }


    static Map<Integer,Integer> intToColor = new HashMap<>();

    static {
        intToColor.put(0,Color.parseColor("#CCC0B3"));
        intToColor.put(2, Color.parseColor("#EEE4DA"));
        intToColor.put(4, Color.parseColor("#EDE0C8"));
        intToColor.put(8, Color.parseColor("#F2B179"));
        intToColor.put(16, Color.parseColor("#F49563"));
        intToColor.put(32, Color.parseColor("#F5794D"));
        intToColor.put(64, Color.parseColor("#F55D37"));
        intToColor.put(128, Color.parseColor("#EEE863"));
        intToColor.put(256, Color.parseColor("#EDB04D"));
        intToColor.put(512, Color.parseColor("#ECB04D"));
        intToColor.put(1024, Color.parseColor("#EB9437"));
        intToColor.put(2048, Color.parseColor("#EA7821"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (GridLayout) findViewById(R.id.grid_container);
        addBlankItem();
        container.setOnTouchListener(new MyOnTouchListener());
        initItem();
    }

    private void initItem() {
        dataMetrix = new int[4][4];
        for (int i = 0; i < dataMetrix.length; i++)
            for (int j = 0; j < dataMetrix[i].length; j++)
                dataMetrix[i][j] = 0;
        for (int k = 0; k < 2; k++) {
            int i = new Random().nextInt(4);
            int j = new Random().nextInt(4);
            dataMetrix[i][j] = new Random().nextInt() > 0.5 ? 4 : 2;
            drawItem();
        }
    }

    private void drawItem() {
        for (int i = 0; i < dataMetrix.length; i++)
            for (int j = 0; j < dataMetrix[i].length; j++) {
                ItemView item = (ItemView) container.getChildAt(4 * i + j);
                if (dataMetrix[i][j] == 0) {
                    item.setText(" ");
                    item.setBgColor(intToColor.get(0));
                } else if(dataMetrix[i][j] == 2048){
                    SimpleDialogFactory.alertDialog(this, "提示", "YOU WIN,感谢使用,祝你好运", R.mipmap.ic_launcher, new SimpleDialogFactory.IAlertDialogCallBack() {
                        @Override
                        public void doSomething(boolean isOK) {
                            initItem();
                        }
                    });
                } else{
                    item.setText(dataMetrix[i][j] + "");
                    item.setBgColor(intToColor.get(dataMetrix[i][j]));
                }
                item.invalidate();
            }
    }

    private void addItem() {
        List<Point> l = new LinkedList<>();
        for (int i = 0; i < dataMetrix.length; i++)
            for (int j = 0; j < dataMetrix[i].length; j++)
                if (dataMetrix[i][j] == 0)
                    l.add(new Point(i, j));
        if (l.size() == 0) {
            SimpleDialogFactory.alertDialog(this, "提示", "Game Over:你本次移动没有造成空位", R.mipmap.ic_launcher, null);
            initItem();
            drawItem();
        } else {
            Point p = l.get(new Random().nextInt(l.size()));
            dataMetrix[p.i][p.j] = new Random().nextInt() > 0.5 ? 4 : 2;
            ItemView v = (ItemView) container.getChildAt(4 * p.i + p.j);
            v.setText(dataMetrix[p.i][p.j]+"");
            v.setBgColor(intToColor.get(dataMetrix[p.i][p.j]));
            v.invalidate();
            ScaleAnimation sa = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0.5F,Animation.RELATIVE_TO_SELF,0.5F);
            sa.setDuration(300);
            v.startAnimation(sa);
        }
    }


    private void addBlankItem() {
        for (int i = 0; i < 16; i++) {
            ItemView itemView = new ItemView(this);
            ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(Util.dip2px(this, 80), Util.dip2px(this, 80));
            int m = Util.dip2px(this, 5);
            itemView.setPadding(m, m, m, m);
            itemView.setLayoutParams(margin);
            itemView.setTextSize(Util.dip2px(this, 20));
            container.addView(itemView);
        }
    }

    class MyOnTouchListener implements View.OnTouchListener {
        private float downX;    //按下时 的X坐标
        private float downY;    //按下时 的Y坐标

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //将按下时的坐标存储
                    downX = x;
                    downY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    //获取到距离差
                    float dx = x - downX;
                    float dy = y - downY;
                    //防止是按下也判断
                    if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                        //通过距离差判断方向
                        int orientation = getOrientation(dx, dy);
                        switch (orientation) {
                            case 'r':

                                goToRight();
                                break;
                            case 'l':

                                goToLeft();
                                break;
                            case 't':

                                goToTop();
                                break;
                            case 'b':

                                goToBottom();
                                break;
                        }
                        drawItem();
                        addItem();
                    }
                    break;
            }
            return true;
        }

        private void goToRight() {
            for (int i = 0; i < 4; i++) {
                for (int j = 3; j > 0; j--) {
                    if (dataMetrix[i][j] == dataMetrix[i][j - 1]) {
                        //queue.add(dataMetrix[i][j] *2);
                        dataMetrix[i][j - 1] = dataMetrix[i][j] * 2;
                        dataMetrix[i][j] = 0;
                    }
                }
                int count = 3;
                for (int j = 3; j >= 0; j--) {
                    if (dataMetrix[i][j] != 0) {
                        dataMetrix[i][count] = dataMetrix[i][j];
                        if (count != j)
                            dataMetrix[i][j] = 0;
                        count--;
                    }
                }
            }
        }

        private void goToLeft() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (dataMetrix[i][j] == dataMetrix[i][j + 1]) {
                        dataMetrix[i][j + 1] = dataMetrix[i][j] * 2;
                        dataMetrix[i][j] = 0;
                    }
                }
                int count = 0;
                for (int j = 0; j < 4; j++) {
                    if (dataMetrix[i][j] != 0) {
                        dataMetrix[i][count] = dataMetrix[i][j];
                        if (count != j)
                            dataMetrix[i][j] = 0;
                        count++;
                    }
                }
            }
        }

        private void goToTop() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (dataMetrix[j][i] == dataMetrix[j + 1][i]) {
                        dataMetrix[j + 1][i] = dataMetrix[j][i] * 2;
                        dataMetrix[j][i] = 0;
                    }
                }
                int count = 0;
                for (int j = 0; j < 4; j++) {
                    if (dataMetrix[j][i] != 0) {
                        dataMetrix[count][i] = dataMetrix[j][i];
                        if (count != j)
                            dataMetrix[j][i] = 0;
                        count++;
                    }
                }
            }
        }

        private void goToBottom() {
            for (int i = 0; i < 4; i++) {
                for (int j = 3; j > 0; j--) {
                    if (dataMetrix[j][i] == dataMetrix[j - 1][i]) {
                        //queue.add(dataMetrix[i][j] *2);
                        dataMetrix[j - 1][i] = dataMetrix[j][i] * 2;
                        dataMetrix[j][i] = 0;
                    }
                }
                int count = 3;
                for (int j = 3; j >= 0; j--) {
                    if (dataMetrix[j][i] != 0) {
                        dataMetrix[count][i] = dataMetrix[j][i];
                        if (count != j)
                            dataMetrix[j][i] = 0;
                        count--;
                    }
                }
            }
        }


        private int getOrientation(float dx, float dy) {
            if (Math.abs(dx) > Math.abs(dy)) {
                //X轴移动
                return dx > 0 ? 'r' : 'l';
            } else {
                //Y轴移动
                return dy > 0 ? 'b' : 't';
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_header_menu,menu);
        return true;
    }
    private static String MyURL = "https://github.com/Looking-glass-Insects/Looking-glass-Insects/tree/Game2048";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_about){
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(MyURL);
            intent.setData(content_url);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
