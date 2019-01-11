package ru.AnaK.RSA.model.RSA;

import java.math.BigInteger;

public class CloseKey {
    private final BigInteger n;
    private final BigInteger d;

    public CloseKey(BigInteger n, BigInteger d){
        this.n = n;
        this.d = d;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getD() {
        return d;
    }
}
