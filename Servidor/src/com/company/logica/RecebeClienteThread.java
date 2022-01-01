package com.company.logica;


import com.company.logica.dao.ServidorDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import static com.company.typos.Typos.*;

public class RecebeClienteThread extends Thread{

    private ObjectOutputStream oOS;
    private ObjectInputStream oIS;
    private ServerSocket serverSocket;
    ServidorDAO gestorDeSociosDAO;

    public RecebeClienteThread(ServerSocket serverSocket, ServidorDAO gestorDeSociosDAO) {
        this.gestorDeSociosDAO = gestorDeSociosDAO;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            //ServerSocket serverSocket = new ServerSocket(serverPort);
            boolean stop = false;
            Request request;
            while (!stop) {
                var socket = serverSocket.accept();
                try{
                    oOS = new ObjectOutputStream(socket.getOutputStream());
                    oIS = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                request = (Request)oIS.readObject();
                Utilizador utilizador = (Utilizador) request.getConteudo();

                if(request.getMessageCode().equals(LOGIN_REQUEST)){
                    if(login(utilizador)) {
                        try {
                            request = new Request(LOGIN_ACCEPTED, null);

                            oOS.writeObject(request);
                        } catch (IOException e) {
                            System.out.println(e + "1");
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            request = new Request(LOGIN_NOT_ACCEPTED, null);

                            oOS.writeObject(request);
                        } catch (IOException e) {
                            System.out.println(e + "2");
                            e.printStackTrace();
                        }
                    }
                }else{
                    if(registaUtilizador(utilizador)) {
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

    private boolean registaUtilizador(Utilizador utilizador) {
        return false;
    }

    private boolean login(Utilizador utilizador){
        return gestorDeSociosDAO.efetuaLogin(utilizador.getUsername(),utilizador.getPassword());
    }
}
