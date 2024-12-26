package org.nandayo;

import net.md_5.bungee.api.ChatColor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtil {
    private static final Pattern PATTERN = Pattern.compile(
            "<(#[a-f0-9]{6}|aqua|black|blue|dark_(aqua|blue|gray|green|purple|red)|gray|gold|green|light_purple|red|white|yellow)>",
            Pattern.CASE_INSENSITIVE
    );

    private static String color(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        final Matcher matcher = PATTERN.matcher(text);

        while (matcher.find()) {
            try {
                final ChatColor chatColor = ChatColor.of(matcher.group(1));

                if (chatColor != null) {
                    text = text.replace(matcher.group(), chatColor.toString());
                }
            } catch (IllegalArgumentException ignored) { }
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static Map<String, String> colorMap = null;
    public static void setColorMap(Map<String, String> map) {
        colorMap = map;
    }

    public static String parse(String s) {
        if(colorMap == null) return color(s);
        for(Map.Entry<String, String> entry : colorMap.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }
        return color(s);
    }
}
