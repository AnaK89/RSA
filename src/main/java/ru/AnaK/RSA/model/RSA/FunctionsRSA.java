package ru.AnaK.RSA.model.RSA;

import java.math.BigInteger;

public class FunctionsRSA {
    private final BigInteger p;
    private final BigInteger q;

    public FunctionsRSA (BigInteger p, BigInteger q){
        this.p = p;
        this.q = q;
    }

    public PairKeys generatePairKeys (){
        final BigInteger n = calculateN();
        final BigInteger f = calculateF();
        final BigInteger e = calculateE(f);
        final BigInteger d = calculateD(e, f);
        return new PairKeys(new OpenKey(n, e), new CloseKey(n, d));
    }

    public BigInteger[] encrypt(OpenKey openKey, String message){
        final char[] chars = message.toCharArray();
        final BigInteger[] encodedMessage = new BigInteger[chars.length];
        for (int i = 0; i < chars.length; i++) {
            final BigInteger encodedChar = BigInteger.valueOf((int) chars[i]).modPow(openKey.getE(), openKey.getN());
            encodedMessage[i] = encodedChar;
        }
        return encodedMessage;
    }

    public String decrypt(CloseKey closeKey, BigInteger[] message){
        final StringBuilder decodedMessage = new StringBuilder();
        for (final BigInteger code : message) {
            final BigInteger decodedChar = code.modPow(closeKey.getD(), closeKey.getN());
            decodedMessage.append((char) decodedChar.intValue());
        }
        return decodedMessage.toString();
    }

    private BigInteger calculateN(){
        return p.multiply(q);
    }

    private BigInteger calculateF(){
        return (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    }

    private BigInteger calculateE(BigInteger f){
        BigInteger e = BigInteger.valueOf(3);
        while (f.gcd(e).intValue() > 1) {
            e.add(BigInteger.valueOf(2));
        }
        return e;
    }

    private BigInteger calculateD(BigInteger e, BigInteger f){
        return e.modInverse(f);
    }
}
