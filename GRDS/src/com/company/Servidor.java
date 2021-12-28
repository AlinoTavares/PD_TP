package com.company;

import java.io.Serializable;

public class Servidor implements Serializable {
    static final long serialVersionUID = 42L;
    private String ip;
    private int port;

    public Servidor(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
