package interpreter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Heyong on 2016/12/31.
 * <p>
 * 完成 ASSIGN,DEFINE,CALL,FOR
 */
public class Interpreter {

    public static final String RE_ASSIGN = "^\\s*ASSIGN\\s+(\\S+)\\s+(.+)$";
    public static final String RE_DEFINE = "^\\s*DEFINE\\s+(\\S+)\\s*$";
    public static final String RE_CALL = "^\\s*CALL\\s+(\\S+)\\s*$";
    public static final String RE_OPRETOR = "[()+\\-*/]";
    public static final String RE_IS_VAR_NAME = "[a-zA-Z_][a-zA-Z0-9_]*";
    public static final String RE_DEFINE_FUNC_FINISH = "^\\s*END\\s*$";
    public static final String RE_LOOP_STATEMENT = "^\\s*FOR\\s+(\\S+)\\s+(\\S.*)$";


    public static final String ERR_GRAMMAR = "语法错误：";
    public static final String ERR_NO_SUCH_VAR = "未定义变量：";
    public static final String ERR_NO_SUCH_FUNC = "未定义函数：";
    public static final String ERR_EXPRESSION = "表达式有误";
    public static final String ERR_NAME_ILLEAGLE = "变量或函数名不合法：";
    public static final String ERR_FUNC_NEST = "函数嵌套定义";
    public static final String ERR_LOOP_ITERATOR = "循环迭代变量有误：";

    /**
     * 脚本引擎动态执行算数表达式中的内容
     */
    private static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * 变量表
     */
    private Map<String, Integer> vars;

    /**
     * 函数表
     */
    private Map<String, List<String>> functions;

    private Matcher matcher;//匹配readLine()所读入的字符串

    private Pattern[] patterns = {

            Pattern.compile(RE_ASSIGN),
            Pattern.compile(RE_DEFINE),
            Pattern.compile(RE_CALL),
            Pattern.compile(RE_DEFINE_FUNC_FINISH),
            Pattern.compile(RE_LOOP_STATEMENT)

    };
    /**
     * 定义函数时用到的指示变量
     */
    private boolean isDefining = false;//是否在定义函数
    private String currFuncName;//现在正在定义的函数名

    public Interpreter() {
        vars = new HashMap<>();
        functions = new HashMap<>();
    }

    /**
     * 读取一行，
     *
     * @param line
     */
    public void readLine(String line) {
        for (int i = 0; i < patterns.length; i++) {
            matcher = patterns[i].matcher(line);
            if (matcher.matches()) {
                exec(i, line);
                return;
            }
        }
        throw new IllegalArgumentException(ERR_GRAMMAR + line);
    }

    /**
     * 分发事件
     *
     * @param which
     * @param line
     */
    private void exec(int which, String line) {
        switch (which) {
            case 0:
                if (isDefining) {
                    List<String> states = functions.get(currFuncName);
                    states.add(line);
                    return;
                }
                //System.out.println("赋值");
                execAssign();
                break;
            case 1:
                if (isDefining)
                    throw new IllegalArgumentException(ERR_FUNC_NEST);
                //System.out.println("函数定义");
                execDefineFunc();
                break;
            case 2:
                if (isDefining) {
                    List<String> states = functions.get(currFuncName);
                    states.add(line);
                    return;
                }
                //System.out.println("函数调用");
                execCall();
                break;
            case 3:
                if (!isDefining)
                    throw new IllegalArgumentException(ERR_GRAMMAR+line);
                isDefining = false;
                System.out.println("Defining " + currFuncName + " End");
                break;
            case 4:
                if (isDefining) {
                    List<String> states = functions.get(currFuncName);
                    states.add(line);
                    return;
                }
                //System.out.println("循环语句");
                execLoop();
                break;
        }
    }

    /**
     * 循环语句
     */
    private void execLoop() {
        final String[] expression = {matcher.group(1).replaceAll(" ", "")};
        String statement = matcher.group(2);
        filter(expression);
        try {
            int val = execEquation(expression);
            if (val <= 0)
                throw new IllegalArgumentException(ERR_LOOP_ITERATOR + val);
            System.out.println("FOR Executing: times = " + val);
            for (int i = 0; i < val; i++)
                readLine(statement);
            System.out.println("FOR Fin.");
        } catch (ScriptException e) {
            throw new IllegalArgumentException(ERR_EXPRESSION + expression[0]);
        }
    }

    /**
     * 函数调用
     */
    private void execCall() {
        String var = matcher.group(1);//找到函数名
        List<String> states = functions.get(var);
        if (states == null)
            throw new IllegalArgumentException(ERR_NO_SUCH_FUNC + var);
        System.out.println("Calling " + var);
        states.forEach(s -> {
            readLine(s);
        });
        System.out.println("Calling " + var + " End");
    }

    /**
     * 执行函数定义,定义时不对其语句进行语法检查
     */
    private void execDefineFunc() {
        isDefining = true;
        currFuncName = matcher.group(1);//函数变量名
        if (!isLegal(currFuncName))
            throw new IllegalArgumentException(ERR_NAME_ILLEAGLE + currFuncName);
        System.out.println("Defining " + currFuncName);
        functions.put(currFuncName, new LinkedList<>());
    }

    /**
     * 执行赋值语句
     */
    private void execAssign() {
        String var = matcher.group(1);//找到变量名
        if (!isLegal(var))
            throw new IllegalArgumentException(ERR_NAME_ILLEAGLE + var);
        final String[] expression = {matcher.group(2).replaceAll(" ", "")};//表达式去空格
        filter(expression);//替换
        try {
            int val = execEquation(expression);
            vars.put(var, val);
            System.out.println("Assign " + val + " to " + var);
        } catch (ScriptException e) {
            throw new IllegalArgumentException(ERR_EXPRESSION + expression[0]);
        }
    }

    /**
     * 将表达式中的变量过滤为值
     *
     * @param expression
     */
    private void filter(final String[] expression) {
        Stream.of(expression[0].split(RE_OPRETOR))
                .filter(e -> {
                    try {
                        if (e.equals(""))
                            return false;
                        Double.parseDouble(e);
                    } catch (NumberFormatException e1) {
                        //格式化失败表示其中有字母之类的东西，当作变量名
                        Integer i = vars.get(e);
                        if (i == null)//未发现该变量
                            throw new IllegalArgumentException(ERR_NO_SUCH_VAR + e);
                        expression[0] = expression[0].replaceAll(e, i + "");
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());//过滤掉算数表达式中的变量名，以其值替换
    }

    /**
     * 打印当前所有变量
     */
    public void printVars() {
        System.out.println("-------------------------");
        System.out.println("ALL VARIABLES:");
        vars.keySet().forEach(key -> {
            int val = vars.get(key);
            System.out.println(key + " -> " + val);
        });
    }

    /**
     * 打印所有函数
     */
    public void printFuncs() {
        System.out.println("-------------------------");
        System.out.println("ALL FUNCTIONS:");
        functions.keySet().forEach(key -> {
            List<String> l = functions.get(key);
            System.out.println(key);
            l.forEach(f -> {
                System.out.println("    " + f);
            });
        });
    }

    /**
     * 判断该变量名是否合法
     *
     * @return if is legal
     */
    private boolean isLegal(String varName) {
        Pattern pattern = Pattern.compile(RE_IS_VAR_NAME);
        Matcher matcher = pattern.matcher(varName);
        if (matcher.matches())
            return true;
        else return false;
    }


    private int execEquation(final String[] expression) throws ScriptException {
        return ((Double)(Double.parseDouble(jse.eval(expression[0]).toString()))).intValue();
    }
}
