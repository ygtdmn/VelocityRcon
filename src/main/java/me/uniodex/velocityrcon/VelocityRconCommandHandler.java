package me.uniodex.velocityrcon;

import com.velocitypowered.api.proxy.ProxyServer;
import me.uniodex.velocityrcon.server.RconHandler;
import me.uniodex.velocityrcon.server.RconServer;
import me.uniodex.velocityrcon.utils.Utils;
import org.slf4j.Logger;

import java.net.SocketAddress;

public class VelocityRconCommandHandler implements RconHandler {
    private final Logger logger;
    private final ProxyServer proxyServer;

    private final RconCommandSource commandSender;


    public VelocityRconCommandHandler(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.commandSender = new RconCommandSource(proxyServer);
    }

    @Override
    public void onBind(RconServer server) {
        logger.info("Binding RCON to address: /" + server.getAddress().getHostName() + ":" + server.getAddress().getPort());
    }

    @Override
    public void onClientConnected(RconServer server, SocketAddress clientAddress) {
        logger.info("RCON connection from [{}]", clientAddress);
    }

    @Override
    public void onClientDisconnected(RconServer server, SocketAddress clientAddress) {
        logger.info("RCON client [{}] disconnected ", clientAddress);
    }

    @Override
    public String processData(RconServer server, String payload, SocketAddress clientAddress) {
        boolean success;
        String message;

        if (payload.equalsIgnoreCase("end") || payload.equalsIgnoreCase("stop")) {
            success = true;
            message = "Shutting down the proxy...";
            proxyServer.shutdown();
        } else {
            try {
                success = proxyServer.getCommandManager().executeAsync(commandSender, payload).join();
                if (success) {
                    message = commandSender.flush();
                } else {
                    message = "No such command";
                }
            } catch (Exception e) {
                e.printStackTrace();
                success = false;
                message = "Unknown error";
            }
        }

        if (!success) {
            message = String.format("Error executing: %s (%s)", payload, message);
        }

        if (!VelocityRcon.getInstance().isRconColored()) {
            message = Utils.stripColor(message);
        }

        return message;
    }

    @Override
    public void onShutdown(RconServer server) {
        logger.info("Stopping RCON listener");
    }
}
