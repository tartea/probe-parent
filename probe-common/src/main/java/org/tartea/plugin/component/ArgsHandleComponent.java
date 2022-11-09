package org.tartea.plugin.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * 参数处理
 *
 * @Author: jiawenhao
 * @Date: 2022-11-6  14:41
 */
public class ArgsHandleComponent {

    private static String SEPARATOR = "&";


    /**
     * 拼接参数
     *
     * @param paramMap
     * @return
     */
    public static String join(Map<String, Object> paramMap) {
        if (CollectionUtil.isEmpty(paramMap)) {
            return "";
        }
        List<String> result = new ArrayList<String>(paramMap.size());
        paramMap.forEach((key, value) -> {
            if (StrUtil.isNotBlank(key) && Objects.nonNull(value)) {
                result.add(key + "=" + value);
            }
        });
        return String.join(SEPARATOR, result);
    }

    /**
     * 分割参数
     *
     * @param paramStr
     * @return
     */
    public static Map<String, String> split(String paramStr) {
        if (StrUtil.isEmpty(paramStr)) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<String, String>();
        String[] params = paramStr.split(SEPARATOR);
        Arrays.stream(params).forEach(param -> {
            String[] split = param.split("=");
            result.put(split[0], split[1]);
        });
        return result;
    }


}
