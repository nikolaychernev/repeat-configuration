package com.example.repeatconfiguration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class RepeatConfigurationAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String configurationName = "Debug lambda";
        RunConfiguration configuration = getConfiguration(e.getProject(), configurationName);

        if (configuration == null) {
            return;
        }

        startDebugger(e.getProject(), configuration);
    }

    private RunConfiguration getConfiguration(Project project, String configurationName) {
        RunManager runManager = RunManager.getInstance(project);

        for (RunConfiguration runConfiguration : runManager.getAllConfigurationsList()) {
            if (runConfiguration.getName().equals(configurationName)) {
                return runConfiguration;
            }
        }

        return null;
    }

    private void startDebugger(Project project, RunConfiguration runConfiguration) {
        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();

        try {
            ExecutionEnvironmentBuilder
                    .create(project, executor, runConfiguration)
                    .buildAndExecute();
        } catch (ExecutionException ignored) {
        }
    }
}