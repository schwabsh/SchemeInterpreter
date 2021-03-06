package de.hdm.schemeinterpreter.symbols;

import de.hdm.schemeinterpreter.SymbolFactory;
import de.hdm.schemeinterpreter.SymbolManager;
import de.hdm.schemeinterpreter.ValidationResult;
import de.hdm.schemeinterpreter.Validator;
import de.hdm.schemeinterpreter.utils.ParamUtils;

public class Construct implements Symbol {
    @Override
    public String getSymbol() {
        return "cons";
    }

    @Override
    public String getParamDefinition() {
        return "(?:" + Validator.Type.any + " ){2}";
    }


    @Override
    public ValidationResult<String[]> validateParams(String[] params) {
        final ValidationResult<String[]> validationResult = ParamUtils.validateParams(getParamDefinition(), params);
        validationResult.validationSubject = ParamUtils.resolveAllParams(params);

        return validationResult;
    }

    @Override
    public String eval(String... validatedParams) {
        var car = validatedParams[0];
        var cdr = validatedParams[1];
        String finalCons = "";

        if (car.startsWith("(")) {
            finalCons += "(" + SymbolManager.getInstance().resolveVar(car);
        } else {
            finalCons += "(" + car;
        }

        if (cdr.startsWith("(")) {
            var resolvedVar = SymbolManager.getInstance().resolveVar(cdr);
            resolvedVar = resolvedVar.substring(1, resolvedVar.length() - 1);

            finalCons += " " + resolvedVar + ")";
        } else {
            finalCons += " . " + cdr + ")";
        }

        final String uuid = SymbolManager.generateVarId();
        SymbolManager.getInstance().addSymbol(SymbolFactory.createVariable(uuid, finalCons));

        return uuid;
    }
}
