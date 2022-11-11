package org.tartea.plugin.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.rightPad;

public class BeautifyConsole {
    private int width;
    private static String SEPARATOR = "separator";
    List<String> result = new ArrayList<>();


    public synchronized BeautifyConsole addParams(List<String> params) {
        result.add(SEPARATOR);
        params.forEach(param -> {
            final String replace = param.replace("\r", " ").replace("\n", "");
            width = Math.max(width, replace.getBytes().length);
            result.add(replace);
        });
        result.add(SEPARATOR);
        return this;
    }

    public synchronized void print() {
        System.out.print("\033[1;94m");
        String outerBorder = rightPad("", width + 6, "_");
        String empty = "  ";
        String innerBorder = rightPad(empty, width + 4, "_");
        System.out.println(outerBorder);
        for (String s : result) {
            if (Objects.equals(s, SEPARATOR)) {
                System.out.printf("%s", innerBorder);
                System.out.println();
            } else {
                System.out.println(empty + s);
            }
        }
        System.out.println(outerBorder);
    }
}