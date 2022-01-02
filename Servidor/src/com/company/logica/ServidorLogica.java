package com.company.logica;

import com.company.logica.comunicacao.LigacaoClienteThread;
import com.company.logica.comunicacao.LigacaoInicialGRDS;
import com.company.logica.comunicacao.RecebeClienteThread;
import com.company.logica.comunicacao.RecebeDatagramGRDSThread;
import com.company.logica.dao.ServidorDAO;
import com.company.logica.entidade.Servidor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;

public class ServidorLogica {
    private LigacaoInicialGRDS ligacaoInicialGRDS;

    private List<LigacaoClienteThread> ligacaoClienteThread;

    private RecebeClienteThread recebeClienteThread;
    private RecebeDatagramGRDSThread recebeDatagramGRDSThread;

    private ServerSocket serverSocket;

    private ServidorDAO servidorDAO;

    private Servidor servidor;

    public ServidorLogica(InetAddress GRDS_Addr, int GRDS_Port) {
        servidorDAO= new ServidorDAO();

        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        servidor = new Servidor("localhost", serverSocket.getLocalPort());
        ligacaoInicialGRDS = new LigacaoInicialGRDS(GRDS_Addr, GRDS_Port,servidor);
        ligacaoInicialGRDS.pedidoLigacaoGRDS();

        recebeClienteThread = new RecebeClienteThread(serverSocket, servidorDAO);
    }
}
