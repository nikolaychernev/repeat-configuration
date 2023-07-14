package com.example.repeatconfiguration;

import com.intellij.execution.ExecutionManager;
import com.intellij.execution.process.ProcessHandler;
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

        MessageBusConnection connection = ConnectionManager.getConnection();

        if (connection != null) {
            connection.disconnect();
            ConnectionManager.setConnection(null);
        }

        ApplicationManager.getApplication().invokeLater(() -> stopDebugger(project));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        MessageBusConnection connection = ConnectionManager.getConnection();
        e.getPresentation().setEnabled(connection != null);
    }

    private void stopDebugger(Project project) {
        ProcessHandler[] runningProcesses = ExecutionManager.getInstance(project).getRunningProcesses();

        for (ProcessHandler runningProcess : runningProcesses) {
            runningProcess.destroyProcess();
        }
    }
}