package org.tartea.plugin.monitor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import net.bytebuddy.implementation.bind.annotation.*;
import org.tartea.plugin.util.ConsoleInfoUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
            if (Objects.equals(obj.getClass().getName(), "com.mysql.jdbc.PreparedStatement")) {
                buildPreparedStatementSql(obj);
            } else if (Objects.equals(obj.getClass().getSimpleName(), "com.mysql.cj.jdbc.ClientPreparedStatement")) {
                buildClientPreparedStatement(obj);
            }

        }
    }

    /**
     * 5.8以下的驱动
     *
     * @param obj
     */
    private static void buildPreparedStatementSql(Object obj) {
        try {
            String originalSql = (String) BeanUtil.getFieldValue(obj, "originalSql");
            String replaceSql = ReflectUtil.invoke(obj, "asSql");

            formatPrintInfo(originalSql, replaceSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * mysql 5.8以后的驱动
     *
     * @param obj
     */
    private static void buildClientPreparedStatement(Object obj) {
        try {
            String originalSql = ReflectUtil.invoke(obj, "getPreparedSql");
            String replaceSql = ReflectUtil.invoke(obj, "asSql");
            formatPrintInfo(originalSql, replaceSql);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 格式化输出信息
     *
     * @param originalSql
     * @param replaceSql
     */
    private static void formatPrintInfo(String originalSql, String replaceSql) {
        List<String> infos = new ArrayList<String>();
        infos.add("数据库名称：Mysql");
        infos.add("时间：" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN));
        infos.add("原始SQL: ");
        infos.add(originalSql);
        infos.add("替换SQL：");
        infos.add(replaceSql);
       ConsoleInfoUtil.console.addParams(infos);
    }

}
