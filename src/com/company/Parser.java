package com.company;

import jdk.nashorn.internal.runtime.ParserException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Parser {

    private ListIterator<Token> iterator;

    public Parser(Lexer lexer)
    {
        Token token = null;
        List<Token> list = new LinkedList<>();
        while ((token=lexer.next())!=Token.EOF)
        {
            if(token!=null)
            {
                list.add(token);
                System.out.println(token.output());
            }
        }
        iterator = list.listIterator();
    }



    /**
     * 获取下一个token的order,如果没有下一个token,则返回-1
     */
    private int getNextTokenType() {
        if (iterator.hasNext()) {
            int order = iterator.next().getOrder();
            iterator.previous();
            return order;
        }
        return -1;
    }
    /**
     * 获取下一个token的lineNum,如果没有下一个token,则返回0
     */
    private int getNextTokenLine() {
        if (iterator.hasNext()) {
            int line = iterator.next().getLineNum();
            iterator.previous();
            return line;
        }
        return 0;
    }
    /**
     * 消耗下一个token,如果与所给类型不符，错误
     */
    private void consumeNextToken(int type, List<TreeNode> list) throws ParserException{
        if (iterator.hasNext()) {
            int order = iterator.next().getOrder();
            TreeNode node = new TreeNode();
            if(order==type)
            {
                iterator.previous();
                node.setLinenum(iterator.next().getLineNum());
                node.setType(order);
                node.setValue(Token.getStr(type));
                list.add(node);
            }else {
                throw new ParserException("line " + getNextTokenLine() + " : error");
            }
        }else {
            throw new ParserException("line " + getNextTokenLine() + " : no more tokens");
        }
    }

    private TreeNode parseStmt() throws ParserException {
        switch (getNextTokenType()) {
            case Token.IF: return parseIfStmt();
            case Token.WHILE: return parseWhileStmt();
            case Token.READ: return parseReadStmt();
            case Token.WRITE: return parseWriteStmt();
            case Token.INT:
            case Token.REAL:
            case Token.CHAR: return parseDeclareStmt();
            case Token.LBRACE: return parseStmtBlock();
            case Token.ID:
            case Token.EM:
            case Token.LITERAL_INT:
            case Token.LITERAL_REAL: return parseAssignStmt();
            case Token.CONTINUE: return parseContinueStmt();
            case Token.BREAK: return parseBreakStmt();
            case Token.PRINT: return parsePrintFunc();
            case Token.SCAN: return parseScanFunc();
            case Token.FOR: return parseForStmt();
            case Token.SEMI:
                TreeNode node = new TreeNode(TreeNode.SEMI_STMT);
                node.setValue(iterator.next().getValue());
                iterator.previous();
                node.setLinenum(iterator.next().getLineNum());
                return node;

            default:
                throw new ParserException("line " + getNextTokenLine() + " : error");
        }
    }
    /**
     * {  }函数体
     */
    private TreeNode parseStmtBlock() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.STMT_BLOCK);
        node.setValue(TreeNode.getStr(TreeNode.STMT_BLOCK));
        int t = getNextTokenType();
        if(t==Token.LBRACE)
        {
            consumeNextToken(Token.LBRACE, node.getList());
            TreeNode n = parseStmtBlock();
            if(n!=null)
            {
                node.getList().add(n);
            }
            consumeNextToken(Token.RBRACE, node.getList());
        }else if(t==Token.IF||t==Token.WHILE||t==Token.READ||t==Token.WRITE||t==Token.INT||t==Token.REAL||t==Token.CHAR||t==Token.ID||t==Token.EM
                ||t==Token.LITERAL_INT||t==Token.LITERAL_REAL||t==Token.CONTINUE||t==Token.BREAK||t==Token.PRINT||t==Token.SCAN||t==Token.FOR||t==Token.SEMI)
        {
            while(getNextTokenType()==Token.IF||getNextTokenType()==Token.WHILE||getNextTokenType()==Token.READ||getNextTokenType()==Token.WRITE
                    ||getNextTokenType()==Token.INT||getNextTokenType()==Token.REAL||getNextTokenType()==Token.CHAR||getNextTokenType()==Token.ID ||getNextTokenType()==Token.EM
                    ||getNextTokenType()==Token.LITERAL_INT||getNextTokenType()==Token.LITERAL_REAL||getNextTokenType()==Token.CONTINUE
                    ||getNextTokenType()==Token.BREAK||getNextTokenType()==Token.PRINT||getNextTokenType()==Token.SCAN||getNextTokenType()==Token.FOR
                    ||getNextTokenType()==Token.SEMI)
            {
                TreeNode n2 = parseStmt();
                if(n2!=null)
                {
                    node.getList().add(n2);
                }
            }
        }else {
            return null;
        }
        return node;
    }
    /**
     * 函数队列
     */
    /*private TreeNode parseStmtSeq() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.STMT_SEQ);
        node.setValue(TreeNode.getStr(TreeNode.STMT_SEQ));
        if(getNextTokenType()==-1||getNextTokenType()==Token.RBRACE)
        {
            return null;
        }else {
            while (getNextTokenType()!=-1&&getNextTokenType()!=Token.RBRACE)
            {
                node.getList().add(parseStmt());
            }
        }
        return node;
    }*/


    /**
     * if语句
     */
    private TreeNode parseIfStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.IF_STMT);
        node.setValue(TreeNode.getStr(TreeNode.IF_STMT));
        consumeNextToken(Token.IF, node.getList());
        consumeNextToken(Token.LPARENT, node.getList());
        node.getList().add(parseExp());
        consumeNextToken(Token.RPARENT, node.getList());
        if(getNextTokenType()==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
            return node;
        }
        if(getNextTokenType()==Token.LBRACE)
        {
            TreeNode n = parseStmtBlock();
            if(n!=null)
            {
                node.getList().add(n);
            }
        }else if(getNextTokenType()==Token.ELSE||getNextTokenType()==Token.ELSE_IF)
        {
            throw new ParserException("line " + getNextTokenLine() + " : 缺少语句");
        } else {
            TreeNode n = parseStmt();
            node.getList().add(n);
        }
        while (getNextTokenType()==Token.ELSE_IF)
        {
            consumeNextToken(Token.ELSE_IF, node.getList());
            consumeNextToken(Token.LPARENT, node.getList());
            node.getList().add(parseExp());
            consumeNextToken(Token.RPARENT, node.getList());
            if(getNextTokenType()==Token.LBRACE)
            {
                TreeNode n = parseStmtBlock();
                if(n!=null)
                {
                    node.getList().add(n);
                }
            }else if(getNextTokenType()==Token.ELSE||getNextTokenType()==Token.ELSE_IF)
            {
                throw new ParserException("line " + getNextTokenLine() + " : 缺少语句");
            } else {
                TreeNode n = parseStmt();
                node.getList().add(n);
            }
        }
        if(getNextTokenType()==Token.ELSE)
        {
            consumeNextToken(Token.ELSE, node.getList());
            if(getNextTokenType()==Token.LBRACE)
            {
                TreeNode n = parseStmtBlock();
                if(n!=null)
                {
                    node.getList().add(n);
                }
            }else if(getNextTokenType()==Token.ELSE||getNextTokenType()==Token.ELSE_IF)
            {
                throw new ParserException("line " + getNextTokenLine() + " : 缺少语句");
            } else {
                TreeNode n = parseStmt();
                node.getList().add(n);
            }
        }
        if(getNextTokenType()==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }
        return node;
    }
    /**
     * else语句
     */
    /*private TreeNode parseElseStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ELSE_STMT);
        node.setValue(TreeNode.getStr(TreeNode.ELSE_STMT));
        if(getNextTokenType()==Token.ELSE)
        {
            consumeNextToken(Token.ELSE, node.getList());
            TreeNode n = parseStmtBlock();
            if(n!=null)
            {
                node.getList().add(n);
            }
            return node;
        }
        return null;
    }*/
    /**
     * continue语句
     */
    private TreeNode parseContinueStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.CONTINUE_STMT);
        node.setValue(TreeNode.getStr(TreeNode.CONTINUE_STMT));
        consumeNextToken(Token.CONTINUE, node.getList());
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * break语句
     */
    private TreeNode parseBreakStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.BREAK_STMT);
        node.setValue(TreeNode.getStr(TreeNode.BREAK_STMT));
        consumeNextToken(Token.BREAK, node.getList());
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * while语句
     */
    private TreeNode parseWhileStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.WHILE_STMT);
        node.setValue(TreeNode.getStr(TreeNode.WHILE_STMT));
        consumeNextToken(Token.WHILE, node.getList());
        consumeNextToken(Token.LPARENT, node.getList());
        node.getList().add(parseExp());
        consumeNextToken(Token.RPARENT, node.getList());
        int i = getNextTokenType();
        if(i==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }else {
            TreeNode n = parseStmtBlock();
            if(n!=null)
            {
                node.getList().add(n);
            }
        }
        int j = getNextTokenType();
        if(j==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }
        return node;
    }

    /**
     * for语句
     */
    private TreeNode parseForStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.FOR_STMT);
        node.setValue(TreeNode.getStr(TreeNode.FOR_STMT));
        consumeNextToken(Token.FOR, node.getList());
        consumeNextToken(Token.LPARENT, node.getList());
        int i = getNextTokenType();
        if(i==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }else if(i==Token.INT||i==Token.REAL||i==Token.CHAR)
        {
            node.getList().add(parseDeclareStmt());
        }else {
            node.getList().add(parseAssignStmt());
        }
        int j = getNextTokenType();
        if(j==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }else {
            node.getList().add(parseExp());
            consumeNextToken(Token.SEMI, node.getList());
        }
        node.getList().add(parseAssignStmt());
        consumeNextToken(Token.RPARENT, node.getList());
        node.getList().add(parseStmtBlock());
        int t = getNextTokenType();
        if(t==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }
        return node;
    }
    /**
     * read语句
     */
    private TreeNode parseReadStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.READ_STMT);
        node.setValue(TreeNode.getStr(TreeNode.READ_STMT));
        consumeNextToken(Token.READ, node.getList());
        node.getList().add(parseVar());
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * write语句
     */
    private TreeNode parseWriteStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.WRITE_STMT);
        node.setValue(TreeNode.getStr(TreeNode.WRITE_STMT));
        consumeNextToken(Token.WRITE, node.getList());
        node.getList().add(parseExp());
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * print函数
     */
    private TreeNode parsePrintFunc() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.PRINT_FUNC);
        node.setValue(TreeNode.getStr(TreeNode.PRINT_FUNC));
        consumeNextToken(Token.PRINT, node.getList());
        consumeNextToken(Token.LPARENT, node.getList());
        node.getList().add(parseExp());
        consumeNextToken(Token.RPARENT, node.getList());
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * scan函数
     */
    private TreeNode parseScanFunc() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.SCAN_FUNC);
        node.setValue(TreeNode.getStr(TreeNode.SCAN_FUNC));
        consumeNextToken(Token.SCAN, node.getList());
        consumeNextToken(Token.LPARENT, node.getList());
        node.getList().add(parseVar());
        consumeNextToken(Token.RPARENT, node.getList());
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * assign语句
     */
    private TreeNode parseAssignStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ASSIGN_STMT);
        node.setValue(TreeNode.getStr(TreeNode.ASSIGN_STMT));
        node.getList().add(parseVar());
        int i = getNextTokenType();
        if(i==Token.ASSIGN)
        {
            consumeNextToken(Token.ASSIGN, node.getList());
            node.getList().add(parseValueStmt());
        }else if(i==Token.PLUS)
        {
            consumeNextToken(Token.PLUS, node.getList());
            consumeNextToken(Token.PLUS, node.getList());
        }else if(i==Token.MINUS)
        {
            consumeNextToken(Token.MINUS, node.getList());
            consumeNextToken(Token.MINUS, node.getList());
        }else {
            throw new ParserException("line " + getNextTokenLine() + " : 赋值错误！");
        }
        if(getNextTokenType()==Token.SEMI)
        {
            consumeNextToken(Token.SEMI, node.getList());
        }
        return node;
    }
    /**
     * declare语句
     */
    private TreeNode parseDeclareStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.DECLARE_STMT);
        node.setValue(TreeNode.getStr(TreeNode.DECLARE_STMT));
        int i = getNextTokenType();
        if(i==Token.INT||i==Token.REAL||i==Token.CHAR)
        {
            consumeNextToken(i, node.getList());
            node.getList().add(parseID(iterator.next().getValue()));
            if(getNextTokenType()==Token.LBRACKET)
            {
                consumeNextToken(Token.LBRACKET, node.getList());
                if(getNextTokenType()==Token.RBRACKET)
                {
                    consumeNextToken(Token.RBRACKET, node.getList());
                }else{
                    node.getList().add(parseExp());
                    consumeNextToken(Token.RBRACKET, node.getList());
                    while (getNextTokenType()==Token.LBRACKET)
                    {
                        consumeNextToken(Token.LBRACKET, node.getList());
                        node.getList().add(parseExp());
                        consumeNextToken(Token.RBRACKET, node.getList());
                    }
                }
            }
            if(getNextTokenType()==Token.ASSIGN)
            {
                consumeNextToken(Token.ASSIGN, node.getList());
                node.getList().add(parseValueStmt());
            }
        }else {
            throw new ParserException("line " + getNextTokenLine() + " : 声明类型错误！");
        }
        while (getNextTokenType()==Token.CO)
        {
            consumeNextToken(Token.CO, node.getList());
            node.getList().add(parseID(iterator.next().getValue()));
            if(getNextTokenType()==Token.LBRACKET)
            {
                consumeNextToken(Token.LBRACKET, node.getList());
                if(getNextTokenType()==Token.RBRACKET)
                {
                    consumeNextToken(Token.RBRACKET, node.getList());
                }else{
                    node.getList().add(parseExp());
                    consumeNextToken(Token.RBRACKET, node.getList());
                    while (getNextTokenType()==Token.LBRACKET)
                    {
                        consumeNextToken(Token.LBRACKET, node.getList());
                        node.getList().add(parseExp());
                        consumeNextToken(Token.RBRACKET, node.getList());
                    }
                }
            }
            if(getNextTokenType()==Token.ASSIGN)
            {
                consumeNextToken(Token.ASSIGN, node.getList());
                node.getList().add(parseValueStmt());
            }
        }
        consumeNextToken(Token.SEMI, node.getList());
        return node;
    }
    /**
     * declare语句的value
     */
    private TreeNode parseValueStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.VALUE_STMT);
        node.setValue(TreeNode.getStr(TreeNode.VALUE_STMT));
        int i = getNextTokenType();
        if(i==Token.LBRACE)
        {
            consumeNextToken(Token.LBRACE, node.getList());
            node.getList().add(parseExp());
            while (getNextTokenType()==Token.CO)
            {
                consumeNextToken(Token.CO, node.getList());
                node.getList().add(parseExp());
            }
            consumeNextToken(Token.RBRACE, node.getList());
        }else if(i==Token.CHARS)
        {
            node.getList().add(parseChars());
        }  else {
            node.getList().add(parseExp());
        }
        return node;
    }
    /**
     * array的value
     */
    /*private TreeNode parseArrayStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ARRAY_STMTR);
        node.setValue(TreeNode.getStr(TreeNode.ARRAY_STMTR));
        if(getNextTokenType()==Token.SCAN)
        {
            node.getList().add(parseScanFunc());
        }else {
            node.getList().add(parseExp());
        }
        return node;
    }*/
    /**
     * 复合表达式
     */
    private TreeNode parseExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.EXP);
        node.setValue(TreeNode.getStr(TreeNode.EXP));
        node.getList().add(parseOrExp());
        int i = getNextTokenType();
        if(i==Token.OR)
        {
            consumeNextToken(Token.OR, node.getList());
            node.getList().add(parseExp());
        }
        return node;
    }
    /**
     * || 表达式
     */
    private TreeNode parseOrExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.OR_EXP);
        node.setValue(TreeNode.getStr(TreeNode.OR_EXP));
        node.getList().add(parseAndExp());
        int i = getNextTokenType();
        if(i==Token.AND)
        {
            consumeNextToken(Token.AND, node.getList());
            node.getList().add(parseOrExp());
        }
        return node;
    }
    /**
     * && 表达式
     */
    private TreeNode parseAndExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.AND_EXP);
        node.setValue(TreeNode.getStr(TreeNode.AND_EXP));
        int i = getNextTokenType();
        if(i==Token.EM)
        {
            consumeNextToken(Token.EM, node.getList());
        }
        node.getList().add(parseNotExp());
        return node;
    }
    /**
     * ! 表达式
     */
    private TreeNode parseNotExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.NOT_EXP);
        node.setValue(TreeNode.getStr(TreeNode.NOT_EXP));
        node.getList().add(parseAddExp());
        while(getNextTokenType()==Token.LT||getNextTokenType()==Token.EQ||getNextTokenType()==Token.NEQ||getNextTokenType()==Token.LET
                ||getNextTokenType()==Token.GT||getNextTokenType()==Token.GET)
        {
            node.getList().add(parseLogOp());
            node.getList().add(parseAddExp());
        }
        return node;
    }
    private TreeNode parseAddExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ADD_EXP);
        node.setValue(TreeNode.getStr(TreeNode.ADD_EXP));
        node.getList().add(parseTermExp());
        while(getNextTokenType()==Token.PLUS||getNextTokenType()==Token.MINUS)
        {
            node.getList().add(parseAddOp());
            node.getList().add(parseTermExp());
        }
        return node;
    }
    private TreeNode parseTermExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.TERM);
        node.setValue(TreeNode.getStr(TreeNode.TERM));
        node.getList().add(parseFactorExp());
        while (getNextTokenType()==Token.MUL||getNextTokenType()==Token.DIV)
        {
            node.getList().add(parseMulOp());
            node.getList().add(parseFactorExp());
        }
        return node;
    }
    private TreeNode parseFactorExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.FACTOR);
        node.setValue(TreeNode.getStr(TreeNode.FACTOR));
        int i = getNextTokenType();
        if(i==Token.LPARENT)
        {
            consumeNextToken(Token.LPARENT, node.getList());
            node.getList().add(parseExp());
            consumeNextToken(Token.RPARENT, node.getList());
        }else if(i==Token.LITERAL_INT)
        {
            node.getList().add(parseInt());
        }else if (i==Token.LITERAL_REAL)
        {
            node.getList().add(parseReal());
        } else if(i==Token.ID)
        {
            node.getList().add(parseVar());
        }else if(i==Token.LITERAL_CHAR)
        {
            node.getList().add(parseChar());
        }
        return node;
    }
    private TreeNode parseInt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.LITREAL_INT);
        node.setValue(iterator.next().getValue());
        iterator.previous();
        node.setLinenum(iterator.next().getLineNum());
        return node;
    }
    private TreeNode parseReal() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.LITREAL_REAL);
        node.setValue(iterator.next().getValue());
        iterator.previous();
        node.setLinenum(iterator.next().getLineNum());
        return node;
    }
    private TreeNode parseChar() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.CHAR);
        node.setValue(iterator.next().getValue());
        iterator.previous();
        node.setLinenum(iterator.next().getLineNum());
        return node;
    }
    private TreeNode parseChars() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.CHARS);
        node.setValue(iterator.next().getValue());
        iterator.previous();
        node.setLinenum(iterator.next().getLineNum());
        return node;
    }
    private TreeNode parseLogOp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.LOG_OP);
        node.setValue(TreeNode.getStr(TreeNode.LOG_OP));
        consumeNextToken(getNextTokenType(), node.getList());
        return node;
    }
    private TreeNode parseAddOp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ADD_OP);
        node.setValue(TreeNode.getStr(TreeNode.ADD_OP));
        consumeNextToken(getNextTokenType(), node.getList());
        return node;
    }
    private TreeNode parseMulOp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.MUL_OP);
        node.setValue(TreeNode.getStr(TreeNode.MUL_OP));
        consumeNextToken(getNextTokenType(), node.getList());
        return node;
    }
    /**
     * 变量或数组
     */
    private TreeNode parseVar() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.VAR);
        node.setValue(TreeNode.getStr(TreeNode.VAR));
        int i = getNextTokenType();
        if(i==Token.ID)
        {
            node.getList().add(parseID(iterator.next().getValue()));
            if(getNextTokenType()==Token.LBRACKET)
            {
                consumeNextToken(Token.LBRACKET, node.getList());
                if(getNextTokenType()==Token.RBRACKET)
                {
                    consumeNextToken(Token.RBRACKET, node.getList());
                }else{
                    node.getList().add(parseExp());
                    consumeNextToken(Token.RBRACKET, node.getList());
                    while (getNextTokenType()==Token.LBRACKET)
                    {
                        consumeNextToken(Token.LBRACKET, node.getList());
                        node.getList().add(parseExp());
                        consumeNextToken(Token.RBRACKET, node.getList());
                    }
                }
            }
        }
        return node;
    }
    /**
     * ID
     */
    private TreeNode parseID(String s) throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ID);
        node.setValue(s);
        iterator.previous();
        node.setLinenum(iterator.next().getLineNum());
        return node;
    }
    private void PreOrder(TreeNode node, int level)
    {
        ListIterator<TreeNode> iterator = node.getList().listIterator();
        level++;
        while (iterator.hasNext())
        {
            for(int i=0;i<level;i++)
            {
                System.out.print("--");
            }
            System.out.println(iterator.next().getValue());
            iterator.previous();
            if(iterator.next().getList().size()!=0)
            {
                iterator.previous();
                PreOrder(iterator.next(),level);
            }
        }

    }
    private DefaultMutableTreeNode Jtreenode(TreeNode node)
    {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node.getValue());
        ListIterator<TreeNode> iterator = node.getList().listIterator();
        while (iterator.hasNext())
        {
            DefaultMutableTreeNode treeNode2 = Jtreenode(iterator.next());
            treeNode.add(treeNode2);
        }
        return treeNode;
    }
    /**
     * 函数
     */
    public TreeNode parseProgram() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.PROGRAM);
        node.setValue(TreeNode.getStr(TreeNode.PROGRAM));
        while (iterator.hasNext())
        {
            TreeNode n = parseStmtBlock();
            if(n!=null)
            {
                node.getList().add(n);
            }
        }
        DefaultMutableTreeNode treeNode = Jtreenode(node);
        JTree tree = new JTree(treeNode);
        JPanel panel=new JPanel();
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(0,0));
        JScrollPane scrollpane=new JScrollPane();
        scrollpane.setViewportView(tree);
        panel.add(scrollpane);
        JFrame frame=new JFrame("语法分析");
        frame.add(panel);
        frame.setUndecorated(true);
        //frame.setSize(900, 900);
        frame.setBounds(600,300,900,600);
        frame.setVisible(true);
        return node;
    }
}
