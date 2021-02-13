package org.nathan.interpreter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

interface Procedure extends Lambda {

    static Procedure newProcedure(@NotNull Iterable<Object> params, @NotNull Object exp, Env env) {
        return new Procedure() {
            @Override
            public Object apply(List<Object> args) {
                return Jispy.eval(exp, new Env(params, args, env));
            }

            @Override
            public @NotNull Object expression() {
                return exp;
            }

            @Override
            public Env environment() {
                return env;
            }

            @Override
            public @NotNull Iterable<Object> parameters() {
                return params;
            }
        };
    }

    @NotNull Object expression();

    @NotNull Iterable<Object> parameters();

    Env environment();
}
