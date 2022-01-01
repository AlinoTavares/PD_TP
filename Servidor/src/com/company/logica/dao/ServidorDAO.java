package com.company.logica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServidorDAO {
    private final Connection connect;
    private PreparedStatement statement;
    private ResultSet result;

    public ServidorDAO() {
        connect = DBConnector.connection();
    }

    public boolean efetuaLogin(String username, String password) {
        try {
            statement = (PreparedStatement) connect.prepareStatement("SELECT username,password FROM utilizador WHERE username = ? and password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            result = statement.executeQuery();

            if (result.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    /*
    public boolean registaCliente(Utilizador novoSocio) {
        try {
            statement = (PreparedStatement) connect.prepareStatement("INSERT INTO socios (id,nome,email,telemovel,data_inscricao,data_nascimento,morada,creditos,password)" + "VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setInt(1, novoSocio.getId());
            statement.setString(2, novoSocio.getNome());
            statement.setString(3, novoSocio.getEmail());
            statement.setInt(4, novoSocio.getNum_telemovel());
            statement.setDate(5, novoSocio.getDataInscricao());
            statement.setDate(6, novoSocio.getDataNascimento());
            statement.setString(7, novoSocio.getMorada());
            statement.setInt(8, novoSocio.getCreditos());
            statement.setString(9, novoSocio.getPassword());
            statement.execute();
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
        return true;
    }*/

    public boolean socioExiste(int id, String nome, String email) {
        try{
            statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM socios WHERE id = ?");
            statement.setInt(1, id);
            result = statement.executeQuery();

            if (result.next()) {
                return true;
            }

            statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM socios WHERE nome = ?");
            statement.setString(1, nome);
            result = statement.executeQuery();

            if (result.next()) {
                return true;
            }

            statement = (PreparedStatement) connect.prepareStatement("SELECT * FROM socios WHERE email = ?");
            statement.setString(1, email);
            result = statement.executeQuery();

            if (result.next()) {
                return true;
            }

            return false;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }


}
