package com.company;

public class Token {
    /** if */
    public static final int IF = 0;
    /** else */
    public static final int ELSE = 1;
    /** while */
    public static final int WHILE = 2;
    /** read */
    public static final int READ = 3;
    /** write */
    public static final int WRITE = 4;
    /** int */
    public static final int INT = 5;
    /** real */
    public static final int REAL = 6;
    /** + */
    public static final int PLUS = 7;
    /** - */
    public static final int MINUS = 8;
    /** * */
    public static final int MUL = 9;
    /** / */
    public static final int DIV = 10;
    /** = */
    public static final int ASSIGN = 11;
    /** < */
    public static final int LT = 12;
    /** == */
    public static final int EQ = 13;
    /** <> */
    public static final int NEQ = 14;
    /** ( */
    public static final int LPARENT = 15;
    /** ) */
    public static final int RPARENT = 16;
    /** ; */
    public static final int SEMI = 17;
    /** { */
    public static final int LBRACE = 18;
    /** } */
    public static final int RBRACE = 19;
    /** /* */
    public static final int LCOM = 20;
    /** *\/ */
    public static final int RCOM = 21;
    /** // */
    public static final int SCOM = 22;
    /** [ */
    public static final int LBRACKET = 23;
    /** ] */
    public static final int RBRACKET = 24;
    /** <= */
    public static final int LET = 25;
    /** > */
    public static final int GT = 26;
    /** >= */
    public static final int GET = 27;
    /** 标识符,由数字,字母或下划线组成,第一个字符不能是数字 */
    public static final int ID = 28;
    /** && */
    public static final int AND = 29;
    /** \\ */
    public static final int OR = 30;
    /** char */
    public static final int CHAR = 31;
    /** break */
    public static final int BREAK = 32;
    /** continue */
    public static final int CONTINUE = 33;
    /** 文件结尾0*/
    public static final Token EOF = new Token(34);
    /** \'*/
    public static final int SQM = 35;
    /** "*/
    public static final int DQM = 36;
    /**  \n*/
    public static final int NL = 37;
    /**  \r*/
    public static final int CR = 38;
    /**  \t*/
    public static final int HT = 39;
    /** ,*/
    public static final int CO = 40;
    /** int型字面值 */
    public static final int LITERAL_INT = 41;
    /** real型字面值 */
    public static final int LITERAL_REAL = 42;
    /** char型字面值 */
    public static final int LITERAL_CHAR = 43;
    /** print */
    public static final int PRINT = 44;
    /** scan */
    public static final int SCAN = 45;
    /** for */
    public static final int FOR = 46;
    /** ！ */
    public static final int EM = 47;
    /** char数组 */
    public static final int CHARS = 48;
    /** else if */
    public static final int ELSE_IF = 49;


    //存储个数
    public static final int TOK_NUM = 50;
    //每个type对应的字符串类型
    public static final String[] STRS = { "if", "else", "while", "read","write", "int", "real",
            "+","-", "*", "/", "=", "<", "==", "<>", "(", ")", ";", "{", "}", "/*", "*/", "//", "[", "]",
            "<=", ">", ">=", "ID", "&&", "||","char","break","continue","end_of_file","'","\"","\\n","\\r","\\t",",",
            "LITERAL_INT","LITERAL_REAL","LITERAL_CHAR","print","scan","for","!","CHARS","else if"};

    //返回Tok类型实际字符串
    public static String getStr(int num) {
        if (num < 0 || num > TOK_NUM)
            return "undefine";
        else
            return STRS[num];
    }


    //序号
    private int order;
    //类型
    private String type;
    // 值
    private String value;
    //行号
    private int lineNum;
    //开始列号
    private int beginCol;
    //结束列号
    private int endCol;

    //构造函数
    public Token(){}
    public Token(int order) {
        this.order = order;
    }
    public Token(String type) {
        this.type = type;
        this.value = null;
    }
    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public int getLineNum(){
        return lineNum;
    }
    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
    public int getBeginCol() {
        return beginCol;
    }
    public int getEndCol() {
        return endCol;
    }
    public void setEndCol(int endCol){
        this.endCol=endCol;
    }
    public void setBeginCol(int beginCol) {
        this.beginCol = beginCol;
    }

    //输出每个Token实际对应的值
    public String output(){
        return "<"+this.getType()+":"+this.getValue()+" "+"lineNum:"+this.getLineNum()+" "+"("+this.getBeginCol()+ ","+this.getEndCol()+")"+">";
    }
}
