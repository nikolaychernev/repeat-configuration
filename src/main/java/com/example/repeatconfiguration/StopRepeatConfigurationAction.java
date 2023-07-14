package com.example.repeatconfiguration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

public class StopRepeatConfigurationAction extends AnAction {

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
        ApplicationManager.getApplication().invokeLater(() -> stopDebugger(project, configuration));

        MessageBusConnection connection = ConnectionManager.getConnection();

        if (connection != null) {
            connection.disconnect();
            ConnectionManager.setConnection(null);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        MessageBusConnection connection = ConnectionManager.getConnection();
        e.getPresentation().setEnabled(connection != null);
    }

    private void stopDebugger(Project project, RunConfiguration runConfiguration) {
        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();
//
//        try {
//            ExecutionEnvironmentBuilder.create(project, executor, runConfiguration).buildAndExecute();
//        } catch (ExecutionException ignored) {
//        }
    }
}