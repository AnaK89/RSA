package ru.AnaK.RSA;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.AnaK.RSA.handlers.ClientHandler;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class Client {
    private static final Logger log = Logger.getLogger(Client.class.getName());
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final ClientHandler clientHandler;

    public Client(BigInteger p, BigInteger q){
        clientHandler = new ClientHandler(p, q);
    }

    public void run() throws InterruptedException{
        log.info("-------------------- start --------------------");
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(group);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 8080));
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel){
                socketChannel.pipeline().addLast(clientHandler);
            }
        });

        ChannelFuture channelFuture = clientBootstrap.connect().sync();
        channelFuture.channel().closeFuture().sync();
        log.info("-------------------- complete --------------------");
    }
}
