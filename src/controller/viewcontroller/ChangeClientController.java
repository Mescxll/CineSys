package controller.viewcontroller;

import controller.business.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import models.*;

/**
 * Classe responsável por controlar a tela de alteração de um cliente.
 * @author Maria Eduarda Campos
 * @since 10/06/2025
 * @version 5.0
 */
public class ChangeClientController implements Initializable {
    private static Client client;

    @FXML
    private TextField boxDate;

    @FXML
    private TextField boxEmail;

    @FXML
    private TextField boxName;

    @FXML
    private TextField boxCPF;

    /**
     * Inicializa o controlador.
     * 
     * @param url O URL de onde o controlador foi carregado.
     * @param rb O ResourceBundle associado ao controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Muda a cor do texto e do fundo dos campos de texto
        boxName.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        boxEmail.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        boxDate.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        boxCPF.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");

        MainViews.addOnChangeScreenListener(new MainViews.OnChangeScreen() {
            @Override
            public void onScreenChanged(String newScreen, Object userDataObject) {
                if (userDataObject instanceof Client) {
                    client = (Client) userDataObject;
                    boxDate.setText(client.getBirthday());
                    boxEmail.setText(client.getEmail());
                    boxName.setText(client.getName());
                    boxCPF.setText(client.getCpf());
                }
            }
        });
    }
    
    /**
     * Retorna o cliente atual.
     * 
     * @param event O evento de clique do botão.
     */
    @FXML
    void backClient(ActionEvent event) {
        MainViews.changeScreen("clientControl", null);
    }

    /**
     * Altera os dados do cliente.
     * 
     * @param event O evento de clique do botão.
     */
    @FXML
    void changeClient(ActionEvent event) {
        String name = boxName.getText().trim();
        String email = boxEmail.getText().trim();
        String date = boxDate.getText().trim();
        String cpf = boxCPF.getText().trim();
       
        ClientController.updateClient(client.getId(), name, cpf, email, date);
        boxName.clear();
        boxEmail.clear();
        boxDate.clear();
        boxCPF.clear();
        ClientControlController.mostrarPopUp("alterado");
    }
}
