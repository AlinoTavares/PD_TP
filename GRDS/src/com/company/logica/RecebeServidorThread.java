package com.company.logica;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
        Request request = null;
        Servidor servidor = null;

        try{
            socket = new DatagramSocket(PORTO_ESCURA_SERVIDOR);

            while(true){
                packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                socket.receive(packet);

                var bin = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                var oin = new ObjectInputStream(bin);

                request = (Request)oin.readObject();

                System.out.println("Recebido \"" + request.getMessageCode() + "\" de " +
                        packet.getAddress().getHostAddress() + ":" + packet.getPort());

                if(!request.getMessageCode().equalsIgnoreCase(SERVER_REQUEST)){
                    continue;
                }

                servidor = (Servidor) request.getConteudo();
                servidores.add(servidor);

                request = new Request(CONFIRMACAO_SERVIDOR, null);

                var bout = new ByteArrayOutputStream();
                var oout = new ObjectOutputStream(bout);

                oout.writeObject(request);
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
}
