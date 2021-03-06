package de.hdm.schemeinterpreter.symbols;

import de.hdm.schemeinterpreter.Validator;

import java.util.Arrays;

public class NumericNotEqual implements Symbol {
    @Override
    public String getSymbol() {
        return "!=";
    }

    @Override
    public String getParamDefinition() {
        return Validator.enclosed("(?:" + Validator.Type.floatingPoint + " ){2,}");
    }

    @Override
    public String eval(String... validatedParams) {
        double[] numbers = Arrays.stream(validatedParams).mapToDouble(Double::valueOf).toArray();

        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i - 1] == numbers[i]) {
                return "#f";
            }
        }

        return "#t";
    }
}
