package ru.AnaK.RSA.model.RSA;

import java.io.Serializable;
import java.math.BigInteger;

public class OpenKey implements Serializable {
    private BigInteger n;
    private BigInteger e;
    private boolean status = false;

    public OpenKey(BigInteger n, BigInteger e){
        this.n = n;
        this.e = e;
        status = true;
    }

    public OpenKey(){}

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }

    public boolean getStatus() {
        return status;
    }
}
