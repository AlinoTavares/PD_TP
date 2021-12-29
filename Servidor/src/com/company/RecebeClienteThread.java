package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.company.typos.Typos.*;

public class RecebeClienteThread extends Thread{

    private int serverPort;
    private ObjectOutputStream oOS;
    private ObjectInputStream oIS;
    private List<Utilizador> utilizadores;

    public RecebeClienteThread(int serverPort) {
        this.serverPort = serverPort;
        utilizadores = new ArrayList<>();
        Utilizador utilizador = new Utilizador("alino","alino","alino");
        utilizadores.add(utilizador);
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            boolean stop = false;
            while (!stop) {
                var socket = serverSocket.accept();
                try{
                    oOS = new ObjectOutputStream(socket.getOutputStream());
                    oIS = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                var request = (Request)oIS.readObject();
                Utilizador utilizador = (Utilizador) request.getConteudo();

                if(verificaUtilizador(utilizador)) {
                    try {
                        request = new Request(REGIST_ACCEPTED, utilizador);

                        oOS.writeObject(request);
                    } catch (IOException e) {
                        System.out.println(e + "1");
                        e.printStackTrace();
                    }
                }else{
                    try {
                        request = new Request(REGIST_NOT_ACCEPTED, utilizador);

                        oOS.writeObject(request);
                    } catch (IOException e) {
                        System.out.println(e + "2");
                        e.printStackTrace();
                    }
                }

                //fechar a socket tambem feicha os streams de input e output
                socket.close();
            }
            //fecha-se tambem o server socket
            serverSocket.close();

            //tratamento de erros
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean verificaUtilizador(Utilizador utilizador){
        for(var utiAux: utilizadores){

            if(utilizador.getNome().equals(utilizador.getNome()) && utilizador.getPassword().equals(utilizador.getPassword()) && utilizador.getUsername().equals(utilizador.getUsername())){
                return true;
            }
        }
        return false;
    }
}
