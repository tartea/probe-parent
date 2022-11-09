package org.tartea.plugin.setting.factory;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tartea.plugin.infrastructure.DataSetting;
import org.tartea.plugin.infrastructure.DataState;
import org.tartea.plugin.setting.ui.SettingUI;

import javax.swing.*;

public class SettingFactory implements SearchableConfigurable {

    private SettingUI settingUI = new SettingUI();
    private DataSetting instance = DataSetting.getInstance();

    @Override
    public @NotNull String getId() {
        return "probe.id";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "probe-config";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return settingUI.getComponent();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

        try {
            DataState state = instance.getState();

            String url = settingUI.getTextField().getText();
            state.setAgentPath(url);

            boolean mysqlSetting = settingUI.getMysqlCheckBox().isSelected();
            state.setMysqlSetting(mysqlSetting);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}