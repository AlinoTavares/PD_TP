package com.company.logica;

import com.company.logica.dados.Servidor;
import com.company.logica.mensagem.Request;

import java.io.*;
import java.net.*;

import static com.company.typos.Typos.*;

public class UDP {
    private InetAddress GRDS_Addr;
    private int GRDS_Port = -1;
    private DatagramPacket packet;
    private DatagramSocket socketUDP = null;

    private Servidor servidor = null;
    private Request request = null;

    public Servidor getServidor(String ip, String port){

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
