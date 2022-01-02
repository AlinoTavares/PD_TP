package com.company.logica;

import com.company.IUTexto;
import com.company.logica.dados.Servidor;
import com.company.logica.dados.Utilizador;
import com.company.logica.mensagem.Request;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static com.company.typos.Typos.*;

public class Main {


    public static void main(String[] args) {
        IUTexto iutexto;
        Logica cliente;

        if (args.length != 2) {
            System.out.println("Sintaxe: java cliente GRDS_Address GRDS_Port");
            return;
        }

        cliente = new Logica(args[0], args[1]);
        iutexto = new IUTexto(cliente);

        iutexto.start();

    }



}
