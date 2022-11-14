package org.tartea.plugin.runner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.*;
import com.intellij.execution.impl.DefaultJavaProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;
import org.tartea.plugin.component.ArgsHandleComponent;
import org.tartea.plugin.constants.BaseConstant;
import org.tartea.plugin.infrastructure.DataSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Executor {

    private DataSetting instance = DataSetting.getInstance();

    protected void run(RunProfileState state, ExecutionEnvironment env) throws ExecutionException {
        try {
            //判断agent路径是否存在
            if (StringUtils.isEmpty(instance.getAgentPath())) {
                Project project = env.getDataContext().getData(CommonDataKeys.PROJECT);
                Messages.showWarningDialog(project, "请手动配置agent路径", "Warning");
                return;
            }
            if (!(state instanceof JavaCommandLine)) {
                return;
            }
            if (Objects.isNull(env.getDataContext())) {
                return;
            }
            Map<String, Object> params = new HashMap<>();
            //设置包名
            confirmConfigPackage(params, state, env);
            //设置其他属性
            buildOtherConfig(params);
            JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
            // 添加字节码插装
            ParametersList parametersList = parameters.getVMParametersList();
            String args = ArgsHandleComponent.join(params);
            if (StringUtils.isNotEmpty(args)) {
                parametersList.add("-javaagent:" + instance.getAgentPath() + "=" + args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 确实是否含有包名
     *
     * @param params
     * @param state
     * @param env
     */
    private void confirmConfigPackage(Map<String, Object> params, RunProfileState state, ExecutionEnvironment env) {
        try {
            String packageName = null;
            RunProfile runProfile = env.getRunProfile();
            //TODO 目前不支持main方法
            if (runProfile instanceof ApplicationConfiguration) {
                return;
            } else if (Objects.equals(runProfile.getClass().getSimpleName(), "JUnitConfiguration")) {
                //由于目前没有引入JUnitConfiguration，所以采用这种方式判断
                packageName = ReflectUtil.invoke(BeanUtil.getFieldValue(env.getRunProfile(), "myData"), "getPackageName");
            }
            if (Objects.nonNull(packageName)) {
                params.put(BaseConstant.ARGS_PACKAGE, packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建其他属性
     *
     * @param params
     */
    private void buildOtherConfig(Map<String, Object> params) {
        //设置mysql属性
        params.put(BaseConstant.ARGS_MYSQL, instance.getMysqlSetting());
    }
}
