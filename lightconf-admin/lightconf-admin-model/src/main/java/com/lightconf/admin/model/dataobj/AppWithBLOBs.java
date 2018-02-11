package com.lightconf.admin.model.dataobj;

import java.io.Serializable;

public class AppWithBLOBs extends App implements Serializable {
    private String privateKey;

    private String publicKey;

    private static final long serialVersionUID = 1L;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey == null ? null : privateKey.trim();
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey == null ? null : publicKey.trim();
    }
}