package com.example.concessionaria_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CarrosController implements Initializable {
    @FXML
    private Label lblTitle;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtPreco;

    @FXML
    private Button btnAction;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lbl_Id;

    @FXML
    private Label lbl_status;

    @FXML
    private DatePicker datePicker;

    private Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Se a flag Action não estiver definida, exibe um alerta e termina
        if (Settings.ACTION == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sair da aplicação");
            alert.setHeaderText("A flag Action não está definida");
            alert.show();
        }
        // Preparação da cena
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                lblTitle.setText("Adicionar Novo Carro");
                btnAction.setText("Adicionar");
                lbl_status.setText("disponível");
                break;
            case Settings.ACTION_UPDATE:
                lblTitle.setText("Atualização do Carro");
                btnAction.setText("Atualizar");

                lbl_Id.setText(String.valueOf(Settings.getCarsEdit().getIdCarro()));
                txtMarca.setText(Settings.getCarsEdit().getMarca());
                txtModelo.setText(Settings.getCarsEdit().getModelo());
                txtPreco.setText(String.valueOf(Settings.getCarsEdit().getPreco()));
                datePicker.setValue(Settings.getCarsEdit().getDataRegistro());
                lbl_status.setText("disponível");
                break;
            case Settings.ACTION_DELETE:
                lblTitle.setText("Remover Carro");
                btnAction.setText("Remover");

                lbl_Id.setText(String.valueOf(Settings.getCarsEdit().getIdCarro()));
                txtMarca.setText(Settings.getCarsEdit().getMarca());
                txtModelo.setText(Settings.getCarsEdit().getModelo());
                txtPreco.setText(String.valueOf(Settings.getCarsEdit().getPreco()));
                datePicker.setValue(Settings.getCarsEdit().getDataRegistro());

                txtMarca.setDisable(true);
                txtModelo.setDisable(true);
                txtPreco.setDisable(true);
                datePicker.setDisable(true);
                lbl_status.setText("disponível");
                break;
        }
    }

    @FXML
    public void buttonAction(ActionEvent actionEvent) {
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                String marca = txtMarca.getText();
                String modelo = txtModelo.getText();
                LocalDate data_registo = datePicker.getValue();

                if (marca.isEmpty() || modelo.isEmpty() || txtPreco.getText().isEmpty() || data_registo == null) {
                    Alert alertCamposVazios = new Alert(Alert.AlertType.ERROR);
                    alertCamposVazios.setTitle("Erro");
                    alertCamposVazios.setHeaderText("Campos obrigatórios não preenchidos");
                    alertCamposVazios.setContentText("Por favor, preencha todos os campos.");
                    alertCamposVazios.show();
                    return; // Retorna para que o restante do código não seja executado
                }

                // Criando o alerta de confirmação
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação");
                alert.setHeaderText("Deseja inserir este carro?");
                alert.setContentText("Marca: " + marca + "\nModelo: " + modelo + "\nData de registo: " + data_registo + "\nPreço: " + txtPreco.getText());
                // Definindo os botões no alerta
                ButtonType buttonTypeSim = new ButtonType("Sim");
                ButtonType buttonTypeNao = new ButtonType("Não");

                alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

                // Mostrando o alerta e esperando pela resposta do usuário
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim) {
                        double preco = Double.parseDouble(txtPreco.getText());
                        Carros newCar = new Carros(marca, modelo, data_registo, preco);

                        int id_carro = CarrosDAO.adicionarCarro(newCar);
                        newCar.setIdCarro(id_carro);
                        newCar.setStatus("disponível");  // Define o status como disponível

                        Settings.getCars().add(newCar);
                        Alert alert_inserido = new Alert(Alert.AlertType.INFORMATION);
                        alert_inserido.setTitle("Sucesso");
                        alert_inserido.setHeaderText("Inserção Concluida");
                        alert_inserido.setContentText("O carro foi inserido com sucesso.");
                        alert_inserido.show();
                    } else {
                        Alert alertCancelado = new Alert(Alert.AlertType.INFORMATION);
                        alertCancelado.setTitle("Cancelado");
                        alertCancelado.setHeaderText("Inserção cancelada");
                        alertCancelado.setContentText("O Carro não foi adicionado.");
                        alertCancelado.show();
                    }
                });
                break;


            case Settings.ACTION_UPDATE:
                int position = Settings.getCars().indexOf(Settings.getCarsEdit());

                if (txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() || datePicker.getValue() == null || txtPreco.getText().isEmpty()) {
                    Alert alertCamposVazios = new Alert(Alert.AlertType.ERROR);
                    alertCamposVazios.setTitle("Erro");
                    alertCamposVazios.setHeaderText("Campos obrigatórios não preenchidos");
                    alertCamposVazios.setContentText("Por favor, preencha todos os campos.");
                    alertCamposVazios.showAndWait();
                    return; // Retorna para que o restante do código não seja executado
                } else {
                    Alert alert_editar = new Alert(Alert.AlertType.CONFIRMATION);
                    alert_editar.setTitle("Confirmação");
                    alert_editar.setHeaderText("Deseja editar este carro?");
                    alert_editar.setContentText("Marca: " + txtMarca.getText() + "\nModelo: " + txtModelo.getText() + "\nData de Registro: " + datePicker.getValue() + "\nPreço: " + txtPreco.getText());

                    // Definindo os botões no alerta
                    ButtonType buttonTypeSim_editar = new ButtonType("Sim");
                    ButtonType buttonTypeNao_editar = new ButtonType("Não");

                    alert_editar.getButtonTypes().setAll(buttonTypeSim_editar, buttonTypeNao_editar);

                    // Mostrando o alerta e esperando pela resposta do usuário
                    alert_editar.showAndWait().ifPresent(response -> {
                        if (response == buttonTypeSim_editar) {
                            // Se o usuário clicar em "Sim", então edite o carro
                            Carros carroEditado = Settings.getCarsEdit();
                            carroEditado.setIdCarro(Integer.parseInt(lbl_Id.getText()));
                            carroEditado.setMarca(txtMarca.getText());
                            carroEditado.setModelo(txtModelo.getText());
                            carroEditado.setDataRegistro(datePicker.getValue());
                            carroEditado.setPreco(Double.parseDouble(txtPreco.getText()));
                            carroEditado.setStatus("disponível"); // Define o status como disponível

                            // Atualize os dados no banco de dados
                            CarrosDAO.atualizarCarro(carroEditado);
                            Settings.getCars().set(position, Settings.getCarsEdit());
                            System.out.println("Carro editado com sucesso.");

                            // Exibir confirmação de sucesso
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Sucesso");
                            successAlert.setHeaderText("Edição bem-sucedida");
                            successAlert.setContentText("O carro foi editado com sucesso.");
                            successAlert.showAndWait();

                            // Fechar a janela de edição após a edição bem-sucedida
                            Stage stage = (Stage) btnAction.getScene().getWindow();
                            stage.close();
                        } else {
                            // Se o usuário clicar em "Não" ou fechar o alerta, não faça nada
                            Alert canceledAlert = new Alert(Alert.AlertType.INFORMATION);
                            canceledAlert.setTitle("Cancelado");
                            canceledAlert.setHeaderText("Edição cancelada");
                            canceledAlert.setContentText("A edição do carro foi cancelada.");
                            canceledAlert.showAndWait();
                        }
                    });
                }
                break;
            case Settings.ACTION_DELETE:
                int id_car = Integer.parseInt(lbl_Id.getText());
                CarrosDAO.removerCarro(id_car);
                Settings.getCars().remove(Settings.getCarsEdit());
                Alert alert_removido = new Alert(Alert.AlertType.INFORMATION);
                alert_removido.setTitle("Sucesso");
                alert_removido.setHeaderText("Remoção Concluída");
                alert_removido.setContentText("O carro foi removido com sucesso.");
                alert_removido.show();
                break;
        }
        Settings.ACTION = -1;
        Settings.setCarsEdit(null);
        thisStage = (Stage) btnAction.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    public void buttonCancel(ActionEvent actionEvent) {
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                Alert alertCancelado = new Alert(Alert.AlertType.INFORMATION);
                alertCancelado.setTitle("Cancelado");
                alertCancelado.setHeaderText("Inserção cancelada");
                alertCancelado.setContentText("O carro não foi adicionado.");
                alertCancelado.show();
                Settings.ACTION = -1;
                Settings.setCarsEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
            case Settings.ACTION_UPDATE:
                Alert canceledAlert_edi = new Alert(Alert.AlertType.INFORMATION);
                canceledAlert_edi.setTitle("Cancelado");
                canceledAlert_edi.setHeaderText("Edição cancelada");
                canceledAlert_edi.showAndWait();
                Settings.ACTION = -1;
                Settings.setCarsEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
            case Settings.ACTION_DELETE:
                Alert canceledAlert_eli = new Alert(Alert.AlertType.INFORMATION);
                canceledAlert_eli.setTitle("Cancelado");
                canceledAlert_eli.setHeaderText("Eliminação cancelada");
                canceledAlert_eli.showAndWait();
                Settings.ACTION = -1;
                Settings.setCarsEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
        }
    }
}
