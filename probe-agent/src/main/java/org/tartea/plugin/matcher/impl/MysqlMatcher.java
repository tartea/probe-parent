package org.tartea.plugin.matcher.impl;

import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.tartea.plugin.matcher.AbstractMatcher;
import org.tartea.plugin.monitor.MysqlMonitor;

import java.util.Objects;

/**
 * mysql 匹配
 *
 * @Author: jiawenhao
 * @Date: 2022-11-7  21:40
 */
public class MysqlMatcher extends AbstractMatcher<Boolean> {

    public MysqlMatcher(Boolean param) {
        this.param = param;
    }

    @Override
    public ElementMatcher.Junction getMatcher(ElementMatcher.Junction elementMatcher) {
        if (Objects.isNull(elementMatcher)) {
            return ElementMatchers.nameStartsWith("com.mysql.cj.jdbc.ClientPreparedStatement")
                    .or(ElementMatchers.nameStartsWith("com.mysql.jdbc.PreparedStatement"));
        }
        return elementMatcher.or(ElementMatchers.nameStartsWith("com.mysql.cj.jdbc.ClientPreparedStatement"))
                .or(ElementMatchers.nameStartsWith("com.mysql.jdbc.PreparedStatement"));
    }

    @Override
    public DynamicType.Builder getBuilder(DynamicType.Builder builder) {
        return builder
                .method(ElementMatchers.named("execute"))
                .intercept(MethodDelegation.to(MysqlMonitor.class));
    }
}
