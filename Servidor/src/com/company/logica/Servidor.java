package com.company.logica;

import com.company.logica.Conteudo;

import java.io.Serializable;

public class Servidor extends Conteudo implements Serializable {
    static final long serialVersionUID = 42L;
    private String ip;
    private int port;

    public Servidor(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}