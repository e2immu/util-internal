package org.e2immu.util.internal.util;

public class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    public static void indent(StringBuilder sb, int num) {
        sb.append(" ".repeat(Math.max(0, num)));
    }

    public static String quote(String s) {
        return "\"" + s.replace("\"", "\\\"") + "\"";
    }

    public static String capitalize(String name) {
        assert name != null && !name.isEmpty();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
