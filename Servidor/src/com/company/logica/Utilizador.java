package com.company.logica;

import java.io.Serializable;

public class Utilizador extends Conteudo implements Serializable {
    static final long serialVersionUID = 42L;
    private String nome;
    private String username;
    private String password;

    public Utilizador(String nome, String username, String password) {
        this.nome = nome;
        this.username = username;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
