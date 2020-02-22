package com.company;

public class UnexpectedException extends Exception{
    private Integer lineNum = null;

    private Integer colNum = null;

    private String desc = null;

    private Throwable cause = null;

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(Integer lineNum, Integer colNum, String message) {

        this.colNum = colNum;
        this.lineNum = lineNum;
        this.desc = message;
    }

    public UnexpectedException(Integer lineNum, String message) {

        this.lineNum = lineNum;
        this.desc = message;
    }

    public String getMessage() {
        return "[ line:" + lineNum + " ]" + desc + (cause == null ? "" : cause.toString());
    }

    public String getLocalMessage() {
        return getMessage();
    }

    public String toString() {
        return getMessage();
    }

}
