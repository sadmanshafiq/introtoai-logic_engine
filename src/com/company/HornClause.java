package com.company;

import java.util.HashSet;

public class HornClause {
    private String symBefore;
    private String symAfter;
    private HashSet<String> befSymbols;
    private HashSet<String> aftSymbols;

    public String getSymBefore() {
        return symBefore;
    }

    public String getSymAfter() {
        return symAfter;
    }

    public HashSet<String> getBefSymbols() {
        return befSymbols;
    }

    public HashSet<String> getAftSymbols() {
        return aftSymbols;
    }

    public HornClause (String ant, String con) {
        symAfter = con;
        symBefore = ant;
        befSymbols = new HashSet<String>();
        aftSymbols = new HashSet<String>();

        initializeSymbols(symBefore, befSymbols);
        initializeSymbols(symAfter, aftSymbols);
    }

    private void initializeSymbols(String symbol, HashSet<String> SymbolList) {
        if (symbol != null) {
            if (symbol.contains("&")) {
                String[] splitSymbols = symbol.split("&");
                for (String sym : splitSymbols) {
                    SymbolList.add(sym);
                }
            }
            else {SymbolList.add(symbol);}
        }
    }

    public String toString() {
        if (symAfter == null) {
            return getSymBefore();
        } else {
            return getSymBefore() + "=>" + getSymAfter();
        }
    }


}
