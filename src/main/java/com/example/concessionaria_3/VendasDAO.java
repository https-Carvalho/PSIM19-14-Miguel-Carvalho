package com.example.concessionaria_3;

import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class VendasDAO {
    public static int adicionarVenda(Vendas venda, int idCarro) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id_venda = 0;

        String sqlCheckCarro = "SELECT COUNT(*) FROM vendas WHERE id_carro = ?";
        String sqlVenda = "INSERT INTO vendas (data_venda, valor, id_carro) VALUES (?, ?, ?)";
        String sqlCarro = "UPDATE carros SET status = ? WHERE id_carro = ?";

        try {
            // Iniciar transação
            conn.setAutoCommit(false);

            // Verificar se o carro já foi vendido
            stmt = conn.prepareStatement(sqlCheckCarro);
            stmt.setInt(1, idCarro);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Erro: O carro já foi vendido.");
                conn.rollback();
                return 0; // Indica que a venda não foi adicionada
            }

            // Inserir a venda
            stmt = conn.prepareStatement(sqlVenda, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, Date.valueOf(venda.getDataVenda()));
            stmt.setDouble(2, venda.getValor());
            stmt.setInt(3, idCarro);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id_venda = rs.getInt(1);
            }

            // Atualizar o status do carro
            stmt = conn.prepareStatement(sqlCarro);
            stmt.setString(1, "vendido");
            stmt.setInt(2, idCarro);
            stmt.executeUpdate();

            // Confirmar transação
            conn.commit();
            System.out.println("Venda adicionada e status do carro atualizado com sucesso");
        } catch (SQLException ex) {
            try {
                // Reverter transação em caso de erro
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Erro ao adicionar venda e atualizar status do carro: " + ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                conn.setAutoCommit(true); // Reverter para o modo de confirmação automática
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id_venda;
    }

    public static ObservableList<Vendas> listarVendas() {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ObservableList<Vendas> vendas = Settings.getVendas();

        String sql = "SELECT * FROM vendas";

        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id_venda = rs.getInt("id_venda");
                LocalDate data_venda = rs.getDate("data_venda").toLocalDate();
                double valor = rs.getDouble("valor");
                int id_carro = rs.getInt("id_carro");
                vendas.add(new Vendas(id_venda, data_venda, valor, id_carro));
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao listar vendas: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt, rs);
        }
        return vendas;
    }

    public static void removerVenda(int idVenda) {
        Connection conn = null;
        PreparedStatement stmtVenda = null;
        PreparedStatement stmtCarro = null;

        String sqlVenda = "DELETE FROM vendas WHERE id_venda = ?";
        String sqlCarro = "UPDATE carros SET status = 'disponível' WHERE id_carro = (SELECT id_carro FROM vendas WHERE id_venda = ?)";

        try {
            conn = ConexaoDB.openDB();
            conn.setAutoCommit(false); // Iniciar transação

            // Atualizar status do carro
            stmtCarro = conn.prepareStatement(sqlCarro);
            stmtCarro.setInt(1, idVenda);
            stmtCarro.executeUpdate();

            // Remover a venda
            stmtVenda = conn.prepareStatement(sqlVenda);
            stmtVenda.setInt(1, idVenda);
            stmtVenda.executeUpdate();

            // Confirmar transação
            conn.commit();
            System.out.println("Venda removida e status do carro atualizado para disponível com sucesso");
        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback(); // Reverter transação em caso de erro
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Erro ao remover venda e atualizar status do carro: " + ex);
        } finally {
            if (stmtVenda != null) {
                try {
                    stmtVenda.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmtCarro != null) {
                try {
                    stmtCarro.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reverter para o modo de confirmação automática
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void atualizarVenda(Vendas vendas) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;

        String sql = "UPDATE vendas SET data_venda = ?, valor = ? WHERE id_venda = ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(vendas.getDataVenda()));
            stmt.setDouble(2, vendas.getValor());
            stmt.setInt(3, vendas.getIdVenda());
            stmt.executeUpdate();
            System.out.println("Venda atualizada com sucesso");
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar venda: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt);
        }
    }

    public static boolean isCarroVendido(int idCarro) {
        Connection conn = ConexaoDB.openDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isVendido = false;

        String sql = "SELECT COUNT(*) FROM vendas WHERE id_carro = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCarro);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                isVendido = true;
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao verificar se o carro foi vendido: " + ex);
        } finally {
            ConexaoDB.closeDB(stmt, rs);
        }

        return isVendido;
    }
}