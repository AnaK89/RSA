package ru.AnaK.RSA.model;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.AnaK.RSA.model.RSA.OpenKey;

import java.io.*;
import java.math.BigInteger;
import java.util.logging.Logger;

public class NettySerializer{
    private static final Logger log = Logger.getLogger(NettySerializer.class.getName());

    public void writeToChannel(ChannelHandlerContext ctx, Object o) {
        ByteBuf byteBuf = ctx.alloc().buffer(4096);
        byteBuf.resetWriterIndex();
        try {
            final byte[] bytes = serialize(o);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
            ctx.writeAndFlush(byteBuf);
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OpenKey deserializeObjectToOK(Object o) throws IOException, ClassNotFoundException {
        Object obj = beforeSerialization(o);
        if (obj instanceof OpenKey){
            OpenKey transferData =  (OpenKey) obj;
            return transferData;
        }
        return new OpenKey();
    }

    public String deserializeObjectToM(Object o) throws IOException, ClassNotFoundException {
        Object obj = beforeSerialization(o);
        if (obj instanceof String){
            return (String) obj;
        }
        return "";
    }

    public BigInteger[] deserializeObjectToEncrM(Object o)throws IOException, ClassNotFoundException {
        Object obj = beforeSerialization(o);
        if (obj instanceof BigInteger[]){
            return (BigInteger[]) obj;
        }
        BigInteger[] bi = new BigInteger[1];
        bi[0].add(BigInteger.valueOf(0));
        return bi;
    }

    private byte[] serialize(final Object o) throws IOException{
        //log.info("Object serialize..");
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(o);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private Object deserialize(final byte[] bytes) throws IOException, ClassNotFoundException {
        //log.info("Object deserialize..");
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        }
    }

    private Object beforeSerialization (Object o) throws IOException, ClassNotFoundException {
        if (o instanceof ByteBuf){
            ByteBuf byteBuf = (ByteBuf) o;
            byteBuf.resetReaderIndex();
            final int messageLength = byteBuf.readInt();
            log.info(String.valueOf(messageLength));
            byte byteArray[] = new byte[messageLength];
            byteBuf.readBytes(byteArray, 0, messageLength);
            return deserialize(byteArray);
        }
        return new Object();
    }
}
