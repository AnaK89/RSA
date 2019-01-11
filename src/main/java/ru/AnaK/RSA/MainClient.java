package ru.AnaK.RSA;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MainClient {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client(BigInteger.valueOf(5), BigInteger.valueOf(3));
        client.run();
    }
}
