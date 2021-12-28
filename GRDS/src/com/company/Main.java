package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.net.*;

import static com.company.typos.Typos.*;


public class Main {
    private static List<Servidor> servidores = new ArrayList<>();
    private static Iterator<Servidor> it;


    public static void main(String[] args) {
        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet;
        String receivedMsg;


        try{
            it = servidores.iterator();
            socket = new DatagramSocket(PORTO_ESCURA_CLIENTE);

            while(true){
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                var bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                var oin = new ObjectInputStream(bin);

                receivedMsg = (String)oin.readObject();

                //System.out.println("Recebido \"" + receivedMsg + "\" de " +
                //        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if(!receivedMsg.equalsIgnoreCase(SERVER_REQUEST)){
                    continue;
                }

                var servidor = proximoServidor();
                //System.out.println(servidor.getIp()+ "    " +servidor.getPort());

                var bout = new ByteArrayOutputStream();
                var oout = new ObjectOutputStream(bout);

                oout.writeObject(servidor);
                oout.flush();

                packet.setData(bout.toByteArray(),0, bout.size());
                packet.setLength(bout.size());


                //O ip e porto de destino ja' se encontram definidos em packet
                socket.send(packet);
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

    private static Servidor proximoServidor(){
        if(it.hasNext())
            return it.next();
        it = servidores.iterator();
        return it.next();
    }
}
