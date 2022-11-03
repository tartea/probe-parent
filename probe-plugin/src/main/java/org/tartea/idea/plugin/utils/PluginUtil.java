package org.tartea.idea.plugin.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PluginUtil {

    private static String agentPath;

    private static final IdeaPluginDescriptor IDEA_PLUGIN_DESCRIPTOR;

    static {
        PluginId pluginId = PluginId.getId("org.tartea.idea.plugin");
        IDEA_PLUGIN_DESCRIPTOR = PluginManagerCore.getPlugin(pluginId);
    }

    /**
     * 获取核心Jar路径
     *
     * @return String
     */
    public static String getAgentCoreJarPath() {
        if (StringUtils.isEmpty(agentPath)) {
            agentPath = getJarPathByStartWith("ProjectProbe");
        }
        return agentPath;
    }

    /**
     * 根据jar包的前缀名称获路径
     *
     * @param startWith 前缀名称
     * @return String
     */
    private static String getJarPathByStartWith(String startWith) {
        final String quotes = "\"";
        if (Objects.nonNull(IDEA_PLUGIN_DESCRIPTOR)) {
            List<File> files = FileUtil.loopFiles(IDEA_PLUGIN_DESCRIPTOR.getPath());
            for (File file : files) {
                String name = file.getName();
                if (name.startsWith(startWith)) {
                    String pathStr = FileUtil.getCanonicalPath(file);
//                    if (StrUtil.contains(pathStr, StrUtil.SPACE)) {
//                        return StrUtil.builder().append(quotes).append(pathStr).append(quotes).toString();
//                    }
                    return pathStr;
                }
            }
        }
        return StrUtil.EMPTY;
    }

}
