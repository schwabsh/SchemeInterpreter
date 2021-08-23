package de.hdm.schemeinterpreter;

import de.hdm.schemeinterpreter.symbols.Symbol;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SymbolManager {

    protected static SymbolManager instance;
    protected List<Symbol> symbols;

    public static SymbolManager getInstance() {
        if (null == instance) {
            instance = new SymbolManager();
        }

        return instance;
    }

    public SymbolManager() {
        this.symbols = new ArrayList<>();
    }

    public Optional<Symbol> getSymbol(String symbol) {
        return symbols.stream().filter(f -> f.getSymbol().equals(symbol)).findFirst();
    }

    public boolean replaceSymbol(String symbol, String newValue){
        final Optional<Symbol> oldSymbol = this.getSymbol(symbol);
        if (oldSymbol.isPresent()){
            this.symbols.remove(oldSymbol.get());
            this.addSymbol(this.createSymbol(symbol, newValue));
            return true;
        }
        return false;
    }

    public Symbol createSymbol(String symbol, String value){
        return new Symbol() {
            @Override
            public String getSymbol() {
                return symbol;
            }

            @Override
            public ValidationResult<String[]> validateParams(String[] params) {
                final ValidationResult.Status status = params.length == 0
                        ? ValidationResult.Status.VALID
                        : ValidationResult.Status.INVALID;

                return new ValidationResult<>(params, status, "");
            }

            @Override
            public String getParamDefinition() {
                return null;
            }

            @Override
            public String eval(String... validatedParams) {
                return value;
            }
        };
    }

    public void addSymbol(Symbol symbol) {
        if(this.symbols.stream().anyMatch(e -> e.getSymbol().equals(symbol.getSymbol()))){
            // TODO: Maybe handle more gracefully to keep the program running.
            throw new RuntimeException("Variable '" + symbol.getSymbol() + "' already exists.");
        }

        this.symbols.add(symbol);
    }

    public void addSymbols(List<? extends Symbol> symbols) {
        symbols.forEach(this::addSymbol);
    }

    public String resolveVar(String param) {
        if (!Validator.isSchemeVar(param)) {
            return param;
        }

        Optional<Symbol> symbol = getSymbol(param);

        if (symbol.isPresent()) {
            return symbol.get().eval(param);
        } else {
            // TODO: Maybe handle more gracefully to keep the program running.
            throw new NullPointerException("Variable '" + param + "' is undefined.");
        }
    }
}
