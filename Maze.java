import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Heyong on 2016/12/30.
 * <p>
 * 迷宫类
 * 起点为0，终点为 width*height-1
 */
public class Maze {
    private int width;
    private int height;
    /**
     * 墙的数据，(1,2)表示1与2之间无墙
     */
    private Set<Point> notWalls;

    UnionFindSet set;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        set = new UnionFindSet(width * height);
        notWalls = new HashSet<Point>() {
            @Override
            public boolean contains(Object o) {
                if (o == null || Point.class != o.getClass()) return false;
                final boolean[] flag = {false};
                this.forEach(e -> {
                    if (((Point) o).x == e.x && ((Point) o).y == e.y) {
                        flag[0] = true;
                        return;
                    }
                });
                return flag[0];
            }
        };
    }

    /**
     * 初始化迷宫
     */
    public void init() {
        while (!set.isConnected(0, width * height - 1)) {
            int rand = new Random().nextInt(width * height);
            int next = getNearEle(rand);
            notWalls.add(new Point(rand, next));
            set.union(rand, next);
        }
    }


    /**
     * 打印数据
     */
    public void print() {
        //Collections.sort(notWalls);
        for (Point p :
                notWalls) {
            p.print();
        }
    }

    /**
     * 获取x旁边的可达元素，4邻近
     *
     * @param x
     * @return
     */
    public List<Integer> getNextChoicesByEle(int x) {
        List<Integer> list = new LinkedList<>();
        assert x >= 0 && x < width * height : "参数范围错误";
        int row = x / width;
        int column = x % width;
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};
        for (int i = 0; i < dx.length; i++) {
            int _row = row + dx[i];
            int _column = column + dy[i];
            if (_row >= 0 && _row < height && _column >= 0 && _column < width)
                list.add(_row * width + _column);
        }
        list = list.stream().filter(i -> {
                            boolean flag = notWalls.contains(new Point(x, i)) || notWalls.contains(new Point(i, x));
                            //System.out.println(flag);
                            return flag;
                        }
                ).collect(Collectors.toList());
        return list;
    }


    /**
     * 随机获取x的旁边元素
     *
     * @param x
     * @return
     */
    public int getNearEle(int x) {
        assert x >= 0 && x < width * height : "参数范围错误";
        int row = x / width;
        int column = x % width;
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};
        int rand = new Random().nextInt(4);
        int _row = row + dx[rand];
        int _column = column + dy[rand];

        while (!(_row >= 0 && _row < height && _column >= 0 && _column < width)) {
            rand = new Random().nextInt(4);
            _row = row + dx[rand];
            _column = column + dy[rand];
        }

        return _row * width + _column;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * 二元组
     */
    static class Point/* implements Comparable<Point>*/ {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void print() {
            System.out.println("(" + x + "," + y + ")");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

//        @Override
//        public int compareTo(Point o) {
//            return x - o.x;
//        }
    }
}
