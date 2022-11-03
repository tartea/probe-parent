package org.tartea.idea.plugin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

public class MonitorMethod {

    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable, @AllArguments Object... args) throws Exception {
        Date startDate = new Date();

        Object resObj = null;
        try {
            resObj = callable.call();
            return resObj;
        } finally {
            Date endDate = new Date();
            System.out.print("\033[1;94m");
            System.out.println("方法名称：【" + obj.getClass().getName() + "." + method.getName() + "】");
            System.out.println("方法执行开始时间：" + DateUtil.format(startDate, DatePattern.NORM_DATETIME_MS_PATTERN));
            System.out.println("方法执行结束时间：" + DateUtil.format(endDate, DatePattern.NORM_DATETIME_MS_PATTERN));
            System.out.println("累计执行时间：" + DateUtil.formatBetween(startDate, endDate));
//            System.out.println("入参个数：" + method.getParameterCount());
//            for (int i = 0; i < method.getParameterCount(); i++) {
//                System.out.println("入参 Idx：" + (i + 1) + " 类型：" + method.getParameterTypes()[i].getTypeName() + " 内容：" + args[i]);
//            }
//            System.out.println("出参类型：" + method.getReturnType().getName());
//            System.out.println("出参结果：" + resObj);
//            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
