package org.tartea.plugin.monitor;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import net.bytebuddy.implementation.bind.annotation.*;
import org.tartea.plugin.constants.BaseConstant;
import org.tartea.plugin.logger.BeautifyConsole;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * junit4 junit5 Test注解
 * main方法
 *
 * @Author: jiawenhao
 * @Date: 2022-11-5  15:58
 */
public class JunitAnnotationMonitor {


    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable, @AllArguments Object... args) throws Exception {
        Date startDate = new Date();
        try {
            return callable.call();
        } finally {
            handleInfo(startDate, obj, method);
        }
    }

    /**
     * 处理junit信息
     *
     * @param startDate
     * @param obj
     * @param method
     */
    private static void handleInfo(Date startDate, Object obj, Method method) {
        Date endDate = new Date();
        try {

            if (obj.getClass().getName().startsWith(BaseConstant.PREFIX_MAIN_PACKAGE) && Objects.equals(method.getName(), "start")
                    || Objects.nonNull(method.getAnnotation(org.junit.jupiter.api.Test.class))
                    || Objects.nonNull(method.getAnnotation(org.junit.Test.class))) {

                List<String> infos = new ArrayList<String>();
                infos.add("方法名称：【 " + obj.getClass().getName() + "." + method.getName() + " 】");
                infos.add("方法执行开始时间：" + DateUtil.format(startDate, DatePattern.NORM_DATETIME_MS_PATTERN));
                infos.add("方法执行结束时间：" + DateUtil.format(endDate, DatePattern.NORM_DATETIME_MS_PATTERN));
                infos.add("累计执行时间：" + DateUtil.formatBetween(startDate, endDate));
                new BeautifyConsole().printInfo(infos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
