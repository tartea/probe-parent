package org.tartea.idea.plugin.setting.factory;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tartea.idea.plugin.infrastructure.DataSetting;
import org.tartea.idea.plugin.setting.ui.SettingUI;

import javax.swing.*;

public class SettingFactory implements SearchableConfigurable {

    private SettingUI settingUI = new SettingUI();
    private DataSetting instance = DataSetting.getInstance();

    @Override
    public @NotNull String getId() {
        return "setting.id";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "setting-config";
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
        String url = settingUI.getTextField().getText();
        // 设置文本信息
        try {
            instance.getState().setAgentPath(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}