package org.tartea.plugin.matcher;

import com.alibaba.fastjson.JSON;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import org.tartea.plugin.component.ArgsHandleComponent;
import org.tartea.plugin.constants.BaseConstant;
import org.tartea.plugin.matcher.impl.MysqlMatcher;
import org.tartea.plugin.matcher.impl.JunitMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据参数匹配合适的方法
 *
 * @Author: jiawenhao
 * @Date: 2022-11-7  21:14
 */
public class MatcherHandler {

    private Map<String, Object> params = new HashMap<>();
    private List<AbstractMatcher> matcherList = new ArrayList<AbstractMatcher>();

    public MatcherHandler(String args) {
        this.params.putAll(ArgsHandleComponent.split(args));
        System.out.println("解析参数" + JSON.toJSONString(params));
        matcherSetting();
    }


    private void matcherSetting() {
        if (params.containsKey(BaseConstant.ARGS_PACKAGE)) {
            matcherList.add(new JunitMatcher((String) params.get(BaseConstant.ARGS_PACKAGE)));
        }
        if (params.containsKey(BaseConstant.ARGS_MYSQL)) {
            matcherList.add(new MysqlMatcher((Boolean) params.get(BaseConstant.ARGS_MYSQL)));
        }
    }


    /**
     * 获取符合要求的matcher
     *
     * @return
     */
    public ElementMatcher getElementMatcher() {
        ElementMatcher.Junction elementMatcher = null;
        for (AbstractMatcher matcher : matcherList) {
            elementMatcher = matcher.getMatcher(elementMatcher);
        }
        return elementMatcher;
    }


    /**
     * 代理类
     *
     * @param builder
     * @return
     */
    public DynamicType.Builder getBuilder(DynamicType.Builder builder) {
        for (AbstractMatcher matcher : matcherList) {
            builder = matcher.getBuilder(builder);
        }
        return builder;

    }
}
