package org.tartea.idea.plugin.monitor;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import net.bytebuddy.implementation.bind.annotation.*;
import org.tartea.idea.plugin.util.ConsoleInfoUtil;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * mysql 拦截器
 *
 * @Author: jiawenhao
 * @Date: 2022-11-5  18:48
 */
public class MysqlMonitor {

    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable, @AllArguments Object... args) throws Exception {

        Object resObj = null;
        try {
            resObj = callable.call();
            return resObj;
        } finally {

            String originalSql = ReflectUtil.invoke(obj, "getPreparedSql");
            if (StrUtil.isNotBlank(originalSql)) {
                System.out.println(originalSql.contains("\n\r"));;
            }

            String replaceSql = ReflectUtil.invoke(obj, "asSql");
            ConsoleInfoUtil.consoleInfo
                    .appendLog("数据库名称：Mysql")
                    .appendLog("线程ID：" + Thread.currentThread().getId())
                    .appendLog("时间：" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN))
                    .appendLog("原始SQL: ")
                    .appendLog(originalSql)
                    .appendLog("替换SQL：")
                    .appendLog(replaceSql);
        }
    }
}
