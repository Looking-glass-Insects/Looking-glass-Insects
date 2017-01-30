package com.example.heyong.game2048;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    int[][] dataMetrix;

    {
        dataMetrix = new int[4][4];
        for (int i = 0; i < dataMetrix.length; i++)
            for (int j = 0; j < dataMetrix[i].length; j++)
                dataMetrix[i][j] = 2;


    }


    @Test
    public void addition_isCorrect() throws Exception {
        goToTop();

    }



    private void goToTop(){
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

}