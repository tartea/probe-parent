package org.tartea.plugin;


import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.tartea.plugin.monitor.MysqlMonitor;
import org.tartea.plugin.monitor.TestAnnotationMonitor;

import java.lang.instrument.Instrumentation;

public class PreAgent {
    //JVM 首先尝试在代理类上调用以下方法
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(agentArgs);
        //@Test 拦截
        new AgentBuilder
                .Default()
                .type(
                        //拦截junit4 junit5
                        ElementMatchers.nameStartsWith(agentArgs)
                                //拦截mysql
                                .or(ElementMatchers.nameStartsWith("com.mysql.cj.jdbc.ClientPreparedStatement"))
                                .or(ElementMatchers.nameStartsWith("com.mysql.jdbc.PreparedStatement"))

                )
                .transform((builder, typeDescription, classLoader, javaModule) -> {
                    return builder
                            .method(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.junit.Test")))
                            .intercept(MethodDelegation.to(TestAnnotationMonitor.class))
                            .method(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.junit.jupiter.api.Test")))
                            .intercept(MethodDelegation.to(TestAnnotationMonitor.class))
                            .method(ElementMatchers.named("execute"))
                            .intercept(MethodDelegation.to(MysqlMonitor.class)); // 委托
                })
                .installOn(inst);

    }

    //如果代理类没有实现上面的方法，那么 JVM 将尝试调用该方法
    public static void premain(String agentArgs) {
    }

}
