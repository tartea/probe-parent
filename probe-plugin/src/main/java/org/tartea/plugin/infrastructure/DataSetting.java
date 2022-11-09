package org.tartea.plugin.infrastructure;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tartea.plugin.utils.PluginUtil;

@State(name = "DataSetting", storages = @Storage("plugin.xml"))
public class DataSetting implements PersistentStateComponent<DataState> {

    private DataState state = new DataState();

    public static DataSetting getInstance() {
        return ServiceManager.getService(DataSetting.class);
    }

    @Nullable
    @Override
    public DataState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull DataState state) {
        this.state = state;
    }

    /**
     * 获取agent路径
     *
     * @return
     */
    public String getAgentPath() {
        if (StringUtils.isEmpty(state.getAgentPath())) {
            state.setAgentPath(PluginUtil.getAgentCoreJarPath());
        }
        return state.getAgentPath();
    }

    /**
     * 获取mysql配置
     *
     * @return
     */
    public boolean getMysqlSetting() {
        return state.getMysqlSetting();
    }

}
