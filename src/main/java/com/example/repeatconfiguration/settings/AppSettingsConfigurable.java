package com.example.repeatconfiguration.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent component;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Repeat Configuration";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return component.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        component = new AppSettingsComponent();
        return component.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState state = AppSettingsState.getInstance();
        return component.getDebugSleepTimeMs() != state.debugSleepTimeMs;
    }

    @Override
    public void apply() {
        AppSettingsState state = AppSettingsState.getInstance();
        state.debugSleepTimeMs = component.getDebugSleepTimeMs();
    }

    @Override
    public void reset() {
        AppSettingsState state = AppSettingsState.getInstance();
        component.setDebugSleepTimeMs(state.debugSleepTimeMs);
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }
}