package com.company;

import java.util.LinkedList;
import java.util.List;

public class TreeNode {

    /**
     */
    public static final int PROGRAM = 0;
    /**
     * if语句
     */
    public static final int IF_STMT = 1;
    /**
     */
    public static final int WHILE_STMT = 2;
    /**
     */
    public static final int READ_STMT = 3;
    /**
     */
    public static final int WRITE_STMT = 4;
    /**
     * 声明语句
     */
    public static final int DECLARE_STMT = 5;
    /**
     * 赋值语句
     */
    public static final int ASSIGN_STMT = 6;
    /**
     * 复合表达式
     */
    public static final int EXP = 7;
    /**
     * 变量
     */
    public static final int VAR = 8;
    /**
     * 运算符
     * 在datatype中存储操作符类型
     */
    public static final int LOG_OP = 9;
    /**
     * {}函数体
     *
     */
    public static final int STMT_BLOCK = 10;
    /**
     * 函数队列
     *
     */
    public static final int STMT_SEQ = 11;

    public static final int FACTOR = 12;

    public static final int LITREAL_INT = 13;
    /**
     *
     *identifier
     *
     */
    public static final int ID = 14;

    public static final int ADD_EXP = 15;

    public static final int TERM = 16;

    public static final int ADD_OP = 17;

    public static final int MUL_OP = 18;

    public static final int ELSE_STMT = 19;

    public static final int CONTINUE_STMT = 20;

    public static final int BREAK_STMT = 21;

    public static final int VALUE_STMT = 22;

    public static final int CHAR = 23;

    public static final int ARRAY_STMTR = 24;

    public static final int PRINT_FUNC = 25;

    public static final int SCAN_FUNC = 26;

    public static final int FOR_STMT = 27;

    public static final int OR_EXP = 28;

    public static final int AND_EXP = 29;

    public static final int NOT_EXP = 30;

    public static final int SEMI_STMT = 31;

    public static final int CHARS = 32;

    public static final int LITREAL_REAL = 33;

    //存储个数
    public static final int TYPE_NUM = 34;

    public static final String[] TYPES = { "program", "if_stmt", "while_stmt", "read_stmt","write_stmt", "declare_stmt", "assign_stmt",
            "exp","variable", "log_op", "stmt_block", "stmt_sequence", "factor","int","identifier","add_exp","term","add_op","mul_op",
            "else-stmt","continue-stmt","break-stmt","value-stmt","character","array-stmt","print_func","scan_func","for-stmt","or_exp",
            "and_exp","not_exp","semi-stmt","chars","real"};

    //返回实际字符串
    public static String getStr(int num) {
        if (num < 0 || num > TYPE_NUM)
            return "undefine";
        else
            return TYPES[num];
    }
    private int type;

    private List<TreeNode> list = new LinkedList<>();

    private String value;

    private int linenum;

    public TreeNode(){

    }


    public TreeNode(int type){
        this.type = type;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TreeNode> getList() {
        return list;
    }

    public void setList(List<TreeNode> list) {
        this.list = list;
    }

    public int getLinenum() {
        return linenum;
    }

    public void setLinenum(int linenum) {
        this.linenum = linenum;
    }
}
