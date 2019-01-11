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

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ServerHandler.class.getName());
    private final NettySerializer nettySerializer = new NettySerializer();
    private final PairKeys pairKeys;
    private final FunctionsRSA functionsRSA;
    private OpenKey clientOpenKey;

    public ServerHandler(BigInteger p, BigInteger q){
        functionsRSA = new FunctionsRSA(p, q);
        pairKeys = functionsRSA.generatePairKeys();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws IOException, ClassNotFoundException {
        OpenKey openKey = nettySerializer.deserializeObjectToOK(o);
        if (openKey.getStatus()){
            clientOpenKey = openKey;
            nettySerializer.writeToChannel(ctx, pairKeys.getOpenKey());
        } else {
            String str = nettySerializer.deserializeObjectToM(o);
            if ( ! str.isEmpty()){
                if ("Client connect...".equals(str)){
                    nettySerializer.writeToChannel(ctx, "Connection is active");
                    log.info("Connection is active");
                }
            } else {
                BigInteger[] encryptedMessage = nettySerializer.deserializeObjectToEncrM(o);
                if (encryptedMessage.length > 1){
                    log.info("Server received: " + functionsRSA.decrypt(pairKeys.getCloseKey(), encryptedMessage));
                    nettySerializer.writeToChannel(ctx, functionsRSA.encrypt(clientOpenKey, "Example2"));
                }
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
