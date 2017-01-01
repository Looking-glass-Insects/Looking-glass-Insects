package interpreter;


/**
 * Created by Heyong on 2016/12/31.
 */
public class Main {

    public static void main(String[] argv) {
        Interpreter i = new Interpreter();
//        i.readLine("ASSIGN x 1+1*2");
//        i.readLine("ASSIGN x x+1");
//        i.readLine("ASSIGN   y  x*2");
//        i.readLine(" ASSIGN z y * x - 3 * ( x - y ) ");
//        i.readLine("ASSIGN xx x+x-y*z");
//        i.printVars();
        i.readLine("DEFINE f ");
        i.readLine("ASSIGN x 1+2");
        i.readLine("ASSIGN x x*x");
        //i.readLine("CALL g");// generate StackOverFlow Error
        i.readLine("END");

        i.readLine("DEFINE g ");
        i.readLine("ASSIGN x 1+2");
        i.readLine("ASSIGN x x*x*x");
        i.readLine("CALL f");
        i.readLine("END");
        //i.printFuncs();
        // i.readLine("CALL g");

        //i.readLine(" FOR 1 CALL g");
        i.readLine("DEFINE h");
        i.readLine("FOR x ASSIGN x x+1");
        i.readLine("END");

        i.readLine("ASSIGN x 3");
        i.readLine("CALL h");

    }
}
