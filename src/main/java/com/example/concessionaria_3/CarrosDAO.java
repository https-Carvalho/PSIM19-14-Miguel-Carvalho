package com.example.concessionaria_3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class CarrosDAO {
    public static int adicionarCarro(Carros carro) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        int id_carro = 0;

        String sql = "INSERT INTO carros (marca, modelo, preco, data_registo) VALUES (?, ?, ?, ?)";

        try {
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, carro.getMarca());
            stmt.setString(2, carro.getModelo());
            stmt.setDouble(3, carro.getPreco());
            stmt.setDate(4, Date.valueOf(carro.getDataRegistro()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id_carro = rs.getInt(1);
            }
            System.out.println("Carro adicionado com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao adicionar novo carro: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
        return id_carro;
    }

    public static ObservableList<Carros> listarCarros() {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ObservableList<Carros> carros = Settings.getCars();

        String sql = "SELECT * FROM carros";

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id_carro = rs.getInt("id_carro");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                double preco = rs.getDouble("preco");
                LocalDate dataRegistro = rs.getDate("data_registo").toLocalDate();
                String status = rs.getString("status");  // Obtém o status do carro
                carros.add(new Carros(id_carro, marca, modelo, dataRegistro, preco, status));
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar carros: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt, rs);
        }
        return carros;
    }

    public static void removerCarro(int id) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;

        String sql = "DELETE FROM carros WHERE id_carro = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Carro removido com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao remover carro: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
    }

    public static void atualizarCarro(Carros carro) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;

        String sql = "UPDATE carros SET marca = ?, modelo = ?, preco = ?, data_registo = ? WHERE id_carro = ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, carro.getMarca());
            stmt.setString(2, carro.getModelo());
            stmt.setDouble(3, carro.getPreco());
            stmt.setDate(4, Date.valueOf(carro.getDataRegistro()));
            stmt.setInt(5, carro.getIdCarro());
            stmt.executeUpdate();
            System.out.println("Carro atualizado com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar carro: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
    }
}
