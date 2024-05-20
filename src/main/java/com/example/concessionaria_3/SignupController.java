package com.example.concessionaria_3;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private Stage Anchorpane;
    @FXML
    public Button btnSignUp;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;


    private void inserir(String username, String password) throws SQLException {
        // Configure a conexão com seu banco de dados

        String query = "INSERT INTO utilizadores (username, password) VALUES (?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Query para inserir o novo usuário na tabela
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Lidar com exceções, por exemplo, mostrar uma mensagem de erro
            e.printStackTrace();
        }
    }

    public void ButtonSignUp(ActionEvent actionEvent) throws SQLException, IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // Aqui você pode inserir esses dados na tabela de usuários em seu banco de dados
        inserir(username, password);

        // Após inserir, você pode limpar os campos ou mostrar uma mensagem de sucesso
        txtUsername.clear();
        txtPassword.clear();
        Anchorpane = (Stage) btnSignUp.getScene().getWindow();
        Anchorpane.close();


    }
}
