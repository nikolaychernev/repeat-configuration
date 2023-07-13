package com.example.repeatconfiguration;

import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class RepeatConfigurationAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            return;
        }

        RunnerAndConfigurationSettings selectedConfiguration = RunManager.getInstance(project).getSelectedConfiguration();

        if (selectedConfiguration == null) {
            return;
        }

        RunConfiguration configuration = selectedConfiguration.getConfiguration();
        startDebugger(project, configuration);

        MessageBusConnection connection = ConnectionManager.getConnection();

        if (connection != null) {
            connection.disconnect();
        }

        connection = project.getMessageBus().connect();
        ConnectionManager.setConnection(connection);

        connection.subscribe(ExecutionManager.EXECUTION_TOPIC, new ExecutionListener() {
            @Override
            public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler, int exitCode) {
                if (env.getRunProfile().getName().equals(configuration.getName())) {
                    startDebugger(project, configuration);
                }
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        MessageBusConnection connection = ConnectionManager.getConnection();
        e.getPresentation().setEnabled(connection == null);
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