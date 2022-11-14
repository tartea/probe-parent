package org.tartea.plugin.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.rightPad;

public class BeautifyConsole {
    private int width;
    private static String SEPARATOR = "separator";
    List<String> result = new ArrayList<>();


    public void printInfo(List<String> params) {
        result.add(SEPARATOR);
        params.forEach(param -> {
            final String replace = param.replace("\r", " ").replace("\n", "");
            width = Math.max(width, replace.getBytes().length);
            result.add(replace);
        });
        result.add(SEPARATOR);
        print();
    }

    public void print() {
        System.out.print("\033[1;94m");
        String outerBorder = rightPad("", width + 6, "_");
        String empty = "  ";
        System.out.println(outerBorder);
        for (String s : result) {
            if (!Objects.equals(s, SEPARATOR)) {
                System.out.println(empty + s);
            }
        }
        System.out.println(outerBorder);
        System.out.println("\033[0m");
    }
}