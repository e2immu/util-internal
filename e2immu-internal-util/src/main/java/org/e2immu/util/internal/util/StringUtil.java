package org.e2immu.util.internal.util;

public class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    public static void indent(StringBuilder sb, int num) {
        sb.append(" ".repeat(Math.max(0, num)));
    }

    public static String quote(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 2);
        sb.append('"');
        for (char c : s.toCharArray()) {
            if ('\n' == c) {
                sb.append("\\n");
            } else if ('\r' == c) {
                sb.append("\\r");
            } else if ('\t' == c) {
                sb.append("\\t");
            } else if ('\b' == c) {
                sb.append("\\b");
            } else if ('"' == c || '\\' == c) {
                sb.append("\\").append(c);
            } else {
                sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static String capitalize(String name) {
        assert name != null && !name.isEmpty();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
