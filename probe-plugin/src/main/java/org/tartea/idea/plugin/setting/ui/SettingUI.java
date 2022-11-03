package org.tartea.idea.plugin.setting.ui;

import org.apache.commons.lang.StringUtils;
import org.tartea.idea.plugin.infrastructure.DataSetting;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

public class SettingUI {
    private JPanel mainPanel;
    private JPanel settingPanel;
    private JTextField textField;
    private JButton btn;
    private JLabel label;

    private DataSetting instance = DataSetting.getInstance();

    public SettingUI() {
        if(StringUtils.isNotEmpty(instance.getAgentPath())){
            textField.setText(instance.getAgentPath());
        }
        // 给按钮添加一个选择文件的事件
        btn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.showOpenDialog(settingPanel);
            File file = fileChooser.getSelectedFile();
            if (Objects.nonNull(file) && StringUtils.isNotEmpty(file.getPath())) {
                textField.setText(file.getPath());
            }
        });
    }

    public JComponent getComponent() {
        return mainPanel;
    }

    public JTextField getTextField() {
        return textField;
    }
}
