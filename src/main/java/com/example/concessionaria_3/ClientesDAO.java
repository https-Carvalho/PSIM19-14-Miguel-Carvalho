package com.example.concessionaria_3;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ClientesDAO {

    public static int adicionarCliente(Clientes clientes) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        int id_cliente= 0;

        String sql = "INSERT INTO clientes (nome, contato) VALUES (?, ?);";

        try {
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, clientes.getNome());
            stmt.setString(2, clientes.getContato());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id_cliente = rs.getInt(1);
            }
            System.out.println("Cliente adicionado com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao adicionar novo cliente: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
        return id_cliente;
    }

    public static ObservableList<Clientes> listarClientes() {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ObservableList<Clientes> clientes = Settings.getClients();

        String sql = "SELECT * FROM clientes;";

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id_cliente = rs.getInt("id_cliente");
                String nome = rs.getString("nome");
                String contato = rs.getString("contato");
                clientes.add(new Clientes(id_cliente, nome, contato));
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar os clientes: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt, rs);
        }
        return clientes;
    }

    public static Clientes buscarClientePorID(int id_cliente) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Clientes cliente = null;

        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_cliente);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String contato = rs.getString("contato");
                cliente = new Clientes(id_cliente, nome, contato);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao buscar cliente por ID: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt, rs);
        }
        return cliente;
    }

    public static void removerCliente(int id_cliente) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;

        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_cliente);
            stmt.executeUpdate();
            System.out.println("Cliente removido com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao remover cliente: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
    }

    public static void atualizarCliente(Clientes cliente) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;

        String sql = "UPDATE clientes SET nome = ?, contato = ? WHERE id_cliente = ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getContato());
            stmt.setInt(3, cliente.getIdCliente());
            stmt.executeUpdate();
            System.out.println("Cliente atualizado com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar cliente: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
    }
}
