package com.company;

import java.util.*;

public class TruthTable {
    public static HashMap<String, Boolean> model;
    public HashSet<String> SymbolsUsed;
    public HashSet<HornClause> KnowledgeBase;
    public LinkedList<String> Agenda;
    private Integer validModels;
    public static ArrayList<String> symbols = new ArrayList<String>();
    public String tell;
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    private LogicalExpression KnowBase = new LogicalExpression();
    private String ask;

    public TruthTable(String ASK, String TELL){

        tell = TELL;
        ask = ASK;
        init(tell);

    }

    public void init(String TELL) {
        this.validModels = 0;
        KnowledgeBase = new HashSet<HornClause>();
        HashSet<String> Symbols = new HashSet<String>();
        tell = TELL.replaceAll("\\s", "");
        String[] Clauses = tell.split(";");
        for (String clause: Clauses) {
            if (!clause.contains("=>")) {
                KnowledgeBase.add(new HornClause(clause.trim(), null));
            }
            else {
                String[] SplitClause = clause.split("=>");
                KnowledgeBase.add(new HornClause(SplitClause[0].trim(), SplitClause[1].trim()));
            }
        }
        for (HornClause clause: KnowledgeBase) {
            AddSymbol(clause.getSymBefore(), Symbols);
            AddSymbol(clause.getSymAfter(), Symbols);
        }
        SymbolsUsed = Symbols;
        //Symbols = InitSol(TELL, Symbols);
        //Need to split the tell file into a logical expression
        //Need to fix ask into logical expression alpha
    }

    private void AddSymbol(String sym, HashSet<String> Symbols) {
        if (sym != null) {
            if (sym.contains("&")) {
                String[] splitSymbols = sym.split("&");
                symbols.addAll(Arrays.asList(splitSymbols));
            } else {
                symbols.add(sym);
            }
        }
    }

    public String execute() {
        String result = "";
        if (TTEntails())
        {
            result = "YES: ";
            result.concat(validModels.toString());
        }
        else {
            result = "NO";
        }
        return result;
    }

    private  boolean TTEntails() {
        HashSet<String> symbolsList = new HashSet<String>();
        symbolsList = SymbolsUsed;

        return CheckAll(ask, symbolsList, new HashMap<String, Boolean>());
    }

    private boolean CheckAll(String ASK, HashSet<String> symbolsList, HashMap<String, Boolean> model) {
        if (symbolsList.isEmpty()) {
            if (checkKB(model)) {
                if (model.get(ASK)) {
                    validModels++;
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            HashSet<String> rest = new HashSet<String>(symbolsList);
            String p = symbolsList.iterator().next();

            rest.remove(p);

            return CheckAll(ASK, rest, Extend(p, true, model)) && CheckAll(ASK, rest, Extend(p, false, model));
        }
    }

    private boolean checkKB(HashMap<String, Boolean> model) {
        for (HornClause clause: KnowledgeBase)
        {
            if (!CheckTrue(clause, model)) {return false;}
        }
        return true;
    }

    private boolean CheckTrue(HornClause clause, HashMap<String,Boolean>model) {
        if (clause.getSymAfter() == null)
        {
            return model.get(clause.getSymBefore());
        }
        if (clause.getBefSymbols().size() == 1) {
            if (model.get(clause.getSymBefore())) {
                return model.get(clause.getSymAfter());
            }
        }
        else if (clause.getBefSymbols().size() == 2) {
            if (model.get(clause.getBefSymbols().iterator().next()) &&
                model.get(clause.getBefSymbols().iterator().next())) {
                return model.get(clause.getSymAfter());
            }
        }
        return true;
    }
        /*
           PREVIOUS EXTEND METHOD
           private static HashMap<String, Boolean> EXTENDS (String P,boolean value, HashMap<String, Boolean > model){
            model.put(P, value);
            return model;
        }
        */
    private HashMap<String, Boolean> Extend(String p, Boolean b, HashMap<String, Boolean> model) {
        model.put(p,b);
        return model;
    }

        /*
]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]
PREVIOUS CODE EXTENDS AND OTHER METHODS THAT WERE TRIALLED
[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[
         if (PLTrue(KB, model, false)) { //ADD FALSE
                return PLTrue(alpha, model, true); //COMPUTINGNECATION STATEMENT ADD
            } else {return true;}
        }
        else {
            String P  = symbolsList.remove(0);
            ArrayList<String> rest = new ArrayList<String>(symbolsList);

            return CheckAll(KB, alpha, rest, EXTENDS(P, true, model)) &&
                    CheckAll(KB, alpha, rest, EXTENDS(P, false, model));
            }
        }
    private static boolean PLTrue(LogicalExpression sentence, HashMap<String, Boolean> model, boolean CompNegStatement) {
        boolean result = sentence.solveExpression(model);

        LogicalExpression.clearStack();

        if (CompNegStatement ) {
            return !result;
        }
        else { return true; }
    }

    public static LogicalExpression readExpression(String input_string) {
        LogicalExpression result = new LogicalExpression();

        // trim the whitespace off
        input_string = input_string.trim();

        if (input_string.startsWith("(")) {
            // its a subexpression

            String symbolString = "";

            // remove the '(' from the input string
            symbolString = input_string.substring(1);

            if (!symbolString.endsWith(")")) {
                // missing the closing paren - invalid expression
                System.out.println("missing ')' !!! - invalid expression! - readExpression():-" + symbolString);

            } else {
                // remove the last ')'
                // it should be at the end
                symbolString = symbolString.substring(0, (symbolString.length() - 1));
                symbolString.trim();

                // read the connective into the result LogicalExpression object
                symbolString = result.setConnection(symbolString);
            }

            // read the subexpressions into a vector and call setSubExpressions( Vector );
            //THIS NEEDS TO BE UN COMMENTED result.setChildren(read_subexpressions(symbolString));

        } else {
            // the next symbol must be a unique symbol
            // if the unique symbol is not valid, the setUniqueSymbol will tell us.
            result.setSymbol(input_string);
        }

        return result;
    }

    public static boolean getValueFromArray(String symbol) {
        // METHODOLOGY FOR CNF

        String symbol_initials = null;
        String[] symbol_literals = new String[3];

        symbol_literals = symbol.split("");
        symbol_initials = symbol_literals[0];
        int pos_x = Integer.parseInt(symbol_literals[1]);
        int pos_y = Integer.parseInt(symbol_literals[2]);

        if (symbol_initials.equals("M")) {
            if (1 == TRUE) {
                return true;
            } else {
                return false;
            }
        } else if (symbol_initials.equals("P")) {
            if (2 == TRUE) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Oops...Incorrect Symbol format!!");
        }
        return false;
    }

}
         */
}