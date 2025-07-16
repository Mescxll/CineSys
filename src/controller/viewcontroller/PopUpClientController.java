package controller.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import controller.viewcontroller.MainViews;

/**
 * Classe responsável pela exibição de mensagens de sucesso ou erro na tela de cadastro de clientes.
 * 
 * @author Maria Eduarda Campos
 * @since 12/06/2025
 * @version 1.0
 */
public class PopUpClientController {

    @FXML private Label lblMsg;
    private Stage stage;

    /**
     * Inicializa a tela de mensagem de sucesso.
     * @param mensagem mensagem a ser exibida na tela
     */
    public void setMensagemPersonalizada(String mensagem) {
        lblMsg.setText("Cliente " + mensagem + " com Sucesso!");
    }

    /**
     * Fecha a tela de mensagem de sucesso.
     * @param stage 
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Fecha a tela de mensagem de sucesso.
     * @param event evento de clique do botão
     */
    @FXML
    void backClientControl(ActionEvent event) {
        MainViews.changeScreen("clientControl", null);
        stage.close();
    }
}
