package controller.viewcontroller;

import controller.business.SaleController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import models.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;


/**
 * Classe responsável por controlar a tela de registro de compras.
 * 
 * @author Maria Eduarda Campos 
 * @since 10/06/2025
 * @version 1.0
 */
public class PurchaseRecordController implements Initializable {
    private static Ticket ticket;
    
    @FXML
    private Label labelMovie;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPagamento;

    @FXML
    private Label labelSession;

    @FXML
    private Label labelTicket;

    @FXML
    private Label lnlNome;

    /**
     * Inicializa a tela de registro de compras.
     */    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainViews.addOnChangeScreenListener(new MainViews.OnChangeScreen() {
            @Override
            public void onScreenChanged(String newScreen, Object userDataObject) {
                if (userDataObject instanceof Ticket) {
                    ticket = (Ticket) userDataObject;
                    accessTicketData();
                }
            }
        });
    }

    /**
     * Mostra uma janela.
     * * @param acao Ação realizada.
     */
    public static void mostrarPopUpCancellTicket() {
        try {
            FXMLLoader loader = new FXMLLoader(PurchaseRecordController.class.getResource("/gui/PopUpTicketCanceled.fxml"));
            Parent root = loader.load();

            PopUpTicketCanceledController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.getClient(ticket.getClient());

            stage.setScene(new Scene(root));
            stage.setTitle("Confirmação");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * acessa as datas de cada ticket no sistema e exibe na tela
     */
    private void accessTicketData(){
        labelMovie.setText(ticket.getSession().getMovie().getTitle());
        labelName.setText(ticket.getClient().getName());
        labelSession.setText(ticket.getSession().getDate() + " às " + ticket.getSession().getTime() + " (Sala " + ticket.getSession().getRoom().getId() + ")");
        labelPagamento.setText(ticket.getPaymentMethod().toString());
        
        List<Ticket> list = ticket.getClient().getPurchasingHistory();
        int tickets = 0;
        for(Ticket tck : list){
            if(ticket.getId() == tck.getId()){
                tickets++;
            }
        }
        labelTicket.setText(tickets+"");
    }

    /**
     * Retorna ao histórico de compras do cliente.
     * 
     * @param event evento de clique do botão
     */
    @FXML
    void backPurchase(ActionEvent event) {
        MainViews.changeScreen("clientHistory", ticket.getClient());
    }

    /**
     * Cancela o ticket.
     * 
     * @param event evento de clique do botão
     */
    @FXML
    void cancelTicket(ActionEvent event) {
        SaleController.cancelSale(ticket.getId());
        mostrarPopUpCancellTicket();
    }

    /**
     * Abre o histórico de compras do cliente.
     * 
     * @param event evento de clique do botão
     */
    @FXML
    void openClient(ActionEvent event) {
        MainViews.changeScreen("clientControl", ticket.getClient());
    }
}