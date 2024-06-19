package org.e2immu.util.internal.graph;

import java.util.Iterator;
import java.util.Map;

public interface EdgeIterator<T> {
    Iterator<Map<V<T>, Map<V<T>, Long>>> iterator(G<T> g);
}
