package org.qhc99.interpreter;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
interface Lambda extends Function<List<Object>, Object>{
}
