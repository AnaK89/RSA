package ru.AnaK.RSA;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.AnaK.RSA.handlers.ServerHandler;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class Server {
    private static final Logger log = Logger.getLogger(Server.class.getName());
    private final ServerHandler serverHandler;


    public Server(BigInteger p, BigInteger q){
        serverHandler = new ServerHandler(p, q);
    }

    public void run() throws InterruptedException {
        log.info("-------------------- start --------------------");
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(new InetSocketAddress("localhost", 8080));

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel){
                socketChannel.pipeline().addLast(serverHandler);
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        channelFuture.channel().closeFuture().sync();
        log.info("-------------------- complete --------------------");
    }
}
