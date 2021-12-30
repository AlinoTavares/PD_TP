package com.company;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static com.company.typos.Typos.*;

public class Main {
    private static Servidor servidor = null;
    private Utilizador utilizador = null;

    private static Socket socket = null;
    private static ObjectOutputStream oOS;
    private static ObjectInputStream oIS;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Sintaxe: java cliente GRDS_Address GRDS_Port");
            return;
        }

        servidor = getServidor(args[0], args[1]);

        if(servidor == null){
            System.out.println("Não foi possivel conectar-se a um servidor.");
            return;
        }

        try{
            socket = new Socket(servidor.getIp(), servidor.getPort());

            oOS = new ObjectOutputStream(socket.getOutputStream());
            oIS = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private static boolean loginUtilizador() {
        Scanner sc = new Scanner(System.in);
        String nome, username, pass;
        Utilizador utilizador = null;
        Request request = null;


        System.out.println("Introduza o seu nome:");
        nome = sc.nextLine();
        System.out.println("Introduza o seu username:");
        username = sc.next();
        System.out.println("Introduza a sua password:");
        pass = sc.next();

        utilizador = new Utilizador(nome, username, pass);

        try {
            request = new Request(LOGIN_REQUEST, utilizador);

            oOS.writeObject(request);
        } catch (IOException e) {
            System.out.println(e + "_MAIN_8");
            e.printStackTrace();
        }

        try {
            request = (Request) oIS.readObject();
            if (request.getMessageCode().equals(LOGIN_ACCEPTED)) {
                return true;
            }else{
                System.out.println("LOGIN_NOT_ACCEPTED");
            }

        } catch (IOException e) {
            System.out.println(e + "_MAIN_9");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e + "_MAIN_10");
            e.printStackTrace();
        }

        return false;
    }

    private static boolean registaUtilizador() {
        Scanner sc = new Scanner(System.in);
        String nome, username, pass;
        Utilizador utilizador = null;
        Request request = null;


        System.out.println("Introduza o seu nome:");
        nome = sc.nextLine();
        System.out.println("Introduza o seu username:");
        username = sc.next();
        System.out.println("Introduza a sua password:");
        pass = sc.next();

        utilizador = new Utilizador(nome, username, pass);

        try {
            request = new Request(REGIST_REQUEST, utilizador);

            oOS.writeObject(request);
        } catch (IOException e) {
            System.out.println(e + "_MAIN_8");
            e.printStackTrace();
        }

        try {
            request = (Request) oIS.readObject();
            if (request.getMessageCode().equals(REGIST_ACCEPTED)) {
                return true;
            }

        } catch (IOException e) {
            System.out.println(e + "_MAIN_9");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e + "_MAIN_10");
            e.printStackTrace();
        }

        return false;
    }

    private static Servidor getServidor(String ip, String port){
        InetAddress GRDS_Addr;
        int GRDS_Port = -1;
        DatagramPacket packet;
        DatagramSocket socketUDP = null;

        Servidor servidor = null;
        Request request = null;


        try {
            GRDS_Addr = InetAddress.getByName(ip);
            GRDS_Port = Integer.parseInt(port);

            socketUDP = new DatagramSocket();

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream oout = null;
            try {
                oout = new ObjectOutputStream(bout);
            } catch (IOException e) {
                System.out.println(e + "_MAIN_1");
                e.printStackTrace();
            }
            try {
                request = new Request(SERVER_REQUEST, null);
                oout.writeObject(request);
            } catch (IOException e) {
                System.out.println(e + "_MAIN_2");
                e.printStackTrace();
            }
            try {
                oout.flush();
            } catch (IOException e) {
                System.out.println(e + "_MAIN_3");
                e.printStackTrace();
            }

            packet = new DatagramPacket(bout.toByteArray(), bout.size(), GRDS_Addr, GRDS_Port);

            try {
                socketUDP.send(packet);
            } catch (IOException e) {
                System.out.println(e + "_MAIN_4");
                e.printStackTrace();
            }

            packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
            try {
                socketUDP.receive(packet);
            } catch (IOException e) {
                System.out.println(e + "_MAIN_5");
                e.printStackTrace();
            }

            var bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream oin = null;
            try {
                oin = new ObjectInputStream(bin);
            } catch (IOException e) {
                System.out.println(e + "_MAIN_6");
                e.printStackTrace();
            }


            try {
                request = (Request) oin.readObject();
                if(request.getMessageCode().equals(NO_SERVER_AVAILABLE)){
                    return null;
                }
                servidor = (Servidor) request.getConteudo();
            } catch (IOException e) {
                System.out.println(e + "_MAIN_7");
                e.printStackTrace();
            }
            System.out.println(servidor.getIp() + "    " + servidor.getPort());


        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (socketUDP != null) {
                socketUDP.close();
            }
        }

        return servidor;
    }
}
