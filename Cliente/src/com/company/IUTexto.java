package com.company;

import com.company.logica.Logica;

import java.util.Scanner;

public class IUTexto {
    Logica cliente;

    public IUTexto(Logica cliente) {
        this.cliente = cliente;
    }

    public void start(){
        int aux;
        Scanner sc = new Scanner(System.in);
        boolean sair = false;
        while(!sair) {
            System.out.println("1 - Login");
            System.out.println("2 - Registar");
            System.out.println("3 - Sair");

            aux = sc.nextInt();

            switch (aux) {
                case 1:
                    sair = loginUtilizador();
                    break;
                case 2:
                    sair = registaUtilizador();
                    break;
                case 3:
                    sair = true;
                    break;
                default:
                    break;
            }
        }
    }

    private boolean registaUtilizador() {
        Scanner sc = new Scanner(System.in);
        String nome, username,pass;

        System.out.println("Introduza o seu nome:");
        nome = sc.nextLine();
        System.out.println("Introduza o seu username:");
        username = sc.next();
        System.out.println("Introduza a sua password:");
        pass = sc.next();

        return cliente.registaUtilizador(nome, username, pass);
    }

    private boolean loginUtilizador() {
        Scanner sc = new Scanner(System.in);
        String nome, username,pass;

        System.out.println("Introduza o seu nome:");
        nome = sc.nextLine();
        System.out.println("Introduza o seu username:");
        username = sc.next();
        System.out.println("Introduza a sua password:");
        pass = sc.next();

        return cliente.loginUtilizador(nome, username, pass);
    }


}
