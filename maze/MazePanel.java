package maze;


import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import maze.Maze.Point;

/**
 * Created by Samon Yu on 2017/2/14
 */

public class MazePanel extends JPanel {

    private Maze m;
    private AStar astar;
    private int x, y;
    private LinkedList<Point> ans;
    private Graphics g;
    //格子边长
    private int CellLength = 20;
    private int answay[];
    int j = 2;

    public MazePanel(Maze m_f, AStar astar_f, int x_f, int y_f) {

        //初始化数据
        m = m_f;
        astar = astar_f;
        x = x_f;
        y = y_f;

        answay = new int[x * y];
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 200 + x * CellLength, 200 + y * CellLength);
        g.setColor(Color.black);
        for (int i = 0; i <= y; i++) {
            g.drawLine(100, 50 + i * CellLength, 100 + x * CellLength, 50 + i * CellLength);
        }
        for (int i = 0; i <= x; i++) {
            g.drawLine(100 + i * CellLength, 50, 100 + i * CellLength, 50 + y * CellLength);
        }
        g.setColor(Color.white);
        g.drawLine(100, 50, 100 + CellLength, 50);
        g.drawLine(100, 50, 100, 50 + CellLength);
        g.drawLine(100 + x * CellLength, 50 + y * CellLength, 100 + (x - 1) * CellLength, 50 + y * CellLength);
        g.drawLine(100 + x * CellLength, 50 + y * CellLength, 100 + x * CellLength, 50 + (y - 1) * CellLength);

        Set<Point> nowall = m.getNotWalls();

        for (Point p : nowall) {

            //初始化（使x<y）
            int real_x;
            int real_y;
            if (p.x > p.y) {
                real_x = p.y;
                real_y = p.x;
            } else {
                real_x = p.x;
                real_y = p.y;
            }

            if (real_x + 1 == real_y) {
                g.drawLine((real_x % x + 1) * CellLength + 100, (real_x / x) * CellLength + 50 + 1, (real_x % x + 1) * CellLength + 100, (real_x / x + 1) * CellLength + 50 - 1);
            } else {
                g.drawLine((real_x % x) * CellLength + 100 + 1, (real_x / x + 1) * CellLength + 50, (real_x % x + 1) * CellLength + 100 - 1, (real_x / x + 1) * CellLength + 50);
            }
        }

        ans = astar.getAnsList();


        getParent(ans.get(m.getWidth() * m.getHeight() - 1), answay);

        g.setColor(Color.green);
        System.out.print("路线：");
        for (int i = 0; i < j; i++) {
            g.fillRect(105 + (answay[i] % x) * CellLength, 55 + (answay[i] / x) * CellLength, 10, 10);
            if (i != j - 1) {
                if (answay[i] + 1 == answay[i + 1]) {
                    System.out.print("E");
                } else if (answay[i] - 1 == answay[i + 1]) {
                    System.out.print("W");
                } else if (answay[i] < answay[i + 1]) {
                    System.out.print("S");
                } else {
                    System.out.print("N");
                }
            }

        }
    }

    private void getParent(Point point, int answay[]) {
        if (point.x == 0) {
//	            System.out.println(point.x);
//	            System.out.println(point.y);

            answay[0] = point.x;
            answay[1] = point.y;
            return;
        } else {
            getParent(ans.get(point.x), answay);
//	            System.out.println(point.y);

            answay[j] = point.y;
            j++;
        }
    }

}
