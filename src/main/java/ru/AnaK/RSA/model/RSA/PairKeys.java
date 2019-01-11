package ru.AnaK.RSA.model.RSA;

public class PairKeys {
    private final OpenKey openKey;
    private final CloseKey closeKey;

    public PairKeys(OpenKey openKey, CloseKey closeKey){
        this.openKey = openKey;
        this.closeKey = closeKey;
    }

    public OpenKey getOpenKey() {
        return openKey;
    }

    public CloseKey getCloseKey() {
        return closeKey;
    }
}
