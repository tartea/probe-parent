package org.tartea.plugin.monitor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import net.bytebuddy.implementation.bind.annotation.*;
import org.tartea.plugin.util.ConsoleInfoUtil;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;
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
            if (Objects.equals(obj.getClass().getSimpleName(), "PreparedStatement")) {
                buildPreparedStatementSql(obj);
            } else if (Objects.equals(obj.getClass().getSimpleName(), "ClientPreparedStatement")) {
                buildClientPreparedStatement(obj);
            }

        }
    }


    private static void buildPreparedStatementSql(Object obj) {
        String originalSql = (String) BeanUtil.getFieldValue(obj, "originalSql");
        String replaceSql = ReflectUtil.invoke(obj, "asSql");

        sqlInfo(originalSql, replaceSql);
    }


    private static void buildClientPreparedStatement(Object obj) {
        String originalSql = ReflectUtil.invoke(obj, "getPreparedSql");


        String replaceSql = ReflectUtil.invoke(obj, "asSql");

        sqlInfo(originalSql, replaceSql);
    }

    private static void sqlInfo(String originalSql, String replaceSql) {
        if (StrUtil.isNotBlank(originalSql)) {
            originalSql = originalSql.replace("\n", " ").replace("\r"," ").replace("\t"," ");
        }
        if (StrUtil.isNotBlank(replaceSql)) {
           replaceSql =  replaceSql.replace("\n", " ").replace("\r"," ").replace("\t"," ");
        }
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
