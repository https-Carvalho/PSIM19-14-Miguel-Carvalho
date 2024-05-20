package com.example.concessionaria_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {


    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public void buttonLogin(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();

        if (validateLogin(user, pass)) {
            // Login bem-sucedido, redirecionar para a página inicial
            redirectToHomePage();
        } else {
            // Login falhou, mostrar alerta
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Login");
            alert.setHeaderText("Nome de usuário ou senha inválidos");
            alert.setContentText("Por favor, tente novamente.");
            alert.showAndWait();
        }
    }

    private boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM utilizadores WHERE username = ? AND password = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Retorna verdadeiro se um resultado for encontrado

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void redirectToHomePage() {
        try {
            // Carregar a nova cena
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_page.fxml"));
            Parent root = loader.load();

            // Obter o estágio atual
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Concessionaria");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void buttonSignUp(ActionEvent actionEvent) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("Signup.fxml"));

        // Nova janela
        Stage SignUp = new Stage();
        SignUp.setTitle("Lista de Clientes - Remover Cliente");

        // Associação da Scene à Stage
        SignUp.setScene(new Scene(scene));

        // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
        SignUp.initOwner(Settings.getPrimaryStage());
        SignUp.initModality(Modality.WINDOW_MODAL);

        // Abertura da Window
        SignUp.show();
    }
}

