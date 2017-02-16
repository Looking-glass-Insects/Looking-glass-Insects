package maze;

import java.util.*;

import javax.swing.JFrame;

/**
 * Created by Heyong on 2016/12/30.
 */

/**
 * Updated by Samon Yu on 2017/2/14
 */

public class Main {
    public static void main(String[] argv) {

        int x = 40;
        int y = 30;

        Maze m = new Maze(x, y);
        long startTime = Calendar.getInstance().getTimeInMillis();
        m.init();
        //m.print();
        long endTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("生成迷宫用时：" + (endTime - startTime));

        startTime = Calendar.getInstance().getTimeInMillis();
        AStar aStar = new AStar(m);
        aStar.cal();
        endTime = Calendar.getInstance().getTimeInMillis();
        //aStar.print();
        System.out.println("路线求解用时：" + (endTime - startTime));

        startTime = Calendar.getInstance().getTimeInMillis();
        JFrame drawing = new DrawMaze(m, aStar, x, y);
        drawing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawing.setVisible(true);
        endTime = Calendar.getInstance().getTimeInMillis();
        System.out.println("绘制迷宫用时：" + (endTime - startTime));
    }

}
