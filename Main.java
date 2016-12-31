import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Heyong on 2016/12/30.
 */
public class Main {
    public static void main(String[] argv){
        Maze m = new Maze(100,100);
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
        System.out.println("求解用时：" + (endTime - startTime));
    }

}
