package controller.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.application.Platform;

/**
 * Classe referente ao controle da interface "Tela Principal" presente na
 * aplicação.
 * 
 * @author Maria Eduarda Campos
 * @since 31-05-2025
 * @version 2
 */
public class HomeScreenController {
    @FXML
    private ImageView closeButton;

    /**
     * Método que abre a Tela de Controle de Clientes.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openClientControl(ActionEvent event) {
        MainViews.changeScreen("clientControl", null); 
    }

    /**
     * Método do botão que fecha o programa.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void closeCineSys(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Método que abre a Tela de Registro de Clientes.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openRegisterClient(ActionEvent event) {
        MainViews.changeScreen("registerClient", null);
    }

    /**
     * Método que abre a Tela de Controle de Filmes.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openMovieControl(ActionEvent event) {
        MainViews.changeScreen("movieControl", null);
    }

    /**
     * Método que abre a Tela de Relatório de Ocupação.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openOccupationRelatory(ActionEvent event) {
        MainViews.changeScreen("roomOccupation", null);
    }

    /**
     * Método que abre a Tela de Controle de Compras.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openPurchaseControl(ActionEvent event) {
        MainViews.changeScreen("purchaseRelatory", null);
    }

    /**
     * Método que abre a Tela de Relatórios de Compras.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openPurchaseRelatory(ActionEvent event) {
        MainViews.changeScreen("purchaseRelatory", null);
    }

    /**
     * Método que abre a Tela de Controle de Sessões.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openSessionControl(ActionEvent event) {
        MainViews.changeScreen("sessionControl", null);
    }    
}