package com.example.repeatconfiguration;

import com.intellij.util.messages.MessageBusConnection;

public class ConnectionManager {

    public static MessageBusConnection connection;

    public static MessageBusConnection getConnection() {
        return connection;
    }

    public static void setConnection(MessageBusConnection connection) {
        ConnectionManager.connection = connection;
    }
}
