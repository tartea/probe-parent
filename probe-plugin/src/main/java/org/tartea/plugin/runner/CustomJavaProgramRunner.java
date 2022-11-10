package org.tartea.plugin.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaCommandLine;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.impl.DefaultJavaProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.tartea.plugin.component.ArgsHandleComponent;
import org.tartea.plugin.constants.BaseConstant;
import org.tartea.plugin.infrastructure.DataSetting;

import java.util.HashMap;
import java.util.Map;

import java.util.Objects;

public class CustomJavaProgramRunner extends DefaultJavaProgramRunner {

    private DataSetting instance = DataSetting.getInstance();

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {

        try {
            if ((state instanceof JavaCommandLine) && Objects.nonNull(env.getDataContext())) {
                JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
                // 信息获取
                PsiFile psiFile = env.getDataContext().getData(LangDataKeys.PSI_FILE);
                if (psiFile instanceof PsiJavaFileImpl) {
                    PsiJavaFileImpl psiJavaFile = (PsiJavaFileImpl) psiFile;
                    //当前选择的文件包名
                    String packageName = psiJavaFile.getPackageName();
                    // 添加字节码插装
                    ParametersList parametersList = parameters.getVMParametersList();
                    //获取agent路径
                    if (StringUtils.isEmpty(instance.getAgentPath())) {
                        Project project = env.getDataContext().getData(CommonDataKeys.PROJECT);
                        Messages.showWarningDialog(project, "请手动配置agent路径", "Warning");
                    } else {
                        parametersList.add("-javaagent:" + instance.getAgentPath() + "=" + buildParam(packageName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.doExecute(state, env);
    }

    /**
     * 设置参数
     *
     * @param packageName
     * @return
     */
    private String buildParam(String packageName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(BaseConstant.ARGS_PACKAGE, packageName);
        params.put(BaseConstant.ARGS_MYSQL, instance.getMysqlSetting());
        return ArgsHandleComponent.join(params);
    }
}
