package com.company;
import java.util.*;
import java.io.*;

public class BackwardChaining {
//declare variables:
    private static String tell;
    private static String ask;
    private static ArrayList<String> agenda;
    private static ArrayList<String> facts;
    private static ArrayList<String> clauses;
    private static ArrayList<String> entailed;

    public BackwardChaining (String ASK, String TELL){

        agenda = new ArrayList<String>();
        clauses = new ArrayList<String>();
        entailed = new ArrayList<String>();
        facts = new ArrayList<String>();
        tell = TELL;
        ask = ASK;
        init(tell);
    }

    public static void init(String tell) {
        agenda.add(ask);
        String [] sentences = tell.split(";");
        for (int i=0; i< sentences.length; i++){
            if (!sentences[i].contains("=>")) {
                facts.add(sentences[i].replaceAll("\\s",""));
                //System.out.println(facts);
            }
            else {
                clauses.add(sentences[i]);
                //System.out.println(clauses);
            }
        }
    }

    public String execute() {
        String result = "";
        if (entails()) {
            result = "YES: ";
            // loop through all entailed symbols in reverse
            for (int i = entailed.size() - 1; i >= 0; i--) {
                if (i == 0)
                    result += entailed.get(i);
                else
                    // no comma at the end
                    result += entailed.get(i) + ", ";

            }
        }
        // no
        else {
            result = "NO";
        }
        return result;
    }


    public boolean entails() {
        while (!agenda.isEmpty()){
                String q = agenda.remove(agenda.size()-1);
                entailed.add(q.trim());
//                System.out.println("The query:" + q);
                //
  //              System.out.println(facts);
                if (!facts.contains(q)){
                    ArrayList<String> p = new ArrayList<String>();
                    for (int i=0; i < clauses.size(); i++){
                        if (concContains(clauses.get(i), q)) {
                            ArrayList<String> temp = getPremises(clauses.get(i));
    //                        System.out.println("Premises :" + temp);
                            for (int j=0; j < temp.size(); j++)
                            {
                                p.add(temp.get(j));
                            }
                        }
                    }
                    //
                    if (p.size() == 0)
                        return false;
                    else
                        for (int i=0; i<p.size(); i++) {
                            if (!entailed.contains(p.get(i))) {
      //                          System.out.println("If not entailed contains premise: " + p.get(i));
                                agenda.add(p.get(i));
                            }
                        }
                }
        }
        //
        return true;
    }

    public static ArrayList<String> getPremises(String clause) {
        String premise =  clause.split("=>")[0];
        ArrayList<String> tmp = new ArrayList<String>();
        String[] conjuncts = premise.split("&");
        for (int i=0; i<conjuncts.length; i++) {
            if (!agenda.contains(conjuncts[i])) {
                tmp.add(conjuncts[i]);
            }
        }
        return tmp;
    }

    public boolean concContains(String clause, String conc) {
        String conclusion = clause.split("=>")[1];
        //System.out.println("The clause is:" + conclusion + " The testing is: " + conc);
        if (conc.contains(conclusion))
        {
            //System.out.println("True");
            return true;
        }
        else
        {
            return false;
        }
    }
}


