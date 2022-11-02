package me.uniodex.velocityrcon.utils;

import java.util.regex.Pattern;

public class Utils {
    public static final char COLOR_CHAR = '\u00A7';
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + COLOR_CHAR + "[0-9A-FK-OR]");
    public static final Pattern STRIP_MC_COLOR_PATTERN = Pattern.compile("§[0-8abcdefklmnor]");

    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String stripMcColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_MC_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
