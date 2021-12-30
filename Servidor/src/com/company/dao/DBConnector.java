package com.company.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnector {

    private static Connection connect = null;

    public static Connection  connection() {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pd","root","1234");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        System.out.println("Conectado com sucesso");
        return connect;
    }

}