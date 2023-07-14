package com.example.repeatconfiguration.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel panel;
    private final JSpinner debugSleepTimeMs = new JSpinner(new SpinnerNumberModel(1000, 0, 10000, 1));

    public AppSettingsComponent() {
        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Debug sleep time (ms)"), debugSleepTimeMs, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JComponent getPreferredFocusedComponent() {
        return debugSleepTimeMs;
    }

    public JPanel getPanel() {
        return panel;
    }

    public int getDebugSleepTimeMs() {
        return (int) debugSleepTimeMs.getValue();
    }

    public void setDebugSleepTimeMs(int value) {
        debugSleepTimeMs.setValue(value);
    }
}
