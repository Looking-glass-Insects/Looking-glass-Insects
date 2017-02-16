package maze;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Created by Samon Yu on 2017/2/14
 */

public class DrawMaze extends JFrame {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private Maze m;
    private AStar astar;
    private int x, y;


    //格子边长
    private int CellLength = 20;

    public DrawMaze(Maze m_f, AStar astar_f, int x_f, int y_f) {

        //初始化数据
        m = m_f;
        astar = astar_f;
        x = x_f;
        y = y_f;

        //界面设置
        Container p = getContentPane();
        setSize(200 + x * CellLength, 200 + y * CellLength);
        setTitle("迷宫");
        setLayout(null);
        setResizable(false);

        Dimension us = this.getSize();
        Dimension them = Toolkit.getDefaultToolkit().getScreenSize();
        int newX = (them.width - us.width) / 2;
        int newY = (them.height - us.height) / 2;
        this.setLocation(newX, newY);


        MazePanel mazepanel = new MazePanel(m, astar, x, y);
        mazepanel.setBounds(0, 0, 200 + x * CellLength, 200 + y * CellLength);
        add(mazepanel);

    }


}
