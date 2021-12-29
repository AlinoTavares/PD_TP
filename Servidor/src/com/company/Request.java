package com.company;

import java.io.Serializable;

public class Request implements Serializable {
    static final long serialVersionUID = 42L;
    private String messageCode;
    private Conteudo conteudo;

    public Request(String messageCode, Conteudo conteudo) {
        this.messageCode = messageCode;
        this.conteudo = conteudo;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }
}
