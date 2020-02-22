package com.company;


public class Lexer {
    //当前行号
    private int lineNum = 0;
    //每个Token开始的位置
    private  int startCol = 0;
    //当前字符位置
    private int cur = -1;
    //当前要分析的字符串
    private String str = null;
    //当前要分析的字符串长度
    private int len = 0;

    public Lexer(String str) {
        if (str == null)
            throw new NullPointerException("构造函数不能传递null");
        this.str = str;
        this.len = str.length();
        this.startCol = 0;
        this.cur = -1;
        this.lineNum = 1;
    }


    private boolean isLetter(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }
    private boolean isUnderLine(char c) {
        return (c == '_');
    }
    private boolean isLetterUnderline(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')||c == '_');
    }
    private boolean isNumLetterUnderline(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_');
    }
    private boolean isNum(char c) {
        return (c >= '0' && c <= '9');
    }
    private boolean isDecNum(char c){
        return ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'E'));
    }
    private boolean isNotDecNum(char c){
        return ((c >= 'a' && c <= 'z') || (c >= 'E' && c <= 'Z')||c=='_');
    }





    //获取下一个字符串
    private char nextChar()
    {
        if(cur >= len-1)
        {
            return 0;
        }
        cur++;
        startCol++;
        char c = str.charAt(cur);
        if (c=='\n') {
            lineNum++;
            startCol = 0;
        }
        return c;
    }
    //回退一个字符
    private int revertChar()
    {
        if(cur <= 0)
        {
            return 0;
        }
        int rcur = cur--;
        startCol--;
        char c = str.charAt(rcur);
        if (c == '\n') {
            lineNum--;
        }
        return rcur;
    }

    //分析identifier与keyword的Token
    private Token parseIdentifierKeyword()
    {
        int start = cur;
        char c;
        Token token = new Token();
        while ((c=nextChar())!=0)
        {
            if(!isNumLetterUnderline(c))
            {
                int t = revertChar();
                String s = str.substring(start,t);
                if("if".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.IF);
                    return token;
                }else if("else".equals(s))
                {
                    if(nextChar()==' ')
                    {
                        char p = nextChar();
                        if(p=='i')
                        {
                            char l = nextChar();
                            if(l=='f')
                            {
                                token.setType("keyword");
                                token.setOrder(Token.ELSE_IF);
                                return token;
                            }else {
                                revertChar();
                                revertChar();
                                revertChar();
                            }
                        }else {
                            revertChar();
                            revertChar();
                        }
                    }else {
                        revertChar();
                    }
                    token.setType("keyword");
                    token.setOrder(Token.ELSE);
                    return token;
                }else if("while".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.WHILE);
                    return token;
                }else if("read".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.READ);
                    return token;
                }else if("write".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.WRITE);
                    return token;
                }else if("int".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.INT);
                    return token;
                }else if("real".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.REAL);
                    return token;
                }else if("char".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.CHAR);
                    return token;
                }else if("break".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.BREAK);
                    return token;
                }else if("continue".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.CONTINUE);
                    return token;
                }else if("print".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.PRINT);
                    return token;
                }else if("scan".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.SCAN);
                    return token;
                }else if("for".equals(s))
                {
                    token.setType("keyword");
                    token.setOrder(Token.FOR);
                    return token;
                }else {
                    if(c=='_')
                    {
                        System.out.println("lineNum: "+lineNum+"  不能以“_”结尾");
                        while (cur < len - 1) {
                            char curc = nextChar();
                            if (curc == '\n') {
                                return null;
                            }
                        }
                    }
                    token.setType("identifier");
                    token.setOrder(Token.ID);
                    token.setValue(s);
                    return token;
                }
            }
        }
        return null;
    }
    //分析number的Token
    //十进制
    private String parseNumber()
    {
        int start = cur;
        boolean hasPoint = false;
        char c;
        while ((c=nextChar())!=0)
        {
            if(isNum(c)) { }
            else if(c=='.')
            {
                if(hasPoint)
                {
                    System.out.println("lineNum: "+lineNum+"  数字有多余的“.”");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return null;
                        }
                    }
                }else {
                    hasPoint=true;
                }
            }else if(isLetterUnderline(c))
            {
                System.out.println("lineNum: "+lineNum+"  数字格式错误");
                while (cur < len - 1) {
                    char curc = nextChar();
                    if (curc == '\n') {
                        return null;
                    }
                }
            }else {
                return str.substring(start,revertChar());
            }
        }
        return null;
    }
    //十六进制
    private String parseDecNumber()
    {
        int start = cur-1;//把位置定位到x前的0
        boolean hasPoint=false;
        char c;
        while ((c=nextChar())!=0) {
            if(isDecNum(c)){}
            else if(c=='.'){
                if(hasPoint){
                    System.out.println("lineNum: "+lineNum+"  十六进制数字有多余的“.”");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return null;
                        }
                    }
                }else {
                    hasPoint=true;
                }
            } else if (isNotDecNum(c)){
                System.out.println("lineNum: "+lineNum+"  十六进制数字格式错误");
                while (cur < len - 1) {
                    char curc = nextChar();
                    if (curc == '\n') {
                        return null;
                    }
                }
            }else {
                return str.substring(start, revertChar());
            }
        }
        return null;
    }
    //分析sign的Token
    private int parseSign(char c)
    {
        switch (c) {
            case '+':
                return Token.PLUS;
            case '-':
                return Token.MINUS;
            case '*':
                return Token.MUL;
            case '/':
                char next = nextChar();
                if (next == '*')//表示有多行注释
                {
                    while (cur<len-1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            lineNum++;
                            startCol = 0;
                        }
                        if (curc == '*' && nextChar() == '/') {
                            return 0;
                        }
                    }
                    System.out.println("  缺少*/");
                    return 0;
                } else if (next == '/')//表示单行注释
                {
                    while (cur < len) {
                        char curc = nextChar();
                        if (curc == '\n'|| cur == len - 1) {
                            return 0;
                        }
                    }
                } else {
                    revertChar();
                    return Token.DIV;
                }
            case '=':
                //判断是=还是==
                if (nextChar() == '=') {
                    return Token.EQ;
                } else {
                    revertChar();
                    return Token.ASSIGN;
                }
            case '<':
                //判断是<还是<=还是<>
                char t = nextChar();
                if (t == '=') {
                    return Token.LET;
                } else if (t == '>') {
                    return Token.NEQ;
                } else {
                    revertChar();
                    return Token.LT;
                }
            case '>':
                //判断是>还是>=
                if (nextChar() == '=') {
                    return Token.GET;
                } else {
                    revertChar();
                    return Token.GT;
                }
            case '(':
                return Token.LPARENT;
            case ')':
                return Token.RPARENT;
            case ';':
                return Token.SEMI;
            case '{':
                return Token.LBRACE;
            case '}':
                return Token.RBRACE;
            case '[':
                return Token.LBRACKET;
            case ']':
                return Token.RBRACKET;
            case '&':
                if (nextChar() == '&') {
                    return Token.AND;
                } else {
                    revertChar();
                    System.out.println("lineNum: "+lineNum+"  符号'&'错误");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return 0;
                        }
                    }
                }
            case '|':
                if (nextChar() == '|') {
                    return Token.OR;
                } else {
                    revertChar();
                    System.out.println("lineNum: "+lineNum+"  符号'|'错误");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return 0;
                        }
                    }
                }
            case '!':
                return  Token.EM;
            case '\'':
                return  Token.SQM;
            case '"':
                return  Token.DQM;
            case '\\':
                char ch = nextChar();
                if(ch=='n')
                    return Token.NL;
                else if(ch=='r')
                    return  Token.CR;
                else if(ch=='t')
                    return  Token.HT;
                else if(ch=='\'')
                    return Token.SQM;
                else
                    revertChar();
                    return -1;
            case ',':
                return Token.CO;
        }
        return -1;
    }
    //获取下一个Token
    public Token next()
    {
        char c;
        while ((c = nextChar())!=0)
        {
            if(isLetter(c))//分析identifier与keyword
            {
                Token token = parseIdentifierKeyword();
                if(token!=null)
                {
                    token.setLineNum(lineNum);
                    if(startCol<=0)
                    {
                        System.out.println("lineNum: "+lineNum+"  未加终止符");
                        while (cur < len - 1) {
                            char curc = nextChar();
                            if (curc == '\n') {
                                return null;
                            }
                        }
                    }
                    if(token.getOrder()!=Token.ID)
                    {
                        token.setBeginCol(startCol+1-Token.getStr(token.getOrder()).length());
                        token.setEndCol(startCol);
                        token.setValue(Token.getStr(token.getOrder()));
                    }else {
                        token.setBeginCol(startCol+1-token.getValue().length());
                        token.setEndCol(startCol);
                    }
                }
                return token;
            }else if(isUnderLine(c))//下划线开头为错误格式
            {
                System.out.println("lineNum: "+lineNum+"  不能以“_”开头");
                while (cur < len - 1) {
                    char curc = nextChar();
                    if (curc == '\n') {
                        return null;
                    }
                }
            }
            else if(isNum(c))//分析数字
            {
                Token token = new Token();
                token.setBeginCol(startCol);
                String s;
                char next = nextChar();
                if(c=='0'&&(next=='x'||next=='X')){
                    //获取十六进制数
                    s=parseDecNumber();
                }else {
                    //获取十进制数
                    revertChar();
                    s=parseNumber();
                }
                /*if(startCol<=0)
                {
                    System.out.println("lineNum: "+lineNum+"  未加终止符");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return null;
                        }
                    }
                }*/
                if(s!=null)
                {
                    if(s.contains("."))
                    {
                        token.setOrder(Token.LITERAL_REAL);
                        token.setType("Real");
                    }else {
                        token.setOrder(Token.LITERAL_INT);
                        token.setType("Int");
                    }
                    token.setValue(s);
                    token.setLineNum(lineNum);
                    token.setEndCol(startCol);
                    return token;
                }
            }
            else if((c=='+'||c=='-'))//分析是数字还是运算符
            {
                revertChar();
                revertChar();
                char ch = nextChar();
                if(isNum(ch)||isLetter(ch))
                {
                    int t = 0;
                    char a = nextChar();
                    if(c == '+')
                    {
                        t = Token.PLUS;
                    }else if(c == '-')
                    {
                        t = Token.MINUS;
                    }else {
                        return null;
                    }
                    if(startCol<=0)
                    {
                        System.out.println("lineNum: "+lineNum+"  未加终止符");
                        while (cur < len - 1) {
                            char curc = nextChar();
                            if (curc == '\n') {
                                return null;
                            }
                        }
                    }
                    Token token = new Token();
                    token.setOrder(t);
                    token.setLineNum(lineNum);
                    token.setBeginCol(startCol);
                    token.setEndCol(startCol);
                    token.setType("Sign");
                    token.setValue(Token.getStr(t));
                    return token;
                }else {
                    char a = nextChar();
                    if(isNum(nextChar()))
                    {
                        revertChar();
                        Token token = new Token();
                        token.setBeginCol(startCol);
                        String s;
                        s=parseNumber();
                        if(startCol<=0)
                        {
                            System.out.println("lineNum: "+lineNum+"  未加终止符");
                            while (cur < len - 1) {
                                char curc = nextChar();
                                if (curc == '\n') {
                                    return null;
                                }
                            }
                        }
                        if(s.contains("."))
                        {
                            token.setOrder(Token.LITERAL_REAL);
                            token.setType("Real");
                        }else {
                            token.setOrder(Token.LITERAL_INT);
                            token.setType("Int");
                        }
                        token.setValue(s);
                        token.setLineNum(lineNum);
                        token.setEndCol(startCol);
                        return token;
                    }else {
                        int t = 0;
                        revertChar();
                        if(a == '+')
                        {
                            t = Token.PLUS;
                        }else if(a == '-')
                        {
                            t = Token.MINUS;
                        }else {
                            return null;
                        }
                        if(startCol<=0)
                        {
                            System.out.println("lineNum: "+lineNum+"  未加终止符");
                            while (cur < len - 1) {
                                char curc = nextChar();
                                if (curc == '\n') {
                                    return null;
                                }
                            }
                        }
                        Token token = new Token();
                        token.setOrder(t);
                        token.setLineNum(lineNum);
                        token.setBeginCol(startCol);
                        token.setEndCol(startCol);
                        token.setType("Sign");
                        token.setValue(Token.getStr(t));
                        return token;
                    }
                }
            }
            else if(c==' '|| c == '\t' || c == '\n'|| c=='\r')//分析空格
            {
                continue;
            } else if (c == '"')
            {
                Token token = new Token();
                int start = cur + 1;
                String s;
                token.setBeginCol(startCol);
                token.setLineNum(lineNum);
                int end = cur;
                while (cur < len - 1) {
                    char curc = nextChar();
                    end++;
                    if (curc == '\n') {
                        System.out.println("lineNum: "+lineNum+"  char数组格式错误");
                        return null;
                    }else if(curc == '"')
                    {
                        s = str.substring(start, end);
                        token.setValue(s);
                        token.setType("Chars");
                        token.setEndCol(startCol);
                        token.setOrder(Token.CHARS);
                        return token;
                    }
                }
            } else if (c == '\'')//char型的字符值
            {
                Token token = new Token();
                int start = cur + 1;
                String s;
                token.setBeginCol(startCol);
                token.setLineNum(lineNum);
                char t = nextChar();
                int end = cur;
                if (t == '\\') {
                    char ch = nextChar();
                    end++;
                    if (ch == 'n') {
                        char p = nextChar();
                        end++;
                        if (p == '\'') {
                            s = str.substring(start, end);
                            token.setValue(s);
                            token.setType("Char");
                            token.setEndCol(startCol);
                            token.setOrder(Token.LITERAL_CHAR);
                            return token;
                        } else {
                            System.out.println("lineNum: "+lineNum+"  char型字符错误");
                            while (cur < len - 1) {
                                char curc = nextChar();
                                if (curc == '\n') {
                                    return null;
                                }
                            }
                        }
                    } else if (ch == 'r') {
                        char p = nextChar();
                        end++;
                        if (p == '\'') {
                            s = str.substring(start, end);
                            token.setValue(s);
                            token.setType("Char");
                            token.setEndCol(startCol);
                            token.setOrder(Token.LITERAL_CHAR);
                            return token;
                        } else {
                            System.out.println("lineNum: "+lineNum+"  char型字符错误");
                            while (cur < len - 1) {
                                char curc = nextChar();
                                if (curc == '\n') {
                                    return null;
                                }
                            }
                        }
                    } else if (ch == 't') {
                        char p = nextChar();
                        end++;
                        if (p == '\'') {
                            s = str.substring(start, end);
                            token.setValue(s);
                            token.setType("Char");
                            token.setEndCol(startCol);
                            token.setOrder(Token.LITERAL_CHAR);
                            return token;
                        } else {
                            System.out.println("lineNum: "+lineNum+"  char型字符错误");
                            while (cur < len - 1) {
                                char curc = nextChar();
                                if (curc == '\n') {
                                    return null;
                                }
                            }
                        }
                    } else
                        revertChar();
                }
                char p = nextChar();
                end = cur;
                if (p == '\'') {
                    s = str.substring(start, end);
                    token.setValue(s);
                    token.setType("Char");
                    token.setEndCol(startCol);
                    token.setOrder(Token.LITERAL_CHAR);
                    return token;
                } else {
                    System.out.println("lineNum: "+lineNum+"  char型字符错误");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return null;
                        }
                    }
                }
            } else//分析sign
            {
                Token token = new Token();
                token.setBeginCol(startCol);
                token.setLineNum(lineNum);
                int t = parseSign(c);
                if (t > 0) {
                    token.setOrder(t);
                    token.setEndCol(startCol);
                    token.setType("Sign");
                    token.setValue(Token.getStr(t));
                    return token;
                } else if (t == -1) {
                    System.out.println("lineNum: " + lineNum + "  未定义的符号");
                    while (cur < len - 1) {
                        char curc = nextChar();
                        if (curc == '\n') {
                            return null;
                        }
                    }
                }
            }
        }
        //检查是否到达文件末尾
        if(c == 0)
        {
            return Token.EOF;
        }
        return null;
    }
}
