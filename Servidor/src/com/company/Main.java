package com.company;

import com.company.logica.ServidorLogica;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        ServidorLogica servidorLogica;

        InetAddress GRDS_Addr = null;
        int GRDS_Port = -1;

        if (args.length != 2) {
            System.out.println("Sintaxe: java cliente GRDS_Address GRDS_Port");
            return;
        }

        try {
            GRDS_Addr = InetAddress.getByName(args[0]);
            GRDS_Port = Integer.parseInt(args[1]);
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(GRDS_Addr!=null || GRDS_Port != -1)
                servidorLogica = new ServidorLogica(GRDS_Addr, GRDS_Port);
            else
                System.out.println("ERRO argumentos argv");

            System.out.println(GRDS_Addr.getHostAddress().toString()+" "+ GRDS_Port);
        }
    }
}
