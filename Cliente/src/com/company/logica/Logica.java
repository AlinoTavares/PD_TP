package com.company.logica;

import com.company.logica.dados.Servidor;
import com.company.logica.dados.Utilizador;
import com.company.logica.mensagem.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.company.typos.Typos.*;
import static com.company.typos.Typos.REGIST_ACCEPTED;

public class Logica {
    private Servidor servidor = null;
    private Utilizador utilizador = null;

    private Socket socket = null;
    private ObjectOutputStream oOS;
    private ObjectInputStream oIS;

    private boolean continua;


    public Logica(String ip, String porto) {

        continua = getServidor(ip, porto);
    }


    private boolean getServidor(String ip, String porto){
        servidor = new UDP().getServidor(ip, porto);

        if(servidor == null){
            //System.out.println("Não foi possivel conectar-se a um servidor.");
            return false;
        }

        try{
            socket = new Socket(servidor.getIp(), servidor.getPort());

            oOS = new ObjectOutputStream(socket.getOutputStream());
            oIS = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean loginUtilizador(String nome, String username, String pass) {
        if(!continua)
            return false;

        Request request = null;

        utilizador = new Utilizador(nome, username, pass);

        try {
            request = new Request(LOGIN_REQUEST, utilizador);

            oOS.writeObject(request);
        } catch (IOException e) {
            System.out.println(e + "_MAIN_8");
            e.printStackTrace();
        }

        try {
            request = (Request) oIS.readObject();
            if (request.getMessageCode().equals(LOGIN_ACCEPTED)) {
                return true;
            }else{
                System.out.println("LOGIN_NOT_ACCEPTED");
            }

        } catch (IOException e) {
            System.out.println(e + "_MAIN_9");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e + "_MAIN_10");
            e.printStackTrace();
        }

        return false;
    }

    public boolean registaUtilizador(String nome, String username, String pass) {
        if(!continua)
            return false;

        Request request = null;

        utilizador = new Utilizador(nome, username, pass);

        try {
            request = new Request(REGIST_REQUEST, utilizador);

            oOS.writeObject(request);
        } catch (IOException e) {
            System.out.println(e + "_MAIN_8");
            e.printStackTrace();
        }

        try {
            request = (Request) oIS.readObject();
            if (request.getMessageCode().equals(REGIST_ACCEPTED)) {
                return true;
            }

        } catch (IOException e) {
            System.out.println(e + "_MAIN_9");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println(e + "_MAIN_10");
            e.printStackTrace();
        }

        return false;
    }

    public boolean isContinua() {
        return continua;
    }
}
