package maze;

import java.util.Collections;

/**
 * 基本并查集
 */
public class UnionFindSet{

    public static final int DEFAULT_SIZE = 100;

    private int[] father;

    public UnionFindSet() {
        father = new int[DEFAULT_SIZE];
        init();
    }

    public UnionFindSet(int size){
        father = new int[size];
        init();
    }

    public boolean isConnected(int x,int y){
        return find(x) == find(y);
    }

    public int find(int x){
        int r = x;
        //找到根节点
        while ( r != father[r])
            r = father[r];
        //路径压缩
        int i = x,j;
        while (i != r){
            j = father[i];
            father[i] = r;
            i = j;
        }
        return r;
    }

    public void union(int x ,int y){
        int fx = find(x);
        int fy = find(y);
        if(fx == fy) return;
        else if(fx > fy)
            father[fx] = fy;
        else
            father[fy] = fx;
    }

    private void init(){
        for (int i = 0;i<father.length;i++)
            father[i] = i;
    }

}
