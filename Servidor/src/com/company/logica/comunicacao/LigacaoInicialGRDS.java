package com.company.logica.comunicacao;

import com.company.logica.entidade.Request;
import com.company.logica.entidade.Servidor;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static com.company.typos.Typos.MAX_SIZE;
import static com.company.typos.Typos.SERVER_REQUEST;

public class LigacaoInicialGRDS {

    private InetAddress GRDS_Addr;
    private int GRDS_Port = -1;
    private DatagramPacket packet;
    private DatagramSocket socketUDP = null;
    private Servidor servidor;

    public LigacaoInicialGRDS(InetAddress GRDS_Addr, int GRDS_Port, Servidor servidor) {
        this.GRDS_Addr = GRDS_Addr;
        this.GRDS_Port = GRDS_Port;
        this.servidor = servidor;
    }

    public void pedidoLigacaoGRDS(){
        Request registaRequest;
        try {
            registaRequest = new Request(SERVER_REQUEST, servidor);
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
                oout.writeObject(registaRequest);
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

            Request confirmacao = null;
            try {
                confirmacao = (Request) oin.readObject();
            } catch (IOException e) {
                System.out.println(e + "_MAIN_7");
                e.printStackTrace();
            }
            System.out.println(confirmacao.getMessageCode());
        } catch (SocketException e) {
                e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
