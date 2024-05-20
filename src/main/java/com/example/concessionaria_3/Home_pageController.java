package com.example.concessionaria_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class Home_pageController {

    @FXML
    private BorderPane borderPane;
    public void MenuClientes(ActionEvent actionEvent) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("MenuClientes.fxml"));
        borderPane.setCenter(scene);
    }

    public void MenuCarros(ActionEvent actionEvent) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("MenuCarros.fxml"));
        borderPane.setCenter(scene);
    }

    public void MenuVendas(ActionEvent actionEvent) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource("MenuVendas.fxml"));
        borderPane.setCenter(scene);

    }
}
