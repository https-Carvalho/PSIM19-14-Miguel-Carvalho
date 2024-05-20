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
import java.util.ResourceBundle;

    public class ClientesListController implements Initializable {
        @FXML
        private TableView tableViewCliente;
        @FXML
        private TableColumn tableColumnID;
        @FXML
        private TableColumn tableColumnNome;
        @FXML
        private TableColumn tableColumnContato;
        @FXML
        private TextField txtSearch;
        @FXML
        private Button btnEditar;
        @FXML
        private Button btnEliminar;
        @FXML
        private Button btnInserir;
        @FXML
        public Button btnSearch;
        //endregion
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            // Associação das colunas aos atributos da classe
            tableColumnID.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
            tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            tableColumnContato.setCellValueFactory(new PropertyValueFactory<>("contato"));
    
            Settings.getClients().clear();
            tableViewCliente.setItems(ClientesDAO.listarClientes());
            System.out.println(Settings.getClients().size());
    
            /*FilteredList<Clientes> searchDados = new FilteredList<>(Settings.getClients());
    
            txtSearch.textProperty().addListener((observable, oldValue, newValue) ->{
                searchDados.setPredicate(clientes -> {
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
    
                    String searchNome = newValue.toLowerCase();
                    if(clientes.getNome().toLowerCase().contains(searchNome) ){
                        return true;
                    }
                    else if(String.valueOf(clientes.getIdCliente()).contains(searchNome)){
                        return true;
                    }
                    else{
                        return false;
                    }
                });
            });
    
            SortedList<Clientes> sortedDados = new SortedList<>(searchDados);
            sortedDados.comparatorProperty().bind(tableViewCliente.comparatorProperty());
            tableViewCliente.setItems(sortedDados);
             */

    // Fim do filtro de pesquisa
    
            btnEditar.setDisable(true);
            btnEliminar.setDisable(true);
    
        }
    
    
        public void buttonEditar(ActionEvent actionEvent) throws Exception{
            // Caso não haja um item selecionado notifica o Utilizador e termina.
            if(tableViewCliente.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Item não selecionado");
                alert.setHeaderText("Selecione um item, por favor!");
                alert.show();
                return;
            }
    
            Clientes selectedItem = (Clientes) tableViewCliente.getSelectionModel().getSelectedItem();
    
            // Definição da Flag Ation e do objeto de Entidade de settings com Update
            Settings.ACTION = Settings.ACTION_UPDATE;
            Settings.setClientsEdit(selectedItem);
    
            // Abre a Scene numa nova Stage em modo Modal
            // Aquisição do controlo da Scene pretendida
            Parent scene = FXMLLoader.load(getClass().getResource("Clientes.fxml"));
    
            // Nova janela
            Stage clienteEditar = new Stage();
            clienteEditar.setTitle("Lista de Clientes - Atualizar Cliente");
    
            // Associação da Scene à Stage
            clienteEditar.setScene(new Scene(scene));
    
            // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
            clienteEditar.initOwner(Settings.getPrimaryStage());
            clienteEditar.initModality(Modality.WINDOW_MODAL);
    
            // Abertura da Window
            clienteEditar.show();
        }
    
        public void buttonEliminar(ActionEvent actionEvent) throws Exception{
            // Caso não haja um item selecionado notifica o Utilizador e termina.
            if(tableViewCliente.getSelectionModel().getSelectedItem() == null){
    
                // Notifica o utilizdor e termina
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Item não selecionado");
                alert.setHeaderText("Selecione um item, por favor!");
                alert.show();
                return;
            }
            // Se chegou aqui é porque há um item selecionado => Extrai-o
            // O método devolve um Object porque nunca sabe o que lá vem. => Cast para Aluno.
            Clientes selectedItem = (Clientes) tableViewCliente.getSelectionModel().getSelectedItem();
    
            // Definição da Flag Ation e do objeto de Entidade de settings com Delete
            Settings.ACTION = Settings.ACTION_DELETE;
            Settings.setClientsEdit(selectedItem);
    
            // Abre a Scene numa nova Stage em modo Modal
            // Aquisição do controlo da Scene pretendida
            Parent scene = FXMLLoader.load(getClass().getResource("Clientes.fxml"));
    
            // Nova janela
            Stage clienteDelete = new Stage();
            clienteDelete.setTitle("Lista de Clientes - Remover Cliente");
    
            // Associação da Scene à Stage
            clienteDelete.setScene(new Scene(scene));
    
            // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
            clienteDelete.initOwner(Settings.getPrimaryStage());
            clienteDelete.initModality(Modality.WINDOW_MODAL);
    
                // Abertura da Window
            clienteDelete.show();
            tableViewCliente.refresh();
        }
    
    
    
        public void selectedRow(MouseEvent mouseEvent) {
            if(tableViewCliente.getSelectionModel().getSelectedItem() == null){
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
            Parent scene = FXMLLoader.load(getClass().getResource("Clientes.fxml"));
    
            // Nova janela (Stage)
            Stage addCliente = new Stage();
            addCliente.setTitle("Agenda - Inserir novo Contacto");
    
            // Associação da Scene à Stage
            addCliente.setScene(new Scene(scene));
    
            // Abertura da janela addStudent em modo MODAL, em relação à primaryStage
            addCliente.initOwner(Settings.getPrimaryStage());
            addCliente.initModality(Modality.WINDOW_MODAL);
            // Abertura da janela
            addCliente.show();
            tableViewCliente.refresh();
    
        }
    }