package com.example.concessionaria_3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class VendasController implements Initializable {

    @FXML
    private Button btnAction;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lbl_Id;

    @FXML
    private DatePicker dpDataVenda;

    @FXML
    private TextField txtValor;
    @FXML
    private TextField txt_IdCarro;
    @FXML
    private Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Se flag Action não está definida => notifica e termina
        if (Settings.ACTION == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sair da aplicação");
            alert.setHeaderText("A flag Action não está definida");
            alert.show();
            return;
        }
        // Preparação da cena
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                lblTitle.setText("Adicionar Nova Venda");
                btnAction.setText("Adicionar");
                break;
            case Settings.ACTION_UPDATE:
                lblTitle.setText("Atualização da Venda");
                btnAction.setText("Atualizar");

                Vendas venda = Settings.getVendasEdit();
                lbl_Id.setText(String.valueOf(venda.getIdVenda()));
                dpDataVenda.setValue(venda.getDataVenda());
                txtValor.setText(String.valueOf(venda.getValor()));
                txt_IdCarro.setText(String.valueOf(venda.getIdCarro()));
                break;
            case Settings.ACTION_DELETE:
                lblTitle.setText("Remover Venda");
                btnAction.setText("Remover");

                venda = Settings.getVendasEdit();
                lbl_Id.setText(String.valueOf(venda.getIdVenda()));
                dpDataVenda.setValue(venda.getDataVenda());
                txtValor.setText(String.valueOf(venda.getValor()));
                txt_IdCarro.setText(String.valueOf(venda.getIdCarro()));

                dpDataVenda.setDisable(true);
                txtValor.setDisable(true);
                txt_IdCarro.setDisable(true);
                break;
        }
    }

    public void buttonAction(ActionEvent actionEvent) {
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                // Verifica se os campos obrigatórios estão preenchidos
                if (dpDataVenda.getValue() == null || txtValor.getText().isEmpty() || txt_IdCarro.getText().isEmpty()) {
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
                alert.setHeaderText("Deseja inserir esta venda?");
                alert.setContentText("Data: " + dpDataVenda.getValue() + "\nValor: " + txtValor.getText() + "\nID do Carro: " + txt_IdCarro.getText());

                ButtonType buttonTypeSim = new ButtonType("Sim");
                ButtonType buttonTypeNao = new ButtonType("Não");

                alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim) {
                        int idCarro = Integer.parseInt(txt_IdCarro.getText());

                        // Verifica se o carro já foi vendido
                        if (VendasDAO.isCarroVendido(idCarro)) {
                            Alert vendidoAlert = new Alert(Alert.AlertType.WARNING);
                            vendidoAlert.setTitle("Erro");
                            vendidoAlert.setHeaderText("Carro já vendido");
                            vendidoAlert.setContentText("O carro com ID " + idCarro + " já foi vendido.");
                            vendidoAlert.show();
                        } else {
                            LocalDate dataVenda = dpDataVenda.getValue();
                            double valor = Double.parseDouble(txtValor.getText());
                            Vendas newVenda = new Vendas(dataVenda, valor, idCarro);
                            int idVenda = VendasDAO.adicionarVenda(newVenda, idCarro);
                            newVenda.setIdVenda(idVenda);
                            Settings.getVendas().add(newVenda);

                            Alert sucessoAlert = new Alert(Alert.AlertType.INFORMATION);
                            sucessoAlert.setTitle("Sucesso");
                            sucessoAlert.setHeaderText("Venda inserida com sucesso!");
                            sucessoAlert.show();
                        }
                    } else {
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                        cancelAlert.setTitle("Cancelado");
                        cancelAlert.setHeaderText("A inserção foi cancelada");
                        cancelAlert.show();
                    }
                });
                break;

            case Settings.ACTION_UPDATE:
                // Verifica se os campos obrigatórios estão preenchidos
                if (dpDataVenda.getValue() == null || txtValor.getText().isEmpty()) {
                    Alert alertCamposVazios = new Alert(Alert.AlertType.ERROR);
                    alertCamposVazios.setTitle("Erro");
                    alertCamposVazios.setHeaderText("Campos obrigatórios não preenchidos");
                    alertCamposVazios.setContentText("Por favor, preencha todos os campos.");
                    alertCamposVazios.show();
                    return; // Retorna para que o restante do código não seja executado
                }

                int position = Settings.getVendas().indexOf(Settings.getVendasEdit());
                Alert alert_editar = new Alert(Alert.AlertType.CONFIRMATION);
                alert_editar.setTitle("Confirmação");
                alert_editar.setHeaderText("Deseja editar esta venda?");
                alert_editar.setContentText("Data: " + dpDataVenda.getValue() + "\nValor: " + txtValor.getText());

                ButtonType buttonTypeSim_editar = new ButtonType("Sim");
                ButtonType buttonTypeNao_editar = new ButtonType("Não");

                alert_editar.getButtonTypes().setAll(buttonTypeSim_editar, buttonTypeNao_editar);

                alert_editar.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim_editar) {
                        Vendas vendaEditada = Settings.getVendasEdit();
                        vendaEditada.setDataVenda(dpDataVenda.getValue());
                        vendaEditada.setValor(Double.parseDouble(txtValor.getText()));
                        VendasDAO.atualizarVenda(vendaEditada);
                        Settings.getVendas().set(position, vendaEditada);

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Sucesso");
                        successAlert.setHeaderText("Edição bem-sucedida");
                        successAlert.setContentText("A venda foi editada com sucesso.");
                        successAlert.showAndWait();
                    } else {
                        Alert canceledAlert = new Alert(Alert.AlertType.INFORMATION);
                        canceledAlert.setTitle("Cancelado");
                        canceledAlert.setHeaderText("Edição cancelada");
                        canceledAlert.setContentText("A edição da venda foi cancelada.");
                        canceledAlert.showAndWait();
                    }
                });
                break;

            case Settings.ACTION_DELETE:
                int idVenda = Integer.parseInt(lbl_Id.getText());
                Alert alert_eli = new Alert(Alert.AlertType.CONFIRMATION);
                alert_eli.setTitle("Confirmação");
                alert_eli.setHeaderText("Deseja excluir esta venda?");
                alert_eli.setContentText("Você está prestes a excluir a venda com ID: " + idVenda);

                ButtonType buttonTypeSim_eli = new ButtonType("Sim");
                ButtonType buttonTypeNao_eli = new ButtonType("Não");

                alert_eli.getButtonTypes().setAll(buttonTypeSim_eli, buttonTypeNao_eli);
                alert_eli.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim_eli) {
                        VendasDAO.removerVenda(idVenda);
                        Settings.getVendas().remove(Settings.getVendasEdit());

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Sucesso");
                        successAlert.setHeaderText("Remoção Concluída");
                        successAlert.setContentText("A venda foi removida com sucesso.");
                        successAlert.showAndWait();
                    } else {
                        Alert canceledAlert = new Alert(Alert.AlertType.INFORMATION);
                        canceledAlert.setTitle("Cancelado");
                        canceledAlert.setHeaderText("Exclusão cancelada");
                        canceledAlert.setContentText("A exclusão da venda foi cancelada.");
                        canceledAlert.showAndWait();
                    }
                });
                break;
        }
        Settings.ACTION = -1;
        Settings.setVendasEdit(null);
        thisStage = (Stage) btnAction.getScene().getWindow();
        thisStage.close();
    }

    public void buttonCancel(ActionEvent actionEvent) {
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                Alert alertCancelado = new Alert(Alert.AlertType.INFORMATION);
                alertCancelado.setTitle("Cancelado");
                alertCancelado.setHeaderText("Inserção cancelada");
                alertCancelado.setContentText("A venda não foi adicionada.");
                alertCancelado.show();
                Settings.ACTION = -1;
                Settings.setVendasEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
            case Settings.ACTION_UPDATE:
                Alert canceledAlert_edi = new Alert(Alert.AlertType.INFORMATION);
                canceledAlert_edi.setTitle("Cancelado");
                canceledAlert_edi.setHeaderText("Edição cancelada");
                canceledAlert_edi.showAndWait();
                Settings.ACTION = -1;
                Settings.setVendasEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
            case Settings.ACTION_DELETE:
                Alert canceledAlert_eli = new Alert(Alert.AlertType.INFORMATION);
                canceledAlert_eli.setTitle("Cancelado");
                canceledAlert_eli.setHeaderText("Eliminação cancelada");
                canceledAlert_eli.showAndWait();
                Settings.ACTION = -1;
                Settings.setVendasEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
        }
    }

    //region Validação de dados
    public void textFieldValorKeyPressed(KeyEvent keyEvent) {
        Settings.checkMaxLength((TextField) keyEvent.getSource(), 10);
        Settings.isNumeric((TextField) keyEvent.getSource());
    }
    //endregion
}

