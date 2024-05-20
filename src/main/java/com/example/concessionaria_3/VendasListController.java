package com.example.concessionaria_3;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class VendasListController implements Initializable { 
    @FXML
    private TableView tableViewVendas;
    @FXML
    private TableColumn tableColumnID;
    @FXML
    private TableColumn tableColumnData;
    @FXML
    private TableColumn tableColumnValor;
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
            // Aqui você pode inicializar a tabela, associar as colunas aos atributos da classe e carregar os dados se necessário
            // Associação das colunas aos atributos da classe
            tableColumnID.setCellValueFactory(new PropertyValueFactory<Vendas, Integer>("idVenda"));
            tableColumnData.setCellValueFactory(new PropertyValueFactory<Vendas, LocalDate>("dataVenda"));
            tableColumnValor.setCellValueFactory(new PropertyValueFactory<Vendas, Double>("valor"));

            Settings.getVendas().clear();
            tableViewVendas.setItems(VendasDAO.listarVendas());
            System.out.println(Settings.getVendas().size());

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
    public void buttonEditar(ActionEvent actionEvent) throws IOException {
        // Caso não haja um item selecionado notifica o Utilizador e termina.
        if(tableViewVendas.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Item não selecionado");
            alert.setHeaderText("Selecione um item, por favor!");
            alert.show();
            return;
        }

        Vendas selectedItem = (Vendas) tableViewVendas.getSelectionModel().getSelectedItem();

        // Definição da Flag Ation e do objeto de Entidade de settings com Update
        Settings.ACTION = Settings.ACTION_UPDATE;
        Settings.setVendasEdit(selectedItem);

        // Abre a Scene numa nova Stage em modo Modal
        // Aquisição do controlo da Scene pretendida
        Parent scene = FXMLLoader.load(getClass().getResource("Vendas.fxml"));

        // Nova janela
        Stage vendaEditar = new Stage();
        vendaEditar.setTitle("Lista de Clientes - Atualizar Cliente");

        // Associação da Scene à Stagev
        vendaEditar.setScene(new Scene(scene));

        // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
        vendaEditar.initOwner(Settings.getPrimaryStage());
        vendaEditar.initModality(Modality.WINDOW_MODAL);

        // Abertura da Window
        vendaEditar.show();
    }


    public void buttonEliminar(ActionEvent actionEvent) throws IOException {
        if(tableViewVendas.getSelectionModel().getSelectedItem() == null){

            // Notifica o utilizdor e termina
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Item não selecionado");
            alert.setHeaderText("Selecione um item, por favor!");
            alert.show();
            return;
        }
        // Se chegou aqui é porque há um item selecionado => Extrai-o
        // O método devolve um Object porque nunca sabe o que lá vem. => Cast para Aluno.
        Vendas selectedItem = (Vendas) tableViewVendas.getSelectionModel().getSelectedItem();

        // Definição da Flag Ation e do objeto de Entidade de settings com Delete
        Settings.ACTION = Settings.ACTION_DELETE;
        Settings.setVendasEdit(selectedItem);

        // Abre a Scene numa nova Stage em modo Modal
        // Aquisição do controlo da Scene pretendida
        Parent scene = FXMLLoader.load(getClass().getResource("Vendas.fxml"));

        // Nova janela
        Stage vendasDelete = new Stage();
        vendasDelete.setTitle("Lista de Clientes - Remover Cliente");

        // Associação da Scene à Stage
        vendasDelete.setScene(new Scene(scene));

        // Abertura da janela edit Student em modo MODAL, em relação à primaryStage
        vendasDelete.initOwner(Settings.getPrimaryStage());
        vendasDelete.initModality(Modality.WINDOW_MODAL);

        // Abertura da Window
        vendasDelete.show();
        tableViewVendas.refresh();
    }

    public void buttonSearch(ActionEvent actionEvent) {
    }

    public void selectedRow(MouseEvent mouseEvent) {
        if(tableViewVendas.getSelectionModel().getSelectedItem() == null){
            btnEditar.setDisable(true);
            btnEliminar.setDisable(true);
        }
        else{
            btnEditar.setDisable(false);
            btnEliminar.setDisable(false);
        }
    }

    public void buttonInserir(ActionEvent actionEvent) throws IOException {
        Settings.ACTION = Settings.ACTION_INSERT;
        // Abre a Scene numa nova Stage em modo Modal
        // Aquisição do controlo da cena pretendida (student.fxml)
        Parent scene = FXMLLoader.load(getClass().getResource("Vendas.fxml"));

        // Nova janela (Stage)
        Stage addCVenda = new Stage();
        addCVenda.setTitle("Agenda - Inserir novo Contacto");

        // Associação da Scene à Stage
        addCVenda.setScene(new Scene(scene));

        // Abertura da janela addStudent em modo MODAL, em relação à primaryStage
        addCVenda.initOwner(Settings.getPrimaryStage());
        addCVenda.initModality(Modality.WINDOW_MODAL);
        // Abertura da janela
        addCVenda.show();
        tableViewVendas.refresh();
    }
}
        
        
    
    

