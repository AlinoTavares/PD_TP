package com.company;

import java.util.List;
import java.io.*;
import java.net.*;

public class Main {
    public static final int MAX_SIZE = 256;
    public static final String SERVER_REQUEST = "SERVER_REQUEST";
    public static final int PORTO_ESCURA_CLIENTE = 6000;

    //private List<Servidor> servidores;


    public static void main(String[] args) {
        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet;
        String receivedMsg;



        try{

            socket = new DatagramSocket(PORTO_ESCURA_CLIENTE);

            while(true){
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                var bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                var oin = new ObjectInputStream(bin);

                receivedMsg = (String)oin.readObject();

                System.out.println("Recebido \"" + receivedMsg + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if(!receivedMsg.equalsIgnoreCase(SERVER_REQUEST)){
                    continue;
                }

            }

        }catch(NumberFormatException e){
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        }catch(SocketException e){
            System.out.println("Ocorreu um erro ao nivel do socket UDP:\n\t"+e);
        }catch(IOException e){
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t"+e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(socket != null){
                socket.close();
            }
        }
    }

    private int proximoServidor(){
        return 0;
    }
}
