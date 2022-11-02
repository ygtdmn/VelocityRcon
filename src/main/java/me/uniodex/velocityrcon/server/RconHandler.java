package me.uniodex.velocityrcon.server;

import java.net.SocketAddress;

/**
 * Implementation of the handler for data and events coming from the RCON server
 */
public interface RconHandler {

    /**
     * Called right before {@link RconServer#bind()} is called
     *
     * @param server The {@link RconServer} of this handler
     */
    default void onBind(RconServer server) {}

    /**
     * Called when the client connects
     *
     * @param server The {@link RconServer} of this handler
     * @param clientAddress The {@link SocketAddress} of the client
     */
    default void onClientConnected(RconServer server, SocketAddress clientAddress) {}

    /**
     * Called when the client disconnects
     *
     * @param server The {@link RconServer} of this handler
     * @param clientAddress The {@link SocketAddress} of the client
     */
    default void onClientDisconnected(RconServer server, SocketAddress clientAddress) {}

    /**
     * Method that processes incoming data and returns a response
     *
     * @param server The {@link RconServer} of this handler
     * @param payload Received command
     * @param clientAddress The {@link SocketAddress} of the client
     * @return Response string, the result of processing the command
     */
    String processData(RconServer server, String payload, SocketAddress clientAddress);

    /**
     * Called before {@link RconServer} the RCON server is shut down
     *
     * @param server The {@link RconServer} of this handler
     */
    default void onShutdown(RconServer server) {}
}
