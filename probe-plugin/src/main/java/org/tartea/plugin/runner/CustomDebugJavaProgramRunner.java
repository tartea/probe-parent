package org.tartea.plugin.runner;

import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import org.jetbrains.annotations.NotNull;

/**
 * idea debug 执行器
 */
public class CustomDebugJavaProgramRunner extends GenericDebuggerRunner {

    private Executor executor = new Executor();

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        executor.run(state, env);
        return super.doExecute(state, env);
    }
}
