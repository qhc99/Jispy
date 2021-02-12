package org.nathan.interpreter;

import java.util.List;

public interface Procedure extends Lambda{

    static Procedure newProcedure(Iterable<Object> params, Object exp, Env env){
        return new Procedure() {
            @Override
            public Object apply(List<Object> args) {
                return Jispy.eval(exp, new Env(params,args,env));
            }

            @Override
            public Object expression(){
                return exp;
            }

            @Override
            public Env environment(){
                return env;
            }

            @Override
            public Iterable<Object> parameters(){
                return params;
            }
        };
    }

    Object expression();

    Iterable<Object> parameters();

    Env environment();
}
