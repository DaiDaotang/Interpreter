package com.company;

import java.util.ArrayList;
import java.util.List;

public class Var {
    //变量类存储变量的变量名，值，变量类型等等

    //类型
    private String type;
    // 值
    private Object value;
    //变量名
    private String name;

    private int order;

    private int length;

    private List<Object> array = new ArrayList<>();

    private List<Integer> Max = new ArrayList<>();

    private boolean arrayOrNot;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Object> getArray() {
        return array;
    }

    public void setArray(List<Object> array) {
        this.array = array;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isArrayOrNot() {
        return arrayOrNot;
    }

    public void setArrayOrNot(boolean arrayOrNot) {
        this.arrayOrNot = arrayOrNot;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<Integer> getMax() {
        return Max;
    }

    public void setMax(List<Integer> max) {
        Max = max;
    }
}
