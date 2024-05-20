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

public class ClientesController implements Initializable {

    @FXML
    private Button btnAction;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lbl_Id;

    @FXML
    private TextField txtContato;

    @FXML
    private TextField txtNome;

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
        }
        //Preparação da cena
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                // Altera o texto do título e do botão Action
                // Os campos são apresentados abertos e vazios
                lblTitle.setText("Adicionar Novo Contacto");
                btnAction.setText("Adicionar");
                break;
            case Settings.ACTION_UPDATE:
                // Altera o texto do título e do botão Action
                lblTitle.setText("Atualização do Contacto");
                btnAction.setText("Atualizar");

                lbl_Id.setText(String.valueOf(Settings.getClientsEdit().getIdCliente()));
                txtNome.setText(Settings.getClientsEdit().getNome());
                txtContato.setText(Settings.getClientsEdit().getContato());
                break;
            case Settings.ACTION_DELETE:
                // Altera o texto do título e do botão Action
                lblTitle.setText("Remover Contacto");
                btnAction.setText("Remover");

                //Preencher os campos com os dados do objeto Aluno
                //que pretendemos eliminar
                lbl_Id.setText(String.valueOf(Settings.getClientsEdit().getIdCliente()));
                txtNome.setText(Settings.getClientsEdit().getNome());
                txtContato.setText(Settings.getClientsEdit().getContato());

                //Campos que devem estar disable
                txtNome.setDisable(true);
                txtContato.setDisable(true);
                break;
        }
    }

    public void buttonAction(ActionEvent actionEvent) {
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                String nome = txtNome.getText();
                String contato = txtContato.getText();

                if (nome.isEmpty() || contato.isEmpty()) {
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
                alert.setHeaderText("Deseja inserir este cliente?");
                alert.setContentText("Nome: " + nome + "\nContato: " + contato);

                // Definindo os botões no alerta
                ButtonType buttonTypeSim = new ButtonType("Sim");
                ButtonType buttonTypeNao = new ButtonType("Não");

                alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

                // Mostrando o alerta e esperando pela resposta do usuário
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim) {
                        Clientes newClient = new Clientes(nome, contato);
                        int idCliente = ClientesDAO.adicionarCliente(newClient);
                        newClient.setIdCliente(idCliente);
                        Settings.getClients().add(newClient);

                        Alert alert_inserido = new Alert(Alert.AlertType.INFORMATION);
                        alert_inserido.setTitle("Sucesso");
                        alert_inserido.setHeaderText("Inserção Concluída");
                        alert_inserido.setContentText("O cliente foi inserido com sucesso.");
                        alert_inserido.show();
                    } else {
                        Alert alertCancelado = new Alert(Alert.AlertType.INFORMATION);
                        alertCancelado.setTitle("Cancelado");
                        alertCancelado.setHeaderText("Inserção cancelada");
                        alertCancelado.setContentText("O cliente não foi adicionado.");
                        alertCancelado.show();
                    }
                });
                break;
            case Settings.ACTION_UPDATE:
                int position = Settings.getClients().indexOf(Settings.getClientsEdit());

                if (txtNome.getText().isEmpty() || txtContato.getText().isEmpty()) {
                    Alert alertCamposVazios = new Alert(Alert.AlertType.WARNING);
                    alertCamposVazios.setTitle("Campos Vazios");
                    alertCamposVazios.setHeaderText("Preencha todos os campos obrigatórios");
                    alertCamposVazios.setContentText("Por favor, preencha todos os campos antes de continuar.");
                    alertCamposVazios.showAndWait();
                    return;
                }

                Alert alert_editar = new Alert(Alert.AlertType.CONFIRMATION);
                alert_editar.setTitle("Confirmação");
                alert_editar.setHeaderText("Deseja editar este cliente?");
                alert_editar.setContentText("Nome: " + txtNome.getText() + "\nContato: " + txtContato.getText());

                // Definindo os botões no alerta
                ButtonType buttonTypeSim_editar = new ButtonType("Sim");
                ButtonType buttonTypeNao_editar = new ButtonType("Não");

                alert_editar.getButtonTypes().setAll(buttonTypeSim_editar, buttonTypeNao_editar);

                // Mostrando o alerta e esperando pela resposta do usuário
                alert_editar.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim_editar) {
                        Clientes clienteEditado = Settings.getClientsEdit();
                        clienteEditado.setNome(txtNome.getText());
                        clienteEditado.setContato(txtContato.getText());

                        // Atualize os dados no banco de dados
                        ClientesDAO.atualizarCliente(clienteEditado);
                        Settings.getClients().set(position, clienteEditado);
                        System.out.println("Cliente editado com sucesso.");

                        // Exibir confirmação de sucesso
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Sucesso");
                        successAlert.setHeaderText("Edição bem-sucedida");
                        successAlert.setContentText("O cliente foi editado com sucesso.");
                        successAlert.showAndWait();

                        // Fechar a janela de edição após a edição bem-sucedida
                        Stage stage = (Stage) btnAction.getScene().getWindow();
                        stage.close();
                    } else {
                        Alert canceledAlert = new Alert(Alert.AlertType.INFORMATION);
                        canceledAlert.setTitle("Cancelado");
                        canceledAlert.setHeaderText("Edição cancelada");
                        canceledAlert.setContentText("A edição do cliente foi cancelada.");
                        canceledAlert.showAndWait();
                    }
                });
                break;

            case Settings.ACTION_DELETE:
                int idCliente = Integer.parseInt(lbl_Id.getText());

                Alert alert_eli = new Alert(Alert.AlertType.CONFIRMATION);
                alert_eli.setTitle("Confirmação");
                alert_eli.setHeaderText("Deseja excluir este cliente?");
                alert_eli.setContentText("Você está prestes a excluir o cliente com ID: " + idCliente);

                // Definindo os botões no alerta
                ButtonType buttonTypeSim_eli = new ButtonType("Sim");
                ButtonType buttonTypeNao_eli = new ButtonType("Não");

                alert_eli.getButtonTypes().setAll(buttonTypeSim_eli, buttonTypeNao_eli);

                // Mostra o alerta e esperando pela resposta do usuário
                alert_eli.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeSim_eli) {
                        ClientesDAO.removerCliente(idCliente);
                        Settings.getClients().remove(Settings.getClientsEdit());

                        Alert alert_removido = new Alert(Alert.AlertType.INFORMATION);
                        alert_removido.setTitle("Sucesso");
                        alert_removido.setHeaderText("Remoção Concluída");
                        alert_removido.setContentText("O cliente foi removido com sucesso.");
                        alert_removido.show();
                    } else {
                        Alert canceledAlert = new Alert(Alert.AlertType.INFORMATION);
                        canceledAlert.setTitle("Cancelado");
                        canceledAlert.setHeaderText("Eliminação cancelada");
                        canceledAlert.setContentText("A eliminação do cliente foi cancelada.");
                        canceledAlert.showAndWait();
                    }
                });
                break;
        }
        Settings.ACTION = -1;
        Settings.setClientsEdit(null);
        thisStage = (Stage) btnAction.getScene().getWindow();
        thisStage.close();
    }

    public void buttonCancel(ActionEvent actionEvent) {
        // Reposição da Flag e Objeto Entidade em Settings e encerramento da Stage
        switch (Settings.ACTION) {
            case Settings.ACTION_INSERT:
                Alert alertCancelado = new Alert(Alert.AlertType.INFORMATION);
                alertCancelado.setTitle("Cancelado");
                alertCancelado.setHeaderText("Inserção cancelada");
                alertCancelado.setContentText("O cliente não foi adicionado.");
                alertCancelado.show();
                Settings.ACTION = -1;
                Settings.setClientsEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
            case Settings.ACTION_UPDATE:
                Alert canceledAlert_edi = new Alert(Alert.AlertType.INFORMATION);
                canceledAlert_edi.setTitle("Cancelado");
                canceledAlert_edi.setHeaderText("Edição cancelada");
                canceledAlert_edi.showAndWait();
                Settings.ACTION = -1;
                Settings.setClientsEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
            case Settings.ACTION_DELETE:
                Alert canceledAlert_eli = new Alert(Alert.AlertType.INFORMATION);
                canceledAlert_eli.setTitle("Cancelado");
                canceledAlert_eli.setHeaderText("Eliminação cancelada");
                canceledAlert_eli.showAndWait();
                Settings.ACTION = -1;
                Settings.setClientsEdit(null);
                thisStage = (Stage) btnAction.getScene().getWindow();
                thisStage.close();
                break;
        }
    }
}

