package com.company;
import java.util.*;
import java.io.*;

public class ForwardChaining {
public static ArrayList<String> agenda;
public static ArrayList<String> entailed;
public static ArrayList<String> clauses;
public static ArrayList<Integer> count;
public static ArrayList<String> facts;

public static String tell;
public static String ask;


    public ForwardChaining(String ASK, String TELL){
        entailed  = new ArrayList<String>();
        facts  = new ArrayList<String>();
        count  = new ArrayList<Integer>();
        agenda  = new ArrayList<String>();
        clauses  = new ArrayList<String>();
        tell = TELL;
        ask = ASK;
        init(tell);
    }

    public String execute(){
        String output = "";
        if (entails()){
            output = "YES: ";
            for (int i=0;i<entailed.size();i++){
                output += entailed.get(i)+", ";
            }
            output += ask;
        }
        else{
            output = "NO";
        }
        return output;
    }


    public boolean entails(){
        while(!agenda.isEmpty()){
            String p = agenda.remove(0);
            entailed.add(p);
          
            for (int i=0;i<clauses.size();i++){

                if (containsPrem(clauses.get(i),p)){
                    Integer j = count.get(i);
                    count.set(i,--j);
                  
                    if (count.get(i)==0){
                        String head = clauses.get(i).split("=>")[1];
                        if (head.equals(ask))
                            return true;
                        agenda.add(head);

                    }
                }
            }
        }
        return false;
    }


    public static void init(String tell){
        String[] sentences = tell.split(";");
        for (int i=0;i<sentences.length;i++) {

            if (!sentences[i].contains("=>")){
                agenda.add(sentences[i].replaceAll("\\s", ""));
        }
            else{
                // add sentences
                clauses.add(sentences[i]);
                count.add(sentences[i].split("&").length);
            }
        }
    }



    public static boolean containsPrem(String c, String p){
        String clausePremise = c.split("=>")[0];
        String[] conjuncts = clausePremise.split("&");
        if (conjuncts.length==1)
            return clausePremise.contains(p);
        else
            return Arrays.asList(conjuncts).contains(p);
    }
}