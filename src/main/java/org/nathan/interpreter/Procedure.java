package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

interface Procedure extends Lambda {

    static Procedure newProcedure(Object params, @NotNull Object exp, Environment env) {
        return new Procedure() {
            @Override
            public Object apply(List<Object> args) {
                return JispyInterpreter.eval(exp, new Environment(params, args, env));
            }

            @Override
            public @NotNull Object expression() {
                return exp;
            }

            @Override
            public Environment environment() {
                return env;
            }

            @Override
            public @NotNull Object parameters() {
                return params;
            }
        };
    }

    @NotNull Object expression();

    @NotNull Object parameters();

    Environment environment();
}
