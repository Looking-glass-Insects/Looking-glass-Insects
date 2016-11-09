package com.example.heyong.exercisesbase.Bean;

/**
 * Created by Heyong on 2016/11/9.
 */
public class TableName {
    public static final String TAVLE_1 = "科目1";
    public static final String TAVLE_2 = "科目2";
    public static final String TAVLE_3 = "科目3";
    public static int getPositionByTable(String table){
        if(table.equals(TAVLE_1)){
            return 0;
        }else if(table.equals(TAVLE_2)){
            return 1;
        }else {
            return 2;
        }
    }
}
