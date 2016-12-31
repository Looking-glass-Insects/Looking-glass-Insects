import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Heyong on 2016/12/31.
 * <p>
 * <p>
 * A星搜索算法求解迷宫
 */
public class AStar {
    private Maze maze;

    /**
     * 算法维护的数据
     */
    private LinkedList<ThreeDPoint> openedList;
    private LinkedList<ThreeDPoint> closedList;

    /**
     * 存储答案
     */
    private LinkedList<Maze.Point> ansList = new LinkedList<Maze.Point>() {

        /**
         * 通过后域获取该Point
         * @param index 后域
         * @return
         */
        @Override
        public Maze.Point get(int index) {
            final Maze.Point[] p = new Maze.Point[1];
            this.stream().forEach(e -> {
                if (e.y == index) {
                    p[0] = e;
                    return;
                }
            });
            return p[0];
        }
    };

    public void print() {
        printParent(ansList.get(maze.getWidth() * maze.getHeight() - 1));
    }

    private void printParent(Maze.Point point) {
        if (point.x == 0) {
            System.out.println(point.x);
            System.out.println(point.y);
            return;
        } else {
            printParent(ansList.get(point.x));
            System.out.println(point.y);
        }
    }

    private AStar() {
        openedList = new LinkedList<ThreeDPoint>() {
            @Override
            public boolean contains(Object o) {
                if (o == null || Integer.class != o.getClass()) return false;
                final boolean[] flag = {false};
                this.forEach(e -> {
                    if ((Integer) o == e.x) {
                        flag[0] = true;
                        return;
                    }
                });
                return flag[0];
            }

            /**
             *  返回元素为index的ThreeDPoint
             *  更改了原来的意思
             * @param index
             * @return
             */
            @Override
            public ThreeDPoint get(int index) {
                final ThreeDPoint[] p = {new ThreeDPoint(-1, -1)};
                this.forEach(e -> {
                    if (e.x == index) {
                        p[0] = e;
                        return;
                    }
                });
                return p[0];
            }
        };
        closedList = new LinkedList<ThreeDPoint>() {
            @Override
            public boolean contains(Object o) {
                if (o == null || Integer.class != o.getClass()) return false;
                final boolean[] flag = {false};
                this.forEach(e -> {
                    if ((Integer) o == e.x) {
                        flag[0] = true;
                        return;
                    }
                });
                return flag[0];
            }
        };
    }

    public AStar(Maze maze) {
        this();
        this.maze = maze;
    }

    /**
     * A星算法
     */
    public void cal(){
        ThreeDPoint start = new ThreeDPoint(0, 0);//起点入队
        ThreeDPoint end = new ThreeDPoint(maze.getHeight() * maze.getWidth() - 1, -1);
        openedList.add(start);
        for (ThreeDPoint p = openedList.remove(); !p.equals(end); ) {
            List<Integer> list = maze.getNextChoicesByEle(p.x);
            list.stream()
                    .filter(x -> {
                        if (closedList.contains(x))
                            return false;
                        else if (openedList.contains(x)) {
                            ThreeDPoint temp = openedList.get(x);
                            if (p.g + 1 < temp.g) {
                                temp.g = p.g + 1;
                                Maze.Point ansP = ansList.get(temp.x);
                                ansP.x = p.x;
                            }
                            return false;
                        }
                        else return true;
                    })
                    .map(x -> new ThreeDPoint(x, p.g + 1))
                    .forEach(e -> {
                        openedList.add(e);
                        ansList.add(new Maze.Point(p.x, e.x));
                    });
            Collections.sort(openedList);
            try {
                closedList.add((ThreeDPoint) p.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            ThreeDPoint temp = openedList.remove();
            p.copy(temp);
        }
    }


    private class ThreeDPoint implements Comparable<ThreeDPoint>,Cloneable {
        int x;//元素本身
        int h;//曼哈顿距离
        int g;//耗散值

        @Override
        public int compareTo(ThreeDPoint o) {
            return (h + g) - (o.h + o.g);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ThreeDPoint that = (ThreeDPoint) o;

            return x == that.x;

        }

        public void copy(ThreeDPoint p) {
            this.x = p.x;
            this.h = p.h;
            this.g = p.g;
        }

        @Override
        public int hashCode() {
            return x;
        }

        public ThreeDPoint(int x, int g) {
            this.x = x;
            this.g = g;
            this.h = getH();
        }

        private int getH() {
            int row = x / maze.getWidth();
            int column = x % maze.getWidth();
            return (maze.getHeight() - 1 - row) + (maze.getWidth() - 1 - column);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return  new ThreeDPoint(this.x,this.g);
        }
    }
}
