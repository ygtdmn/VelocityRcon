package me.uniodex.velocityrcon;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import me.uniodex.velocityrcon.server.RconServer;
import me.uniodex.velocityrcon.utils.Utils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "velocityrcon", name = "VelocityRcon", version = "1.0",
        description = "Adds RCON to Velocity", authors = {"UnioDex"})
public class VelocityRcon {
    private final ProxyServer server;
    @Getter
    private final Logger logger;

    @Getter
    private int rconPort = 1337;
    @Getter
    private String rconPassword = "8Qnvb563haX26DDF";
    @Getter
    private boolean rconColored = true;
    @Getter
    private static VelocityRcon instance;
    private RconServer rconServer;

    @Inject
    public VelocityRcon(ProxyServer server, Logger logger, @DataDirectory final Path folder) {
        this.server = server;
        this.logger = logger;
        instance = this;

        Toml toml = loadConfig(folder);
        if (toml == null) {
            logger.warn("Failed to load rcon.toml. Shutting down.");
            return;
        }

        if (Utils.isInteger(toml.getString("rcon-port"))) {
            rconPort = Integer.valueOf(toml.getString("rcon-port"));
        } else {
            logger.warn("Invalid rcon port. Shutting down.");
            return;
        }
        rconPassword = toml.getString("rcon-password");
        rconColored = toml.getBoolean("rcon-colored");
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        startListener();
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        stopListener();
    }

    private void startListener() {
        InetSocketAddress address = new InetSocketAddress(rconPort);
        rconServer = new RconServer(server, rconPassword);
        logger.info("Binding rcon to address: /" + address.getHostName() + ":" + address.getPort());

        ChannelFuture future = rconServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();

        if (!channel.isActive()) {
            logger.warn("Failed to bind rcon port. Address already in use?");
        }
    }

    private void stopListener() {
        if (rconServer != null) {
            logger.info("Trying to stop RCON listener");

            rconServer.shutdown();
        }
    }

    private Toml loadConfig(Path path) {
        File folder = path.toFile();
        File file = new File(folder, "rcon.toml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStream input = getClass().getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }

        return new Toml().read(file);
    }
}
