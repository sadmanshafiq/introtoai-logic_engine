package com.company;

import java.util.*;

public class LogicalExpression {
    private String symbol = null;
    private String connection = null;
    private Vector<LogicalExpression> children = null;

    private final String TRUE = "T";
    private final String FALSE = "F";

    public static Stack<String> symbolStack = new Stack<String>();
    private static boolean finalResult;

    public LogicalExpression()
    {
        this.children = new Vector<LogicalExpression>();
    }

    public LogicalExpression(LogicalExpression expression ) {
        //Convert an expression into Symbols and connections and added children
        if (expression.getSymbol() == null) {
            this.symbol = expression.getSymbol();
        } else {
            this.connection = expression.getConnection();

            for (Enumeration e = expression.getChildren().elements(); e.hasMoreElements();) {
                LogicalExpression nextExpression = (LogicalExpression) e.nextElement();

                this.children.add(nextExpression);
            }
        }
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        boolean valid = true;
        symbol.trim();

        if (this.symbol != null)
        {
            System.out.println("Swapping existing symbol: " + this.symbol + " with: " + symbol);
        }
        else if (valid) {
            this.symbol = symbol;
        }
    }

    public String getConnection() {
        return this.connection;
    }

    public String setConnection(String inputconnection) {
        String connect = "";

        inputconnection.trim();

        if (inputconnection.startsWith("(")) {
            inputconnection = inputconnection.substring(inputconnection.indexOf('('), inputconnection.length());

            inputconnection.trim();
        }
        else {
            connect = inputconnection;
            inputconnection =  "";
        }
        if (connect.equalsIgnoreCase("if") || connect.equalsIgnoreCase("iff") || connect.equalsIgnoreCase("and")
                || connect.equalsIgnoreCase("or") || connect.equalsIgnoreCase("xor") || connect.equalsIgnoreCase("not")) {
            // ok, first word in the string is a valid connection

            // set the connective
            this.connection = connect;

            return inputconnection;

        } else {
            System.out.println("unexpected character!!! : invalid connective!! - setConnective():-" + inputconnection);
            this.exitFunction(0);
        }

        // invalid connective - no clue who it would get here
        System.out.println(" invalid connective! : setConnective:-" + inputconnection);
        return inputconnection;
    }

    public Vector<LogicalExpression> getChildren() {
        return children;
    }

    public void setChildren(Vector<LogicalExpression> children) {
        this.children = children;
    }

    public LogicalExpression getNextChild() {
        return this.children.lastElement();
    }

    public void setChild (LogicalExpression child) {
        this.children.add(child);
    }

    public boolean solveExpression (HashMap<String, Boolean> model) {
        if (this.getSymbol() != null) {
            symbolStack.push(this.getSymbol());
        }
        else {
            LogicalExpression nextExpression;

            symbolStack.push(this.getConnection());

            for (Enumeration e = this.children.elements(); e.hasMoreElements();) {
                nextExpression = (LogicalExpression) e.nextElement();

                nextExpression.solveExpression(model);
            }

            finalResult = popSymbolsEvaluateResult(model);
        }
        return finalResult;
    }

    private boolean popSymbolsEvaluateResult(HashMap<String, Boolean> model) {
        ArrayList<String> iSymbol = new ArrayList<String>();
        String symbol, connective;
        boolean result = false;

        do {
            symbol = symbolStack.pop();
            iSymbol.add(symbol);
        }
        while (!isConnective(symbol));
        iSymbol.remove (symbol);
        connective = symbol;

        if (connective.equalsIgnoreCase("or")) { // can have more than two unique symbols
            result = false;
            while (!iSymbol.isEmpty() && !result) {
                result = result || getValue(iSymbol.remove(0), model);
            }
        } else if (connective.equalsIgnoreCase("and")) { // can have more than two unique symbols
            result = true;
            while (!iSymbol.isEmpty() && result) {
                result = result && getValue(iSymbol.remove(0), model);
            }
        } else if (connective.equalsIgnoreCase("not")) {
            result = true;
            result = !getValue(iSymbol.remove(0), model);
        } else if (connective.equalsIgnoreCase("xor")) { // result = a'b + ab'
            // can have more than two unique symbols
            result = false;
            int no_of_true_symbol = 0;
            while (!iSymbol.isEmpty()) {
                if (getValue(iSymbol.remove(0), model)) {
                    no_of_true_symbol++;
                }
            }
            if (no_of_true_symbol == 1) {
                result = true;
            }
        } else if (connective.equalsIgnoreCase("if")) { // required exactly two symbols
            result = true;
            if (iSymbol.size() == 2) {
                if (getValue(iSymbol.get(1), model) && !getValue(iSymbol.get(0), model)) {
                    result = false;
                }
            }
        } else if (connective.equalsIgnoreCase("iff")) { // result = a'b' + ab
            // required exactly two symbols
            result = false;
            if (iSymbol.size() == 2) {
                boolean symbol1 = getValue(iSymbol.get(1), model);
                boolean symbol2 = getValue(iSymbol.get(0), model);
                if ((!symbol1 && !symbol2) || (symbol1 && symbol2)) {
                    result = true;
                }
            }
        } else {
            System.out.println("Oops..incorrect connective!!");
        }

        if (result) { // push evaluated result again on top of stack for further use
            symbolStack.push(TRUE);
        } else {
            symbolStack.push(FALSE);
        }

        return result;
    }

    private boolean getValue(String symbol, HashMap<String, Boolean> model) {
        if (symbol.equalsIgnoreCase(TRUE)) {
            return true;
        } else if (symbol.equalsIgnoreCase(FALSE)) {
            return false;
        } else if (model.get(symbol) == null) {
            return false; //TruthTable.getValueFromArray(symbol); //this is very important
        } else {
            return model.get(symbol);
        }
    }

    public static void clearStack() {
        if (symbolStack != null) {
            symbolStack.clear();
        }
    }

    private boolean isConnective(String symbol) {
        return (symbol.equalsIgnoreCase("if") || symbol.equalsIgnoreCase("iff") || symbol.equalsIgnoreCase("and")
                || symbol.equalsIgnoreCase("or") || symbol.equalsIgnoreCase("xor") || symbol.equalsIgnoreCase("not"));
    }

    private static void exitFunction(int value) {
        System.out.println("exiting from LogicalExpression");
        System.exit(value);
    }
}
