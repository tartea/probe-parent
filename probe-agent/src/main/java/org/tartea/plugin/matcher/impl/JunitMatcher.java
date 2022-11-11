package org.tartea.plugin.matcher.impl;

import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.tartea.plugin.matcher.AbstractMatcher;
import org.tartea.plugin.monitor.JunitAnnotationMonitor;

import java.util.Objects;

/**
 * junit 匹配
 *
 * @Author: jiawenhao
 * @Date: 2022-11-7  21:40
 */
public class JunitMatcher extends AbstractMatcher<String> {


    public JunitMatcher(String setting) {
        this.setting = setting;
    }

    @Override
    public ElementMatcher.Junction getMatcher(ElementMatcher.Junction elementMatcher) {
        if(Objects.isNull(setting)){
            return elementMatcher;
        }
        if (Objects.isNull(elementMatcher)) {
            return ElementMatchers.nameStartsWith(this.setting);
        }
        return elementMatcher.or(ElementMatchers.nameStartsWith(this.setting));
    }

    @Override
    public DynamicType.Builder getBuilder(DynamicType.Builder builder) {
        if(Objects.isNull(setting)){
            return builder;
        }
        return builder
                .method(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.junit.Test").or(ElementMatchers.named("org.junit.jupiter.api.Test"))).or(ElementMatchers.named("start")))
                .intercept(MethodDelegation.to(JunitAnnotationMonitor.class));

    }
}
