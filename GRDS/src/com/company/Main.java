package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.net.*;

import static com.company.typos.Typos.*;


public class Main {
    private static List<Servidor> servidores = new ArrayList<>();

    public static void main(String[] args) {

        RecebeServidorThread recebeServidorThread = new RecebeServidorThread(servidores);
        recebeServidorThread.start();

        RecebeClienteThread recebeClienteThread = new RecebeClienteThread(servidores);
        recebeClienteThread.start();
    }

}
