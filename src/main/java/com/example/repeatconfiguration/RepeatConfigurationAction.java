package com.example.repeatconfiguration;

import com.example.repeatconfiguration.settings.AppSettingsState;
import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class RepeatConfigurationAction extends AnAction {

    private static final AppSettingsState STATE = AppSettingsState.getInstance();

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
        ApplicationManager.getApplication().invokeLater(() -> startDebugger(project, configuration, false));

        MessageBusConnection connection = ConnectionManager.getConnection();

        if (connection != null) {
            connection.disconnect();
        }

        connection = project.getMessageBus().connect();
        ConnectionManager.setConnection(connection);

        connection.subscribe(ExecutionManager.EXECUTION_TOPIC, new ExecutionListener() {
            @Override
            public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler, int exitCode) {
                if (!env.getRunProfile().getName().equals(configuration.getName())) {
                    return;
                }

                ApplicationManager.getApplication().invokeLater(() -> startDebugger(project, configuration, true));
            }
        });
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        MessageBusConnection connection = ConnectionManager.getConnection();
        e.getPresentation().setEnabled(connection == null);
    }

    private void startDebugger(Project project, RunConfiguration runConfiguration, boolean timeout) {
        if (timeout) {
            try {
                TimeUnit.MILLISECONDS.sleep(STATE.debugSleepTimeMs);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();

        try {
            ExecutionEnvironmentBuilder.create(project, executor, runConfiguration).buildAndExecute();
        } catch (ExecutionException ignored) {
        }
    }
}