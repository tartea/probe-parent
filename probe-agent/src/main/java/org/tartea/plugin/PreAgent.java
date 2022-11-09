package org.tartea.plugin;


import net.bytebuddy.agent.builder.AgentBuilder;
import org.tartea.plugin.matcher.MatcherHandler;

import java.lang.instrument.Instrumentation;

public class PreAgent {
    //JVM 首先尝试在代理类上调用以下方法
    public static void premain(String agentArgs, Instrumentation inst) {
        MatcherHandler matcherHandler = new MatcherHandler(agentArgs);
        //@Test 拦截
        new AgentBuilder
                .Default()
                .type(matcherHandler.getElementMatcher())
                .transform((builder, typeDescription, classLoader, javaModule) -> matcherHandler.getBuilder(builder))
                .installOn(inst);

    }
    //如果代理类没有实现上面的方法，那么 JVM 将尝试调用该方法
    public static void premain(String agentArgs) {
    }

}
