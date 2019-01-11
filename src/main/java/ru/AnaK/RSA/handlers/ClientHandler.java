package ru.AnaK.RSA.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.AnaK.RSA.model.NettySerializer;
import ru.AnaK.RSA.model.RSA.FunctionsRSA;
import ru.AnaK.RSA.model.RSA.OpenKey;
import ru.AnaK.RSA.model.RSA.PairKeys;

import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Logger;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ClientHandler.class.getName());
    private final NettySerializer nettySerializer = new NettySerializer();
    private final PairKeys pairKeys;
    private final FunctionsRSA functionsRSA;
    private OpenKey serverOpenKey;

    public ClientHandler(BigInteger p, BigInteger q){
        functionsRSA = new FunctionsRSA(p, q);
        pairKeys = functionsRSA.generatePairKeys();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String connection = "Client connect...";
        log.info(connection);
        nettySerializer.writeToChannel(ctx, connection);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws IOException, ClassNotFoundException {
        OpenKey openKey = nettySerializer.deserializeObjectToOK(o);
        if (openKey.getStatus()){
            serverOpenKey = openKey;
            nettySerializer.writeToChannel(ctx, functionsRSA.encrypt(serverOpenKey, "Example1"));
        } else {
            String str = nettySerializer.deserializeObjectToM(o);
            if ( ! str.isEmpty()){
                if ("Connection is active".equals(str)){
                    log.info("Connection is active");
                    nettySerializer.writeToChannel(ctx, pairKeys.getOpenKey());
                }
            } else {
                BigInteger[] encryptedMessage = nettySerializer.deserializeObjectToEncrM(o);
                if (encryptedMessage.length > 1){
                    log.info("Client received: " + functionsRSA.decrypt(pairKeys.getCloseKey(), encryptedMessage));
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}