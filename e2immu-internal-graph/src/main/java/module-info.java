module org.e2immu.util.internal.graph {
    requires org.slf4j;
    requires org.jgrapht.core;
    requires org.jgrapht.io;

    exports org.e2immu.util.internal.graph;
    exports org.e2immu.util.internal.graph.op;
    exports org.e2immu.util.internal.graph.analyser;
    exports org.e2immu.util.internal.graph.util;
}