package com.company;

import java.io.*;
import java.net.*;

import static com.company.typos.Typos.MAX_SIZE;
import static com.company.typos.Typos.SERVER_REQUEST;

public class Main {

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

            String confirmacao = null;
            try {
                confirmacao = (String)oin.readObject();
            } catch (IOException e) {
                System.out.println(e + "_MAIN_7");
                e.printStackTrace();
            }
            System.out.println(confirmacao);


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
    }

}
