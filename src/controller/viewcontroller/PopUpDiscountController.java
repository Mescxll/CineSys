package controller.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controlador do PopUp de Desconto.
 * Exibe o valor do desconto e retorna à tela de venda de ingressos.
 *
 * @author Helen Santos Rocha
 * @since 13-06-2025
 * @version 1.0
 */
public class PopUpDiscountController {

    @FXML
    private Label discountMessage; // Label que exibe a mensagem de desconto

    @FXML
    private Button backButton; // Botão de "Voltar" para tela de venda

    private Stage stage;

    /**
     * Define o valor de desconto no label da interface.
     *
     * @param discount Valor percentual de desconto (ex: 10.0 = 10%)
     */
    public void setDiscount(double discount) {
        discountMessage.setText(String.format("Desconto aplicado: %.0f%%", discount));
    }

    /**
     * Fecha a tela de mensagem de sucesso.
     * 
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Fecha o pop-up e retorna para a tela de venda de ingressos.
     */
    @FXML
    private void backClientControl() {
        // Vai pra tela de confirmação com os dados do ticket
        SellTicketController.mostrarPopUpSale();
        // Fecha a janelinha
        stage.close();
    }
}