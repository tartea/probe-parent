package org.tartea.idea.plugin.logger;

/**
 * 日志信息
 *
 * @Author: jiawenhao
 * @Date: 2022-11-5  18:56
 */
public class ConsoleInfo {

    private StringBuilder logInfo = new StringBuilder();


    //日志信息
    private static String line = "\n\r";


    /**
     * 追加日志
     *
     * @param log
     */
    public synchronized ConsoleInfo appendLog(String log) {
        logInfo.append(log).append(line);
        return this;
    }


    /**
     * 打印日志
     */
    public synchronized void print() {
        System.out.print("\033[1;94m");
        System.out.print(logInfo.toString());
    }
}
