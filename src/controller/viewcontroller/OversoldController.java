package controller.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controlador da tela de erro por sala lotada (Oversold).
 * Informa que a venda não foi concluída e retorna para a tela de venda.
 *
 * @author Helen Santos Rocha
 * @since 13-06-2025
 * @version 1.0
 */
public class OversoldController {

    @FXML
    private Button backButton;

    /**
     * Fecha a tela atual e retorna para a tela de venda de ingressos.
     */
    @FXML
    private void handleBack() {
       // Volta para a tela de venda
        MainViews.changeScreen("sessionControl", null);
    }
}
