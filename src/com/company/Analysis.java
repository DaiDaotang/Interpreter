package com.company;



import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Analysis {

    private TreeNode node;

    private int line;

    public Analysis(TreeNode treeNode)
    {
        node = treeNode;
    }

    /**
     * 获取下一个treenode的类型
     */
    private int getNextNodeType(ListIterator<TreeNode> iterator)
    {
        if (iterator.hasNext())
        {
            int type = iterator.next().getType();
            iterator.previous();
            return type;
        }
        return -1;
    }

    /**
     * 获取下一个treenode的lineNum
     */
    private int getNodeLine(ListIterator<TreeNode> iterator)
    {
        iterator.previous();
        return iterator.next().getLinenum();
    }
    /**
     * 消耗下一个treenode,如果与所给类型不符，错误
     */
    private void consumeNextNode(int type, ListIterator<TreeNode> iterator) throws UnexpectedException
    {
        if (iterator.hasNext())
        {
            int order = iterator.next().getType();
            if(order==type)
            {

            }else {
                throw new UnexpectedException(getNodeLine(iterator), "识别错误！");
            }
        }else {
            throw new UnexpectedException(getNodeLine(iterator), "无更多节点！");
        }
    }

    private void analysisDeclareStmt(List<TreeNode> list, List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        int i = getNextNodeType(iterator);
        consumeNextNode(i, iterator);
        if(getNextNodeType(iterator)==TreeNode.ID)
        {
            String name = iterator.next().getValue();
            for (Var var:vars
                 ) {
                if(var.getName().equals(name))
                {
                    throw new UnexpectedException(getNodeLine(iterator), "变量已声明！");
                }
            }
            Var v = new Var();
            v.setName(name);
            if(i==Token.INT)
            {
                v.setType("int");
            }else if(i==Token.REAL)
            {
                v.setType("real");
            }else if(i==Token.CHAR)
            {
                v.setType("char");
            }else {
                throw new UnexpectedException(getNodeLine(iterator), "类型错误！");
            }
            if(getNextNodeType(iterator)==Token.LBRACKET)
            {
                v.setArrayOrNot(true);
                consumeNextNode(Token.LBRACKET,iterator);
                if(getNextNodeType(iterator)==Token.RBRACKET)
                {
                    consumeNextNode(Token.RBRACKET,iterator);
                    if(getNextNodeType(iterator)==Token.ASSIGN)
                    {
                        consumeNextNode(Token.ASSIGN,iterator);
                        Var t = analysisValueStmt(iterator.next().getList(),vars);
                        if((t.getType().equals("chars")||t.getType().equals("array"))&&i==Token.CHAR)
                        {
                            v.setType("charArray");
                            v.setArray(t.getArray());
                        }else if(t.getType().equals("array")&&i==Token.INT)
                        {
                            v.setType("intArray");
                            v.setArray(t.getArray());
                        }else if(t.getType().equals("array")&&i==Token.REAL)
                        {
                            v.setType("realArray");
                            v.setArray(t.getArray());
                        }else
                        {
                            throw new UnexpectedException(getNodeLine(iterator), "数组赋值类型不匹配！");
                        }
                        v.setLength(t.getLength());
                    }else {
                        throw new UnexpectedException(getNodeLine(iterator), "变量声明不完整！");
                    }
                }else if(getNextNodeType(iterator)==TreeNode.EXP)
                {
                    Var s = analysisExp(iterator.next().getList(),vars);
                    int length;
                    int max;
                    if(s.getType().equals("int"))
                    {
                        max = Integer.parseInt(s.getValue().toString());
                        length = Integer.parseInt(s.getValue().toString());
                    }else
                    {
                        throw new UnexpectedException(getNodeLine(iterator), "需要一个整数！");
                    }
                    v.getMax().add(max);
                    consumeNextNode(Token.RBRACKET,iterator);
                    while (getNextNodeType(iterator)==Token.LBRACKET)
                    {
                        consumeNextNode(Token.LBRACKET,iterator);
                        Var m = analysisExp(iterator.next().getList(),vars);
                        if(m.getType().equals("int"))
                        {
                            max = Integer.parseInt(s.getValue().toString());
                            length = length * Integer.parseInt(s.getValue().toString());
                        }else
                        {
                            throw new UnexpectedException(getNodeLine(iterator), "需要一个整数！");
                        }
                        v.getMax().add(max);
                        consumeNextNode(Token.RBRACKET,iterator);
                    }
                    if(getNextNodeType(iterator)==Token.ASSIGN)
                    {
                        consumeNextNode(Token.ASSIGN,iterator);
                        Var t = analysisValueStmt(iterator.next().getList(),vars);
                        if(length<t.getLength())
                        {
                            throw new UnexpectedException(getNodeLine(iterator), "数组初值太多！");
                        }
                        int n = length - t.getLength();
                        for(int m = 0;m<n; m++)
                        {
                            v.getArray().add(null);
                        }
                        v.setLength(length);
                        if((t.getType().equals("chars")||t.getType().equals("array"))&&i==Token.CHAR)
                        {
                            v.setType("charArray");
                            v.setArray(t.getArray());
                        }else if(t.getType().equals("array")&&i==Token.INT)
                        {
                            v.setType("intArray");
                            v.setArray(t.getArray());
                        }else if(t.getType().equals("array")&&i==Token.REAL)
                        {
                            v.setType("realArray");
                            v.setArray(t.getArray());
                        }else
                        {
                            throw new UnexpectedException(getNodeLine(iterator), "数组赋值类型不匹配！");
                        }
                    }else {
                        for(int m = 0;m<length; m++)
                        {
                            v.getArray().add(null);
                        }
                        v.setLength(length);
                    }
                    if(i==Token.CHAR)
                    {
                        v.setType("charArray");
                    }else if(i==Token.INT)
                    {
                        v.setType("intArray");
                    }else if(i==Token.REAL)
                    {
                        v.setType("realArray");
                    }
                }
                vars.add(v);
            } else
            {
                if(getNextNodeType(iterator)==Token.ASSIGN)
                {
                    consumeNextNode(Token.ASSIGN, iterator);
                    Var n = analysisValueStmt(iterator.next().getList(),vars);
                    if(v.getType().equals("int"))
                    {
                        if(n.getType().equals("int"))
                        {
                            v.setValue(n.getValue());
                        }else if(n.getType().equals("real"))
                        {
                            int a = Double.valueOf(n.getValue().toString()).intValue();
                            v.setValue(a);
                        }else if(n.getType().equals("char"))
                        {
                            char t = n.getValue().toString().charAt(0);
                            int a = (int) t;
                            v.setValue(a);
                        }else if(n.getType().equals("bool"))
                        {
                            if(Boolean.parseBoolean(n.getValue().toString()))
                            {
                                v.setValue(1);
                            }else {
                                v.setValue(0);
                            }
                        }
                        else {
                            throw new UnexpectedException(getNodeLine(iterator), "为int赋值错误！");
                        }
                    }else if(v.getType().equals("real"))
                    {
                        if(n.getType().equals("int"))
                        {
                            v.setValue(n.getValue());
                        }else if(n.getType().equals("real"))
                        {
                            v.setValue(n.getValue());
                        }else if(n.getType().equals("char"))
                        {
                            char t = n.getValue().toString().charAt(0);
                            int a = (int) t;
                            v.setValue(a);
                        }else if(n.getType().equals("bool"))
                        {
                            if(Boolean.parseBoolean(n.getValue().toString()))
                            {
                                v.setValue(1);
                            }else {
                                v.setValue(0);
                            }
                        }
                        else {
                            throw new UnexpectedException(getNodeLine(iterator), "为real赋值错误！");
                        }
                    }else if(v.getType().equals("char"))
                    {
                        if(n.getType().equals("int"))
                        {
                            char a = (char) Integer.parseInt(n.getValue().toString());
                            v.setValue(a);
                        }else if(n.getType().equals("real"))
                        {
                            int t = Double.valueOf(n.getValue().toString()).intValue();
                            char a = (char) t;
                            v.setValue(a);
                        }else if(n.getType().equals("char"))
                        {
                            v.setValue(n.getValue());
                        }else {
                            throw new UnexpectedException(getNodeLine(iterator), "为char赋值错误！");
                        }
                    }
                }
                vars.add(v);
            }
        }else {
            throw new UnexpectedException(getNodeLine(iterator), "应该声明一个变量！");
        }
        while (getNextNodeType(iterator)==Token.CO)
        {
            consumeNextNode(Token.CO,iterator);
            if(getNextNodeType(iterator)==TreeNode.ID)
            {
                String name = iterator.next().getValue();
                for (Var var:vars
                ) {
                    if(var.getName().equals(name))
                    {
                        throw new UnexpectedException(getNodeLine(iterator), "变量已声明！");
                    }
                }
                Var v = new Var();
                v.setName(name);
                if(i==Token.INT)
                {
                    v.setType("int");
                }else if(i==Token.REAL)
                {
                    v.setType("real");
                }else if(i==Token.CHAR)
                {
                    v.setType("char");
                }else {
                    throw new UnexpectedException(getNodeLine(iterator), "类型错误！");
                }
                if(getNextNodeType(iterator)==Token.LBRACKET)
                {
                    v.setArrayOrNot(true);
                    consumeNextNode(Token.LBRACKET,iterator);
                    if(getNextNodeType(iterator)==Token.RBRACKET)
                    {
                        consumeNextNode(Token.RBRACKET,iterator);
                        if(getNextNodeType(iterator)==Token.ASSIGN)
                        {
                            consumeNextNode(Token.ASSIGN,iterator);
                            Var t = analysisValueStmt(iterator.next().getList(),vars);
                            if((t.getType().equals("chars")||t.getType().equals("array"))&&i==Token.CHAR)
                            {
                                v.setType("charArray");
                                v.setArray(t.getArray());
                            }else if(t.getType().equals("array")&&i==Token.INT)
                            {
                                v.setType("intArray");
                                v.setArray(t.getArray());
                            }else if(t.getType().equals("array")&&i==Token.REAL)
                            {
                                v.setType("realArray");
                                v.setArray(t.getArray());
                            }else
                            {
                                throw new UnexpectedException(getNodeLine(iterator), "数组赋值类型不匹配！");
                            }
                            v.setLength(t.getLength());
                        }else {
                            throw new UnexpectedException(getNodeLine(iterator), "变量声明不完整！");
                        }
                    }else if(getNextNodeType(iterator)==TreeNode.EXP)
                    {
                        Var s = analysisExp(iterator.next().getList(),vars);
                        int length;
                        if(s.getType().equals("int"))
                        {
                            length = Integer.parseInt(s.getValue().toString());
                        }else
                        {
                            throw new UnexpectedException(getNodeLine(iterator), "需要一个整数！");
                        }
                        consumeNextNode(Token.RBRACKET,iterator);
                        while (getNextNodeType(iterator)==Token.LBRACKET)
                        {
                            consumeNextNode(Token.LBRACKET,iterator);
                            Var m = analysisExp(iterator.next().getList(),vars);
                            if(m.getType().equals("int"))
                            {
                                length = length * Integer.parseInt(s.getValue().toString());
                            }else
                            {
                                throw new UnexpectedException(getNodeLine(iterator), "需要一个整数！");
                            }
                            consumeNextNode(Token.RBRACKET,iterator);
                        }
                        if(getNextNodeType(iterator)==Token.ASSIGN)
                        {
                            consumeNextNode(Token.ASSIGN,iterator);
                            Var t = analysisValueStmt(iterator.next().getList(),vars);
                            if(length<t.getLength())
                            {
                                throw new UnexpectedException(getNodeLine(iterator), "数组初值太多！");
                            }
                            int n = length - t.getLength();
                            for(int m = 0;m<n; m++)
                            {
                                v.getArray().add(null);
                            }
                            v.setLength(length);
                            if((t.getType().equals("chars")||t.getType().equals("array"))&&i==Token.CHAR)
                            {
                                v.setType("charArray");
                                v.setArray(t.getArray());
                            }else if(t.getType().equals("array")&&i==Token.INT)
                            {
                                v.setType("intArray");
                                v.setArray(t.getArray());
                            }else if(t.getType().equals("array")&&i==Token.REAL)
                            {
                                v.setType("realArray");
                                v.setArray(t.getArray());
                            }else
                            {
                                throw new UnexpectedException(getNodeLine(iterator), "数组赋值类型不匹配！");
                            }
                        }else {
                            for(int m = 0;m<length; m++)
                            {
                                v.getArray().add(null);
                            }
                            v.setLength(length);
                        }
                    }
                    vars.add(v);
                } else
                {
                    if(getNextNodeType(iterator)==Token.ASSIGN)
                    {
                        consumeNextNode(Token.ASSIGN, iterator);
                        Var n = analysisValueStmt(iterator.next().getList(),vars);
                        if(v.getType().equals("int"))
                        {
                            if(n.getType().equals("int"))
                            {
                                v.setValue(n.getValue());
                            }else if(n.getType().equals("real"))
                            {
                                int a = Double.valueOf(n.getValue().toString()).intValue();
                                v.setValue(a);
                            }else if(n.getType().equals("char"))
                            {
                                char t = n.getValue().toString().charAt(0);
                                int a = (int) t;
                                v.setValue(a);
                            }else if(n.getType().equals("bool"))
                            {
                                if(Boolean.parseBoolean(n.getValue().toString()))
                                {
                                    v.setValue(1);
                                }else {
                                    v.setValue(0);
                                }
                            }
                            else {
                                throw new UnexpectedException(getNodeLine(iterator), "为int赋值错误！");
                            }
                        }else if(v.getType().equals("real"))
                        {
                            if(n.getType().equals("int"))
                            {
                                v.setValue(n.getValue());
                            }else if(n.getType().equals("real"))
                            {
                                v.setValue(n.getValue());
                            }else if(n.getType().equals("char"))
                            {
                                char t = n.getValue().toString().charAt(0);
                                int a = (int) t;
                                v.setValue(a);
                            }else if(n.getType().equals("bool"))
                            {
                                if(Boolean.parseBoolean(n.getValue().toString()))
                                {
                                    v.setValue(1);
                                }else {
                                    v.setValue(0);
                                }
                            }
                            else {
                                throw new UnexpectedException(getNodeLine(iterator), "为real赋值错误！");
                            }
                        }else if(v.getType().equals("char"))
                        {
                            if(n.getType().equals("int"))
                            {
                                char a = (char) Integer.parseInt(n.getValue().toString());
                                v.setValue(a);
                            }else if(n.getType().equals("real"))
                            {
                                int t = Double.valueOf(n.getValue().toString()).intValue();
                                char a = (char) t;
                                v.setValue(a);
                            }else if(n.getType().equals("char"))
                            {
                                v.setValue(n.getValue());
                            }else {
                                throw new UnexpectedException(getNodeLine(iterator), "为char赋值错误！");
                            }
                        }
                    }
                    vars.add(v);
                }
            }else {
                throw new UnexpectedException(getNodeLine(iterator), "应该声明一个变量！");
            }
        }
        consumeNextNode(Token.SEMI, iterator);
    }

    private void analysisAssignStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var v = analysisVar(iterator.next().getList(),vars);
        String name = v.getName();
        Var p = new Var();
        for (Var var:vars
        ) {
            if(var.getName().equals(name))
            {
                p = var;
            }
        }
        if(p.isArrayOrNot())
        {
            if(getNextNodeType(iterator)==Token.PLUS)
            {
                consumeNextNode(Token.PLUS, iterator);
                consumeNextNode(Token.PLUS, iterator);
                if(getNextNodeType(iterator)==Token.SEMI)
                {
                    consumeNextNode(Token.SEMI, iterator);
                }
                if(v.getType().equals("int"))
                {
                    p.getArray().set(v.getOrder(), Integer.parseInt(v.getValue().toString()) + 1);
                }else if(v.getType().equals("real"))
                {
                    p.getArray().set(v.getOrder(), Double.valueOf(v.getValue().toString()) + 1);
                }else if(v.getType().equals("char"))
                {
                    p.getArray().set(v.getOrder(),v.getValue().toString().charAt(0) + 1);
                }else if(v.isArrayOrNot())
                {
                    throw new UnexpectedException(getNodeLine(iterator), "不能对数组进行++！");
                }
            }else if(getNextNodeType(iterator)==Token.MINUS)
            {
                consumeNextNode(Token.MINUS, iterator);
                consumeNextNode(Token.MINUS, iterator);
                if(getNextNodeType(iterator)==Token.SEMI)
                {
                    consumeNextNode(Token.SEMI, iterator);
                }
                if(v.getType().equals("int"))
                {
                    p.getArray().set(v.getOrder(), Integer.parseInt(v.getValue().toString()) - 1);
                }else if(v.getType().equals("real"))
                {
                    p.getArray().set(v.getOrder(), Double.valueOf(v.getValue().toString()) - 1);
                }else if(v.getType().equals("char"))
                {
                    p.getArray().set(v.getOrder(),v.getValue().toString().charAt(0) - 1);
                }else if(v.isArrayOrNot())
                {
                    throw new UnexpectedException(getNodeLine(iterator), "不能对数组进行--！");
                }
            }else if(getNextNodeType(iterator)==Token.ASSIGN)
            {
                consumeNextNode(Token.ASSIGN, iterator);
                Var n = analysisValueStmt(iterator.next().getList(),vars);
                if(v.getType().equals("int"))
                {
                    if(n.getType().equals("int"))
                    {
                        int t = Integer.parseInt(n.getValue().toString());
                        p.getArray().set(v.getOrder(), t);
                    }else if(n.getType().equals("real"))
                    {
                        double a = Double.valueOf(n.getValue().toString());
                        int w = (int) a;
                        p.getArray().set(v.getOrder(), w);
                    }else if(n.getType().equals("char"))
                    {
                        p.getArray().set(v.getOrder(),(int) n.getValue().toString().charAt(0));
                    }else if(n.getType().equals("bool"))
                    {
                        if(Boolean.parseBoolean(n.getValue().toString()))
                        {
                            p.getArray().set(v.getOrder(),1);
                        }else {
                            p.getArray().set(v.getOrder(),0);
                        }
                    }
                    else {
                        throw new UnexpectedException(getNodeLine(iterator), "为int赋值错误！");
                    }
                }else if(v.getType().equals("real"))
                {
                    if(n.getType().equals("int"))
                    {
                        p.getArray().set(v.getOrder(), Integer.parseInt(n.getValue().toString()));
                    }else if(n.getType().equals("real"))
                    {
                        p.getArray().set(v.getOrder(), Integer.parseInt(n.getValue().toString()));
                    }else if(n.getType().equals("char"))
                    {
                        p.getArray().set(v.getOrder(),(int) n.getValue().toString().charAt(0));
                    }else if(n.getType().equals("bool"))
                    {
                        if(Boolean.parseBoolean(n.getValue().toString()))
                        {
                            p.getArray().set(v.getOrder(),1);
                        }else {
                            p.getArray().set(v.getOrder(),0);
                        }
                    }
                    else {
                        throw new UnexpectedException(getNodeLine(iterator), "为real赋值错误！");
                    }
                }else if(v.getType().equals("char"))
                {
                    if(n.getType().equals("int"))
                    {
                        char a = (char) Integer.parseInt(n.getValue().toString());
                        p.getArray().set(v.getOrder(), a);
                    }else if(n.getType().equals("real"))
                    {
                        int t = Double.valueOf(n.getValue().toString()).intValue();
                        char a = (char) t;
                        p.getArray().set(v.getOrder(),a);
                    }else if(n.getType().equals("char"))
                    {
                        p.getArray().set(v.getOrder(),n.getValue());
                    }else {
                        throw new UnexpectedException(getNodeLine(iterator), "为char赋值错误！");
                    }
                }
            }
        }else {
            if(getNextNodeType(iterator)==Token.PLUS)
            {
                consumeNextNode(Token.PLUS, iterator);
                consumeNextNode(Token.PLUS, iterator);
                if(getNextNodeType(iterator)==Token.SEMI)
                {
                    consumeNextNode(Token.SEMI, iterator);
                }
                if(v.getType().equals("int"))
                {
                    v.setValue(Integer.parseInt(v.getValue().toString()) + 1);
                }else if(v.getType().equals("real"))
                {
                    v.setValue(Double.valueOf(v.getValue().toString()) + 1);
                }else if(v.getType().equals("char"))
                {
                    char t = v.getValue().toString().charAt(0);
                    v.setValue(t + 1);
                }else if(v.isArrayOrNot())
                {
                    throw new UnexpectedException(getNodeLine(iterator), "不能对数组进行++！");
                }
            }else if(getNextNodeType(iterator)==Token.MINUS)
            {
                consumeNextNode(Token.MINUS, iterator);
                consumeNextNode(Token.MINUS, iterator);
                if(getNextNodeType(iterator)==Token.SEMI)
                {
                    consumeNextNode(Token.SEMI, iterator);
                }
                if(v.getType().equals("int"))
                {
                    v.setValue(Integer.parseInt(v.getValue().toString()) - 1);
                }else if(v.getType().equals("real"))
                {
                    v.setValue(Double.valueOf(v.getValue().toString()) - 1);
                }else if(v.getType().equals("char"))
                {
                    char t = v.getValue().toString().charAt(0);
                    v.setValue(t - 1);
                }else if(v.isArrayOrNot())
                {
                    throw new UnexpectedException(getNodeLine(iterator), "不能对数组进行--！");
                }
            }else if(getNextNodeType(iterator)==Token.ASSIGN)
            {
                consumeNextNode(Token.ASSIGN, iterator);
                Var n = analysisValueStmt(iterator.next().getList(),vars);
                if(v.isArrayOrNot())
                {
                    if(n.getType().equals("array")&&(v.getType().equals("int")||v.getType().equals("real")))
                    {
                        v.setArray(n.getArray());
                    }else if((n.getType().equals("array")||n.getType().equals("chars"))&&v.getType().equals("char"))
                    {
                        v.setArray(n.getArray());
                    }else {
                        throw new UnexpectedException(getNodeLine(iterator), "数组赋值错误！");
                    }
                }else {
                    if(v.getType().equals("int"))
                    {
                        if(n.getType().equals("int"))
                        {
                            v.setValue(n.getValue());
                        }else if(n.getType().equals("real"))
                        {
                            int a = Double.valueOf(n.getValue().toString()).intValue();
                            v.setValue(a);
                        }else if(n.getType().equals("char"))
                        {
                            char t = n.getValue().toString().charAt(0);
                            int a = (int) t;
                            v.setValue(a);
                        }else if(n.getType().equals("bool"))
                        {
                            if(Boolean.parseBoolean(n.getValue().toString()))
                            {
                                v.setValue(1);
                            }else {
                                v.setValue(0);
                            }
                        }
                        else {
                            throw new UnexpectedException(getNodeLine(iterator), "为int赋值错误！");
                        }
                    }else if(v.getType().equals("real"))
                    {
                        if(n.getType().equals("int"))
                        {
                            v.setValue(n.getValue());
                        }else if(n.getType().equals("real"))
                        {
                            v.setValue(n.getValue());
                        }else if(n.getType().equals("char"))
                        {
                            char t = n.getValue().toString().charAt(0);
                            int a = (int) t;
                            v.setValue(a);
                        }else if(n.getType().equals("bool"))
                        {
                            if(Boolean.parseBoolean(n.getValue().toString()))
                            {
                                v.setValue(1);
                            }else {
                                v.setValue(0);
                            }
                        }
                        else {
                            throw new UnexpectedException(getNodeLine(iterator), "为real赋值错误！");
                        }
                    }else if(v.getType().equals("char"))
                    {
                        if(n.getType().equals("int"))
                        {
                            char a = (char) Integer.parseInt(n.getValue().toString());
                            v.setValue(a);
                        }else if(n.getType().equals("real"))
                        {
                            int t = Double.valueOf(n.getValue().toString()).intValue();
                            char a = (char) t;
                            v.setValue(a);
                        }else if(n.getType().equals("char"))
                        {
                            v.setValue(n.getValue());
                        }else {
                            throw new UnexpectedException(getNodeLine(iterator), "为char赋值错误！");
                        }
                    }
                }
            }
        }
    }

    private Var analysisValueStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var n = new Var();
        if(getNextNodeType(iterator)==Token.LBRACE)
        {
            consumeNextNode(Token.LBRACE,iterator);
            n.getArray().add(analysisExp(iterator.next().getList(),vars).getValue());
            int i = 1;
            while (getNextNodeType(iterator)==Token.CO)
            {
                consumeNextNode(Token.CO,iterator);
                n.getArray().add(analysisExp(iterator.next().getList(),vars).getValue());
                i++;
            }
            consumeNextNode(Token.RBRACE,iterator);
            n.setLength(i);
            n.setType("array");
        }else if(getNextNodeType(iterator)==TreeNode.CHARS)
        {
            n.setType("chars");
            String s = iterator.next().getValue();
            for(int i=0;i<s.length();i++)
            {
                String ss = String.valueOf(s.charAt(i));
                n.getArray().add(ss);
            }
            iterator.previous();
            n.setLength(iterator.next().getValue().length());
        }else {
            n = analysisExp(iterator.next().getList(),vars);
        }
        return n;
    }

    public void analysisProgram() throws UnexpectedException
    {
        if(node.getValue().equals(TreeNode.getStr(TreeNode.PROGRAM)))
        {
            ListIterator<TreeNode> iterator = node.getList().listIterator();
            List<Var> vars = new ArrayList<>();
            int i = analysisStmtBlock(iterator.next().getList(),vars);
            if(i!=0)
            {
                throw new UnexpectedException(line, "跳出循环错误！");
            }
        }
    }

    private int analysisStmtBlock(List<TreeNode> list, List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        List<Var> var = new ArrayList<>(vars);
        if(iterator.hasNext())
        {
            if(getNextNodeType(iterator)==Token.LBRACE)
            {
                consumeNextNode(Token.LBRACE, iterator);
                int i = analysisStmtBlock(iterator.next().getList(),var);
                if(i!=0)
                {
                    return i;
                }
                consumeNextNode(Token.RBRACE, iterator);
            }else {
                while (iterator.hasNext())
                {
                    int i = analysisStmt(iterator.next(),var);
                    if(i!=0)
                    {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    private int analysisStmt(TreeNode node, List<Var> vars) throws UnexpectedException
    {
        switch (node.getType())
        {
            case TreeNode.IF_STMT: return analysisIfStmt(node.getList(),vars);
            case TreeNode.DECLARE_STMT: analysisDeclareStmt(node.getList(),vars); return 0;
            case TreeNode.ASSIGN_STMT: analysisAssignStmt(node.getList(),vars); return 0;
            case TreeNode.STMT_BLOCK: return analysisStmtBlock(node.getList(),vars);
            case TreeNode.WHILE_STMT: analysisWhileStmt(node.getList(),vars); return 0;
            case TreeNode.FOR_STMT: analysisForStmt(node.getList(),vars); return 0;
            case TreeNode.BREAK_STMT: line = node.getLinenum();  return -1;
            case TreeNode.CONTINUE_STMT: line = node.getLinenum(); return 1;
            case TreeNode.WRITE_STMT: analysisWriteStmt(node.getList(),vars); return 0;
            case TreeNode.READ_STMT: analysisReadStmt(node.getList(),vars); return 0;
            case TreeNode.PRINT_FUNC: analysisPrintFunc(node.getList(),vars); return 0;
            case TreeNode.SCAN_FUNC: analysisScanFunc(node.getList(),vars); return 0;



            case TreeNode.SEMI_STMT:  return 0;
        }
        return 0;
    }

    private int analysisIfStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        consumeNextNode(Token.IF, iterator);
        consumeNextNode(Token.LPARENT, iterator);
        Var v = analysisExp(iterator.next().getList(),vars);
        boolean b;
        if(v.getType().equals("bool"))
        {
            b = Boolean.parseBoolean(v.getValue().toString());
        }else if(v.getType().equals("int"))
        {
            int t = Integer.parseInt(v.getValue().toString());
            if(t==0)
            {
                b = false;
            }else {
                b = true;
            }
        }else if(v.getType().equals("real"))
        {
            double t = Double.valueOf(v.getValue().toString());
            if(t==0)
            {
                b = false;
            }else {
                b = true;
            }
        }
        else {
            throw new UnexpectedException(getNodeLine(iterator), "不能用char做if判断！");
        }
        if(b)
        {
            consumeNextNode(Token.RPARENT, iterator);
            if(iterator.hasNext())
            {
                if(getNextNodeType(iterator)==TreeNode.STMT_BLOCK)
                {
                    int i = analysisStmtBlock(iterator.next().getList(),vars);
                    if(i!=0)
                    {
                        return i;
                    }
                }else {
                    int i = analysisStmt(iterator.next(),vars);
                    if(i!=0)
                    {
                        return i;
                    }
                }
            }
        }else {
            consumeNextNode(Token.RPARENT, iterator);
            iterator.next();
            while (getNextNodeType(iterator)==Token.ELSE_IF)
            {
                consumeNextNode(Token.ELSE_IF, iterator);
                consumeNextNode(Token.LPARENT, iterator);
                Var var = analysisExp(iterator.next().getList(),vars);
                boolean p;
                if(var.getType().equals("bool"))
                {
                    p = Boolean.parseBoolean(var.getValue().toString());
                }else if(var.getType().equals("int"))
                {
                    int t = Integer.parseInt(var.getValue().toString());
                    if(t==0)
                    {
                        p = false;
                    }else {
                        p = true;
                    }
                }else if(var.getType().equals("real"))
                {
                    double t = Double.valueOf(var.getValue().toString());
                    if(t==0)
                    {
                        p = false;
                    }else {
                        p = true;
                    }
                }
                else {
                    throw new UnexpectedException(getNodeLine(iterator), "不能用char做if判断！");
                }
                if(p)
                {
                    consumeNextNode(Token.RPARENT, iterator);
                    if(iterator.hasNext())
                    {
                        if(getNextNodeType(iterator)==TreeNode.STMT_BLOCK)
                        {
                            int i = analysisStmtBlock(iterator.next().getList(),vars);
                            if(i!=0)
                            {
                                return i;
                            }
                            return 0;
                        }else {
                            int i = analysisStmt(iterator.next(),vars);
                            if(i!=0)
                            {
                                return i;
                            }
                            return 0;
                        }
                    }
                }else {
                    consumeNextNode(Token.RPARENT, iterator);
                    iterator.next();
                }
            }
            if(getNextNodeType(iterator)==Token.ELSE)
            {
                consumeNextNode(Token.ELSE, iterator);
                if(iterator.hasNext())
                {
                    if(getNextNodeType(iterator)==TreeNode.STMT_BLOCK)
                    {
                        int i = analysisStmtBlock(iterator.next().getList(),vars);
                        if(i!=0)
                        {
                            return i;
                        }
                        return 0;
                    }else {
                        int i = analysisStmt(iterator.next(),vars);
                        if(i!=0)
                        {
                            return i;
                        }
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    private void analysisWhileStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        consumeNextNode(Token.WHILE, iterator);
        consumeNextNode(Token.LPARENT, iterator);
        boolean b = (boolean)analysisExp(iterator.next().getList(),vars).getValue();
        while (b)
        {
            consumeNextNode(Token.RPARENT, iterator);
            int i = analysisStmtBlock(iterator.next().getList(),vars);
            if(i==-1)
            {
                break;
            }else if(i==1)
            {
                iterator.previous();
                iterator.previous();
                continue;
            }
            iterator.previous();
            iterator.previous();
            iterator.previous();
            b = (boolean)analysisExp(iterator.next().getList(),vars).getValue();
        }
    }

    private void analysisForStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        List<Var> varList = new ArrayList<>(vars);
        consumeNextNode(Token.FOR, iterator);
        consumeNextNode(Token.LPARENT, iterator);
        int i = getNextNodeType(iterator);
        if(i==Token.SEMI)
        {
            consumeNextNode(Token.SEMI, iterator);
        }else if(i==Token.INT||i==Token.REAL||i==Token.CHAR)
        {
            analysisDeclareStmt(iterator.next().getList(),varList);
        }else {
            analysisAssignStmt(iterator.next().getList(),varList);
        }
        int j = getNextNodeType(iterator);
        boolean b;
        if(j==Token.SEMI)
        {
            consumeNextNode(Token.SEMI, iterator);
            b = true;
            while (b)
            {
                if(getNextNodeType(iterator)==TreeNode.ASSIGN_STMT)
                {
                    iterator.next();
                    consumeNextNode(Token.RPARENT, iterator);
                    int m = analysisStmtBlock(iterator.next().getList(),varList);
                    if(m==-1)
                    {
                        break;
                    }else if(m==1)
                    {
                        iterator.previous();
                        iterator.previous();
                        iterator.previous();
                        analysisAssignStmt(iterator.next().getList(),varList);
                        iterator.previous();
                        continue;
                    }
                    iterator.previous();
                    iterator.previous();
                    iterator.previous();
                    analysisAssignStmt(iterator.next().getList(),varList);
                    iterator.previous();
                }else if(getNextNodeType(iterator)==Token.RPARENT)
                {
                    consumeNextNode(Token.RPARENT, iterator);
                    int m = analysisStmtBlock(iterator.next().getList(),varList);
                    if(m==-1)
                    {
                        break;
                    }else if(m==1)
                    {
                        iterator.previous();
                        iterator.previous();
                        continue;
                    }
                    iterator.previous();
                    iterator.previous();
                }else {
                    throw new UnexpectedException(getNodeLine(iterator), "for循环使用错误！");
                }
            }
        }else {
            b = (boolean) analysisExp(iterator.next().getList(),varList).getValue();
            consumeNextNode(Token.SEMI, iterator);
            while (b)
            {
                if(getNextNodeType(iterator)==TreeNode.ASSIGN_STMT)
                {
                    iterator.next();
                    consumeNextNode(Token.RPARENT, iterator);
                    int m = analysisStmtBlock(iterator.next().getList(),varList);
                    if(m==-1)
                    {
                        break;
                    }else if(m==1)
                    {
                        iterator.previous();
                        iterator.previous();
                        iterator.previous();
                        analysisAssignStmt(iterator.next().getList(),varList);
                        iterator.previous();
                        iterator.previous();
                        iterator.previous();
                        b = (boolean)analysisExp(iterator.next().getList(),varList).getValue();
                        continue;
                    }
                    iterator.previous();
                    iterator.previous();
                    iterator.previous();
                    analysisAssignStmt(iterator.next().getList(),varList);
                    iterator.previous();
                    iterator.previous();
                    iterator.previous();
                    b = (boolean)analysisExp(iterator.next().getList(),varList).getValue();
                    consumeNextNode(Token.SEMI, iterator);
                }else if(getNextNodeType(iterator)==Token.RPARENT)
                {
                    consumeNextNode(Token.RPARENT, iterator);
                    int m = analysisStmtBlock(iterator.next().getList(),varList);
                    if(m==-1)
                    {
                        break;
                    }else if(m==1)
                    {
                        iterator.previous();
                        iterator.previous();
                        iterator.previous();
                        iterator.previous();
                        iterator.previous();
                        b = (boolean)analysisExp(iterator.next().getList(),varList).getValue();
                        continue;
                    }
                    iterator.previous();
                    iterator.previous();
                    iterator.previous();
                    iterator.previous();
                    iterator.previous();
                    b = (boolean)analysisExp(iterator.next().getList(),varList).getValue();
                }else {
                    throw new UnexpectedException(getNodeLine(iterator), "for循环使用错误！");
                }
            }
        }
    }

    private void analysisWriteStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        consumeNextNode(Token.WRITE, iterator);
        Var v = analysisExp(iterator.next().getList(),vars);
        System.out.println(v.getValue());
    }

    private void analysisReadStmt(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        consumeNextNode(Token.READ, iterator);
        Var v = analysisVar(iterator.next().getList(),vars);
        Scanner scan = new Scanner(System.in);
        String value = scan.nextLine();
        if(v.getType().equals("int"))
        {
            int t = Integer.parseInt(value);
            v.setValue(t);
        }else if(v.getType().equals("real"))
        {
            double t = Double.valueOf(value);
            v.setValue(t);
        }else if(v.getType().equals("char"))
        {
            char t = value.charAt(0);
            v.setValue(t);
        }
    }

    private void analysisPrintFunc(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        consumeNextNode(Token.PRINT, iterator);
        consumeNextNode(Token.LPARENT, iterator);
        Var v = analysisExp(iterator.next().getList(),vars);
        System.out.println(v.getValue());
    }

    private void analysisScanFunc(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        consumeNextNode(Token.SCAN, iterator);
        consumeNextNode(Token.LPARENT, iterator);
        Var v = new Var();
        List<TreeNode> l = iterator.next().getList();
        ListIterator<TreeNode> i = l.listIterator();
        if(i.next().getType()==TreeNode.ID)
        {
            if(getNextNodeType(i)!=Token.LBRACKET)
            {
                v = analysisVar(l,vars);
                Scanner scan = new Scanner(System.in);
                String value = scan.nextLine();
                if(v.getType().equals("int"))
                {
                    int t = Integer.parseInt(value);
                    v.setValue(t);
                }else if(v.getType().equals("real"))
                {
                    double t = Double.valueOf(value);
                    v.setValue(t);
                }else if(v.getType().equals("char"))
                {
                    char t = value.charAt(0);
                    v.setValue(t);
                }
            }else {
                i.previous();
                String name = i.next().getValue();
                for (Var var:vars
                ) {
                    if(var.getName().equals(name))
                    {
                        v = var;
                        int a;
                        int order;
                        consumeNextNode(Token.LBRACKET, i);
                        Var t = analysisExp(i.next().getList(),vars);
                        if(t.getType().equals("int"))
                        {
                            a = Integer.parseInt(t.getValue().toString());
                        }else {
                            throw new UnexpectedException(getNodeLine(i), "必须为整数！");
                        }
                        order = a;
                        consumeNextNode(Token.RBRACKET, i);
                        if(getNextNodeType(i)==Token.LBRACKET)
                        {
                            int e = v.getMax().size();
                            int x = 1;
                            int s = 1;
                            for(int p = s;p < e;p++)
                            {
                                x = x * v.getMax().get(p);
                            }
                            order = a * x;
                            while (getNextNodeType(i)==Token.LBRACKET)
                            {
                                consumeNextNode(Token.LBRACKET, i);
                                Var w = analysisExp(i.next().getList(),vars);
                                if(w.getType().equals("int"))
                                {
                                    a = Integer.parseInt(w.getValue().toString());
                                }
                                s++;
                                int u = 1;
                                for(int o = s ;o < e;o++)
                                {
                                    u = u * v.getMax().get(o);
                                }
                                order = order + a * u;
                                consumeNextNode(Token.RBRACKET, i);
                            }
                        }
                        Scanner scan = new Scanner(System.in);
                        String value = scan.nextLine();
                        if(var.getType().equals("intArray"))
                        {
                            int p = Integer.parseInt(value);
                            var.getArray().set(order,p);
                        }else if(var.getType().equals("realArray"))
                        {
                            double p = Double.valueOf(value);
                            var.getArray().set(order,p);
                        }else if(var.getType().equals("charArray"))
                        {
                            char p = value.charAt(0);
                            var.getArray().set(order,p);
                        }
                    }
                }
                if(!v.getName().equals(name))
                {
                    throw new UnexpectedException(getNodeLine(iterator), "变量未声明！");
                }
            }
        }

    }

    private Var analysisExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var n = analysisOrExp(iterator.next().getList(),vars);
        if(getNextNodeType(iterator)==Token.OR)
        {
            consumeNextNode(Token.OR, iterator);
            Var m = analysisExp(iterator.next().getList(),vars);
            Var v = new Var();
            if(Boolean.parseBoolean(m.getValue().toString())||Boolean.parseBoolean(n.getValue().toString()))
            {
                v.setValue(true);
            }else {
                v.setValue(false);
            }
            v.setType("bool");
            return v;
        }else {
            return n;
        }
    }

    private Var analysisOrExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var n = analysisAndExp(iterator.next().getList(),vars);
        if(getNextNodeType(iterator)==Token.AND)
        {
            consumeNextNode(Token.AND, iterator);
            Var m = analysisOrExp(iterator.next().getList(),vars);
            Var v = new Var();
            if(Boolean.parseBoolean(m.getValue().toString())&&Boolean.parseBoolean(n.getValue().toString()))
            {
                v.setValue(true);
            }else {
                v.setValue(false);
            }
            v.setType("bool");
            return v;
        }else {
            return n;
        }

    }

    private Var analysisAndExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        if(getNextNodeType(iterator)==Token.EM)
        {
            consumeNextNode(Token.EM, iterator);
            Var m = analysisNotExp(iterator.next().getList(),vars);
            Var v = new Var();
            if(Boolean.parseBoolean(m.getValue().toString()))
            {
                v.setValue(false);
            }else {
                v.setValue(true);
            }
            v.setType("bool");
            return v;
        }else {
            return analysisNotExp(iterator.next().getList(),vars);
        }
    }

    private Var analysisNotExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException
    {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var n = analysisAddExp(iterator.next().getList(),vars);
        while (getNextNodeType(iterator)==TreeNode.LOG_OP)
        {
            ListIterator<TreeNode> t = iterator.next().getList().listIterator();
            Var var = analysisAddExp(iterator.next().getList(),vars);
            n = compute(n,var,t.next().getType(),iterator);
        }
        return n;
    }

    private int operateInt(int a, int b, int op, ListIterator<TreeNode> iterator) throws UnexpectedException {
        switch (op)
        {
            case Token.PLUS: return a+b;
            case Token.MINUS: return a-b;
            case Token.MUL: return a*b;
            case Token.DIV:
                if(b==0)
                {
                    throw new UnexpectedException(getNodeLine(iterator), "除数不能为零！");
                }
                return a/b;
                default: throw new UnexpectedException(getNodeLine(iterator), "运算符错误！");
        }
    }

    private double operateReal(double a, double b, int op, ListIterator<TreeNode> iterator) throws UnexpectedException {
        switch (op)
        {
            case Token.PLUS: return a+b;
            case Token.MINUS: return a-b;
            case Token.MUL: return a*b;
            case Token.DIV:
                if(b==0)
                {
                    throw new UnexpectedException(getNodeLine(iterator), "除数不能为零！");
                }
                return a/b;
            default: throw new UnexpectedException(getNodeLine(iterator), "运算符错误！");

        }
    }

    private boolean operateBool(double a, double b, int op , ListIterator<TreeNode> iterator) throws UnexpectedException {
        switch (op)
        {
            case Token.LT: return a<b;
            case Token.EQ: return a==b;
            case Token.NEQ: return a!=b;
            case Token.LET: return a<=b;
            case Token.GT: return a>b;
            case Token.GET: return a>=b;
            default: throw new UnexpectedException(getNodeLine(iterator), "运算符错误！");
        }
    }

    private Var compute(Var a, Var b, int op, ListIterator<TreeNode> iterator) throws UnexpectedException {
        Var v = new Var();
        if(a.isArrayOrNot()||b.isArrayOrNot())
        {
            throw new UnexpectedException(getNodeLine(iterator), "数组不能参与运算！");
        }
        if(op==Token.PLUS||op==Token.MINUS||op==Token.MUL||op==Token.DIV)
        {
            if(a.getType().equals("int")&&b.getType().equals("int"))
            {
                int m = Integer.parseInt(a.getValue().toString());
                int n = Integer.parseInt(b.getValue().toString());
                v.setType("int");
                v.setValue(operateInt(m,n,op,iterator));
            }else if(a.getType().equals("char")&&b.getType().equals("int"))
            {
                char t = a.getValue().toString().charAt(0);
                int m = (int) t;
                int n = Integer.parseInt(b.getValue().toString());
                v.setType("int");
                v.setValue(operateInt(m,n,op,iterator));
            }else if(a.getType().equals("char")&&b.getType().equals("char"))
            {
                char t = a.getValue().toString().charAt(0);
                int m = (int) t;
                char p = b.getValue().toString().charAt(0);
                int n = (int) p;
                v.setType("int");
                v.setValue(operateInt(m,n,op,iterator));
            }else if(a.getType().equals("int")&&b.getType().equals("char"))
            {
                int m = Integer.parseInt(a.getValue().toString());
                char p = b.getValue().toString().charAt(0);
                int n = (int) p;
                v.setType("int");
                v.setValue(operateInt(m,n,op,iterator));
            }else if(a.getType().equals("real")&&b.getType().equals("int"))
            {
                double m = Double.valueOf(a.getValue().toString());
                int n = Integer.parseInt(b.getValue().toString());
                v.setType("real");
                v.setValue(operateReal(m,n,op,iterator));
            }else if(a.getType().equals("real")&&b.getType().equals("real"))
            {
                double m = Double.valueOf(a.getValue().toString());
                double n = Double.valueOf(b.getValue().toString());
                v.setType("real");
                v.setValue(operateReal(m,n,op,iterator));
            }else if(a.getType().equals("real")&&b.getType().equals("char"))
            {
                double m = Double.valueOf(a.getValue().toString());
                char p = b.getValue().toString().charAt(0);
                int n = (int) p;
                v.setType("real");
                v.setValue(operateReal(m,n,op,iterator));
            }else if(a.getType().equals("int")&&b.getType().equals("real"))
            {
                int m = Integer.parseInt(a.getValue().toString());
                double n = Double.valueOf(b.getValue().toString());
                v.setType("real");
                v.setValue(operateReal(m,n,op,iterator));
            }else if(a.getType().equals("char")&&b.getType().equals("real"))
            {
                char t = a.getValue().toString().charAt(0);
                int m = (int) t;
                double n = Double.valueOf(b.getValue().toString());
                v.setType("real");
                v.setValue(operateReal(m,n,op,iterator));
            }
        }else if(op==Token.LT||op==Token.EQ||op==Token.NEQ||op==Token.LET||op==Token.GT||op==Token.GET)
        {
            if(a.getType().equals("int")&&b.getType().equals("int"))
            {
                int m = Integer.parseInt(a.getValue().toString());
                int n = Integer.parseInt(b.getValue().toString());
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("char")&&b.getType().equals("int"))
            {
                char t = a.getValue().toString().charAt(0);
                int m = (int) t;
                int n = Integer.parseInt(b.getValue().toString());
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("char")&&b.getType().equals("char"))
            {
                char t = a.getValue().toString().charAt(0);
                int m = (int) t;
                char p = b.getValue().toString().charAt(0);
                int n = (int) p;
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("int")&&b.getType().equals("char"))
            {
                int m = Integer.parseInt(a.getValue().toString());
                char p = b.getValue().toString().charAt(0);
                int n = (int) p;
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("real")&&b.getType().equals("int"))
            {
                double m = Double.valueOf(a.getValue().toString());
                int n = Integer.parseInt(b.getValue().toString());
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("real")&&b.getType().equals("real"))
            {
                double m = Double.valueOf(a.getValue().toString());
                double n = Double.valueOf(b.getValue().toString());
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("real")&&b.getType().equals("char"))
            {
                double m = Double.valueOf(a.getValue().toString());
                char p = b.getValue().toString().charAt(0);
                int n = (int) p;
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("int")&&b.getType().equals("real"))
            {
                int m = Integer.parseInt(a.getValue().toString());
                double n = Double.valueOf(b.getValue().toString());
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }else if(a.getType().equals("char")&&b.getType().equals("real"))
            {
                char t = a.getValue().toString().charAt(0);
                int m = (int) t;
                double n = Double.valueOf(b.getValue().toString());
                v.setType("bool");
                v.setValue(operateBool(m,n,op,iterator));
            }
        }
        return v;
    }

    private Var analysisAddExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var n = analysisTermExp(iterator.next().getList(),vars);
        if(getNextNodeType(iterator)==TreeNode.ADD_OP)
        {
            while (getNextNodeType(iterator)==TreeNode.ADD_OP)
            {
                ListIterator<TreeNode> t = iterator.next().getList().listIterator();
                Var var = analysisTermExp(iterator.next().getList(),vars);
                n = compute(n,var,t.next().getType(),iterator);
            }
        }
        return n;
    }

    private Var analysisTermExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var n = analysisFactorExp(iterator.next().getList(),vars);
        if(getNextNodeType(iterator)==TreeNode.MUL_OP)
        {
            while (getNextNodeType(iterator)==TreeNode.MUL_OP)
            {
                ListIterator<TreeNode> t = iterator.next().getList().listIterator();
                Var var = analysisFactorExp(iterator.next().getList(),vars);
                n = compute(n,var,t.next().getType(),iterator);
            }
        }
        return n;
    }

    private Var analysisFactorExp(List<TreeNode> list,List<Var> vars) throws UnexpectedException {
        ListIterator<TreeNode> iterator = list.listIterator();
        Var v = new Var();
        if(getNextNodeType(iterator)==Token.LPARENT)
        {
            consumeNextNode(Token.LPARENT,iterator);
            v = analysisExp(iterator.next().getList(),vars);
            consumeNextNode(Token.RPARENT,iterator);
        }else if(getNextNodeType(iterator)==TreeNode.LITREAL_INT)
        {
            v.setValue(iterator.next().getValue());
            v.setType("int");
        }else if(getNextNodeType(iterator)==TreeNode.LITREAL_REAL)
        {
            v.setValue(iterator.next().getValue());
            v.setType("real");
        }else if(getNextNodeType(iterator)==TreeNode.CHAR)
        {
            v.setValue(iterator.next().getValue());
            v.setType("char");
        }else if(getNextNodeType(iterator)==TreeNode.VAR)
        {
            v = analysisVar(iterator.next().getList(),vars);
        }
        return v;

    }

    private Var analysisVar(List<TreeNode> list,List<Var> vars) throws UnexpectedException {
        ListIterator<TreeNode> iterator = list.listIterator();
        String name = iterator.next().getValue();
        Var v = new Var();
        for (Var var:vars
        ) {
            if(var.getName().equals(name))
            {
                v = var;
            }
        }
        if(v.getName()==null)
        {
            throw new UnexpectedException(getNodeLine(iterator), "变量未声明！");
        }
        if(v.isArrayOrNot())
        {
            Var p = new Var();
            if(getNextNodeType(iterator)==Token.LBRACKET)
            {
                int a;
                int order;
                consumeNextNode(Token.LBRACKET, iterator);
                Var t = analysisExp(iterator.next().getList(),vars);
                if(t.getType().equals("int"))
                {
                    a = Integer.parseInt(t.getValue().toString());
                }else {
                    throw new UnexpectedException(getNodeLine(iterator), "必须为整数！");
                }
                order = a;
                consumeNextNode(Token.RBRACKET, iterator);
                if(getNextNodeType(iterator)==Token.LBRACKET)
                {
                    int e = v.getMax().size();
                    int x = 1;
                    int s = 1;
                    for(int h = s;h < e;h++)
                    {
                        x = x * v.getMax().get(h);
                    }
                    order = a * x;
                    while (getNextNodeType(iterator)==Token.LBRACKET)
                    {
                        consumeNextNode(Token.LBRACKET, iterator);
                        Var w = analysisExp(iterator.next().getList(),vars);
                        if(w.getType().equals("int"))
                        {
                            a = Integer.parseInt(w.getValue().toString());
                        }
                        s++;
                        int u = 1;
                        for(int o = s ;o < e;o++)
                        {
                            u = u * v.getMax().get(o);
                        }
                        order = order + a * u;
                        consumeNextNode(Token.RBRACKET, iterator);
                    }
                }
                if(v.getType().equals("intArray"))
                {
                    p.setType("int");
                }else if(v.getType().equals("realArray"))
                {
                    p.setType("real");
                }else if(v.getType().equals("charArray"))
                {
                    p.setType("char");
                }
                p.setArrayOrNot(false);
                p.setName(name);
                p.setOrder(order);
                if(v.getArray().size()!=0)
                {
                    p.setValue(v.getArray().get(order));
                }
                return p;
            }
        }
        if(v.getValue()!=null)
        {
            if(v.getType().equals("int"))
            {
                int t = Integer.parseInt(v.getValue().toString());
                v.setValue(t);
            }else if(v.getType().equals("real"))
            {
                double t = Double.valueOf(v.getValue().toString());
                v.setValue(t);
            }else if(v.getType().equals("char"))
            {
                char t = v.getValue().toString().charAt(0);
                v.setValue(t);
            }
        }
        return v;
    }
}
