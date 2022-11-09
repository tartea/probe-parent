package org.tartea.plugin.matcher;

import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * agent 匹配
 *
 * @Author: jiawenhao
 * @Date: 2022-11-7  21:40
 */
public abstract class AbstractMatcher<T> {

    protected T setting;

    abstract public ElementMatcher.Junction getMatcher(ElementMatcher.Junction elementMatcher);

    abstract public DynamicType.Builder getBuilder(DynamicType.Builder builder);
}
