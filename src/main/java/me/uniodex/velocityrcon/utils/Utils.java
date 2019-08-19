package me.uniodex.velocityrcon.utils;

import java.util.regex.Pattern;

public class Utils {
    public static final char COLOR_CHAR = '\u00A7';
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
}
