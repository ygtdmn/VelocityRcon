package me.uniodex.velocityrcon.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;

import java.net.InetSocketAddress;

public class RconServer {

    @Getter
    private final RconHandler handler;

    @Getter final InetSocketAddress address;

    private final ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public RconServer(InetSocketAddress address, final String password, RconHandler handler) {
        this.handler = handler;
        this.address = address;

        bootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new RconFramingHandler())
                                .addLast(new RconChannelHandler(RconServer.this, password));
                    }
                });
    }

    public ChannelFuture bind() {
        handler.onBind(this);
        return bootstrap.bind(address);
    }

    /**
     * Shut the RCON server down
     */
    public void shutdown() {
        handler.onShutdown(this);
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
