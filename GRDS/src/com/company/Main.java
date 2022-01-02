package com.company;

import com.company.logica.RecebeClienteThread;
import com.company.logica.RecebeServidorThread;
import com.company.logica.Servidor;

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
