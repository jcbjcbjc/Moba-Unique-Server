package com.game.network.Connection;

public interface ConnectionManager {
    <T> NetConnection getConnection(T ctx) ;

    void addToConnection(int userId, NetConnection conn);

    <T> void removeConnection(T context);

    void removeConnection(int userId);

    NetConnection getConnection(int userId);
}
