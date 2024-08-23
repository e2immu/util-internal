package org.e2immu.util.internal.util;

import org.e2immu.annotation.NotNull;

public class StringUtil {

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    public static void indent(StringBuilder sb, int num) {
        sb.append(" ".repeat(Math.max(0, num)));
    }

    public static final String AWFULLY_LONG_METHOD = "?? awfully long method";

    /*
        n <= 10  >> 0..9
        n <=100  >> 00..99
        n <=1000 >> 000..999
         */
    @NotNull
    public static String pad(int i, int n) {
        String s = Integer.toString(i);
        if (n <= 10) return s;
        if (n <= 100) {
            if (i < 10) return "0" + s;
            return s;
        }
        if (n <= 1_000) {
            if (i < 10) return "00" + s;
            if (i < 100) return "0" + s;
            return s;
        }
        if (n <= 10_000) {
            if (i < 10) return "000" + s;
            if (i < 100) return "00" + s;
            if (i < 1_000) return "0" + s;
            return s;
        }
        throw new UnsupportedOperationException(AWFULLY_LONG_METHOD);
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
