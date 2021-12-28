package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;

import static com.company.typos.Typos.*;

public class RecebeServidorThread extends Thread{
    private List<Servidor> servidores;

    public RecebeServidorThread(List<Servidor> servidores) {
        this.servidores = servidores;
    }

    @Override
    public void run() {
        int listeningPort;
        DatagramSocket socket = null;
        DatagramPacket packet;
        String receivedMsg;
        try{
            socket = new DatagramSocket(PORTO_ESCURA_SERVIDOR);

            while(true){
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                var bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                var oin = new ObjectInputStream(bin);

                receivedMsg = (String)oin.readObject();

                System.out.println("Recebido \"" + receivedMsg + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                addNovoServidor(packet.getAddress().getHostAddress().toString(), packet.getPort());

                if(!receivedMsg.equalsIgnoreCase(SERVER_REQUEST)){
                    continue;
                }

                var confirmacao = CONFIRMACAO_SERVIDOR;

                var bout = new ByteArrayOutputStream();
                var oout = new ObjectOutputStream(bout);

                oout.writeObject(confirmacao);
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

    private void addNovoServidor(String ip, int porto){
        Servidor servidor = new Servidor(ip,porto);
        servidores.add(servidor);
    }
}
