package com.company.logica;

import java.util.ArrayList;
import java.util.List;


public class Main {
    private static List<Servidor> servidores = new ArrayList<>();

    public static void main(String[] args) {

        RecebeServidorThread recebeServidorThread = new RecebeServidorThread(servidores);
        recebeServidorThread.start();

        RecebeClienteThread recebeClienteThread = new RecebeClienteThread(servidores);
        recebeClienteThread.start();
    }

}
