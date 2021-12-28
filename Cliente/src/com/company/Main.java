package com.company;

import java.io.*;
import java.net.*;

public class Main {

    public static final String SERVER_REQUEST = "SERVER_REQUEST";

    public static void main(String[] args) {
        InetAddress GRDS_Addr;
        int GRDS_Port = -1;
        DatagramPacket packet;
        DatagramSocket socketUDP = null;


        if (args.length != 2) {
            System.out.println("Sintaxe: java cliente GRDS_Address GRDS_Port");
            return;
        }

        try {
            GRDS_Addr = InetAddress.getByName(args[0]);
            GRDS_Port = Integer.parseInt(args[1]);

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
                oout.writeObject(SERVER_REQUEST);
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

        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t" + e);

        } finally {
            if (socketUDP != null) {
                socketUDP.close();
            }
        }
    }
}
