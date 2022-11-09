package org.tartea.plugin.monitor;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import net.bytebuddy.implementation.bind.annotation.*;
import org.tartea.plugin.util.ConsoleInfoUtil;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 拦截junit4 junit5 Test注解
 *
 * @Author: jiawenhao
 * @Date: 2022-11-5  15:58
 */
public class JunitAnnotationMonitor {

    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable, @AllArguments Object... args) throws Exception {
        Date startDate = new Date();

        Object resObj = null;
        try {
            resObj = callable.call();
            return resObj;
        } finally {
            Date endDate = new Date();

            ConsoleInfoUtil.consoleInfo
                    .appendLog("方法名称：【" + obj.getClass().getName() + "." + method.getName() + "】")
                    .appendLog("方法执行开始时间：" + DateUtil.format(startDate, DatePattern.NORM_DATETIME_MS_PATTERN))
                    .appendLog("方法执行结束时间：" + DateUtil.format(endDate, DatePattern.NORM_DATETIME_MS_PATTERN))
                    .appendLog("累计执行时间：" + DateUtil.formatBetween(startDate, endDate))
                    .print();
        }
    }
}
