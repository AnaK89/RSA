package ru.AnaK.RSA;

import java.math.BigInteger;

public class MainServer {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(BigInteger.valueOf(5), BigInteger.valueOf(3));
        server.run();
    }
}
