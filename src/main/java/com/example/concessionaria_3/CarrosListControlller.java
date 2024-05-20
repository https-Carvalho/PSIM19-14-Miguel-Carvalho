package com.example.concessionaria_3;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class CarrosListControlller implements Initializable {

    @FXML
    private TableView tableViewCarro;
    @FXML
    private TableColumn tableColumnID;
    @FXML
    private TableColumn tableColumnMarca;
    @FXML
    private TableColumn tableColumnModelo;
    @FXML
    private TableColumn tableColumnDataRegisto;
    @FXML
    private TableColumn tableColumnPreco;
    @FXML
    private TableColumn tableColumnStatus;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<Carros, Integer>("idCarro"));
        tableColumnMarca.setCellValueFactory(new PropertyValueFactory<Carros, String>("marca"));
        tableColumnModelo.setCellValueFactory(new PropertyValueFactory<Carros, String>("modelo"));
        tableColumnDataRegisto.setCellValueFactory(new PropertyValueFactory<Carros, LocalDate>("dataRegistro"));
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<Carros, Double>("preco"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<Carros, String>("status")); // Aqui adicionamos a configuração da nova coluna

        Settings.getCars().clear();
        tableViewCarro.setItems(CarrosDAO.listarCarros());

        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
    }
    public void buttonEditar(ActionEvent actionEvent) throws Exception{
        // Caso não haja um item selecionado notifica o Utilizador e termina.
        if(tableViewCarro.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Item não selecionado");
            alert.setHeaderText("Selecione um item, por favor!");
            alert.show();
            return;
        }

        Carros selectedItem = (Carros) tableViewCarro.getSelectionModel().getSelectedItem();

        // Definição da Flag Ation e do objeto de Entidade de settings com Update
        Settings.ACTION = Settings.ACTION_UPDATE;
        Settings.setCarsEdit(selectedItem);

        // Abre a Scene numa nova Stage em modo Modal
        // Aquisição do controlo da Scene pretendida
        Parent scene = FXMLLoader.load(getClass().getResource("Carros.fxml"));

        // Nova janela
        Stage carroEditar = new Stage();
        carroEditar.setTitle("Lista de Carros - Atualizar Carro");

        // Associação da Scene à Stage
        carroEditar.setScene(new Scene(scene));

        // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
        carroEditar.initOwner(Settings.getPrimaryStage());
        carroEditar.initModality(Modality.WINDOW_MODAL);

        // Abertura da Window
        carroEditar.show();
    }

    public void buttonEliminar(ActionEvent actionEvent) throws Exception{
        // Caso não haja um item selecionado notifica o Utilizador e termina.
        if(tableViewCarro.getSelectionModel().getSelectedItem() == null){

            // Notifica o utilizdor e termina
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Item não selecionado");
            alert.setHeaderText("Selecione um item, por favor!");
            alert.show();
            return;
        }
        // Se chegou aqui é porque há um item selecionado => Extrai-o
        // O método devolve um Object porque nunca sabe o que lá vem. => Cast para Aluno.
        Carros selectedItem = (Carros) tableViewCarro.getSelectionModel().getSelectedItem();


        // Definição da Flag Ation e do objeto de Entidade de settings com Delete
        Settings.ACTION = Settings.ACTION_DELETE;
        Settings.setCarsEdit(selectedItem);

        // Abre a Scene numa nova Stage em modo Modal
        // Aquisição do controlo da Scene pretendida
        Parent scene = FXMLLoader.load(getClass().getResource("Carros.fxml"));

        // Nova janela
        Stage carroDelete = new Stage();
        carroDelete.setTitle("Lista de Carros - Remover Carro");

        // Associação da Scene à Stage
        carroDelete.setScene(new Scene(scene));

        // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
        carroDelete.initOwner(Settings.getPrimaryStage());
        carroDelete.initModality(Modality.WINDOW_MODAL);

        // Abertura da Window
        carroDelete.show();
    }

    public void selectedRow(MouseEvent mouseEvent) {
        if(tableViewCarro.getSelectionModel().getSelectedItem() == null){
            btnEditar.setDisable(true);
            btnEliminar.setDisable(true);
        }
        else{
            btnEditar.setDisable(false);
            btnEliminar.setDisable(false);
        }
    }

    public void buttonSearch(ActionEvent actionEvent) {
    }

    public void buttonInserir(ActionEvent actionEvent) throws IOException {
        Settings.ACTION = Settings.ACTION_INSERT;
        // Abre a Scene numa nova Stage em modo Modal
        // Aquisição do controlo da cena pretendida (student.fxml)
        Parent scene = FXMLLoader.load(getClass().getResource("Carros.fxml"));

        // Nova janela (Stage)
        Stage addCarro = new Stage();
        addCarro.setTitle("Concessionária - Inserir novo Carro");

        // Associação da Scene à Stage
        addCarro.setScene(new Scene(scene));

        // Abertura da janela addStudent em modo MODAL, em relação à primaryStage
        addCarro.initOwner(Settings.getPrimaryStage());
        addCarro.initModality(Modality.WINDOW_MODAL);
        // Abertura da janela
        addCarro.show();
    }
}
