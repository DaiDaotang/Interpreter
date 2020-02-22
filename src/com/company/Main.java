package com.company;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class Main {

    public static void main(String[] args) throws IOException,UnexpectedException
    {
        String pathFrom = "D:\\1.txt";
        BufferedReader in = new BufferedReader(new FileReader(pathFrom));
        String str="";
        int c;
        while((c = in.read())!=-1)
        {
            str=str+(char)c;
        }
        in.close();

        str = str.trim();
        Lexer lexer = new Lexer(str);
        Parser parser = new Parser(lexer);
        Analysis analysis = new Analysis(parser.parseProgram());
        analysis.analysisProgram();
    }
}
