package controller.viewcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import controller.business.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.Movie;

/**
 * Classe referente ao controle da interface "Registrar Clientes" presente na
 * aplicação.
 * 
 * @author Maria Eduarda Campos
 * @since 31-05-2025
 * @version 2
 */
public class RegisterClientController implements Initializable {
    @FXML
    private TextField enterDate;
    @FXML
    private TextField enterEmail;
    @FXML
    private TextField enterName;
    @FXML
    private TextField enterCpf;

    /**
     * inicializa mudando a cor do texto e do fundo dos campos de texto
     * 
     * @param url O URL de onde o controlador foi carregado.
     * @param rb O ResourceBundle associado ao controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Muda a cor do texto e do fundo dos campos de texto
        enterName.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        enterEmail.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        enterDate.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        enterCpf.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
    }

    /**
     * Método que abre a Tela de Controle de Clientes.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void backClient(ActionEvent event) {
        MainViews.changeScreen("clientControl", null);
    }

    /**
     * Método que registra um novo Cliente.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void registerClient(ActionEvent event) {
        String name = enterName.getText().trim();
        String email = enterEmail.getText().trim();
        String date = enterDate.getText().trim();
        String cpf = enterCpf.getText().trim();

        if (name.isEmpty() || email.isEmpty() || date.isEmpty() || cpf.isEmpty()) {
            return;
        }else{
            ClientController.addClient(name, cpf, email, date);
            enterName.clear();
            enterEmail.clear();
            enterDate.clear();
            enterCpf.clear();
            ClientControlController.mostrarPopUp("cadastrado");
        }
    }
}
