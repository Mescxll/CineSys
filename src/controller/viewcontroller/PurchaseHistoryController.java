package controller.viewcontroller;

import controller.business.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

/**
 * Classe responsável por controlar a tela de histórico de compras.
 * 
 * @author Vinicius Nunes de Andrade 
 * @author Maria Eduarda Campos 
 * @since 30/05/2025
 * @version 5.0
 */
public class PurchaseHistoryController implements Initializable {
    @FXML
    private ScrollPane scrollPaneResultados;

    @FXML
    private VBox containerResultados;

    @FXML
    private TextField txtBusca;

    /**
     * Inicializa a tela de histórico de compras.
     * 
     * @param url URL da tela
     * @param resourceBundle ResourceBundle da tela
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Muda a cor do texto e do fundo dos campos de texto
        txtBusca.setStyle("-fx-text-fill: white !important; -fx-background-color:  #BB0029 !important;");
        addFilter();
    }


    /**
     * Abre a tela principal.
     * 
     * @param event evento de clique do botão
     */
    @FXML
    void openHomeScreen(ActionEvent event) {
        MainViews.changeScreen("homeScreen", null);
    }

    /**
     * Abre a tela de controle de clientes.
     * 
     * @param event
     */
    @FXML
    void openClientControl(ActionEvent event) {
        MainViews.changeScreen("clientControl", null);
    }

    /**
     * Abre a tela de controle de filmes.
     * 
     * @param event
     */
    @FXML
    void openMovieControl(ActionEvent event) {
        MainViews.changeScreen("movieControl", null);
    }

    /**
     * Abre a tela de controle de salas.
     * 
     * @param event
     */
    @FXML
    void openPurchaseHistory(ActionEvent event) {
        MainViews.changeScreen("purchaseRelatory", null);
    }

    /**
     * Abre a tela de ocupação de salas.
     * 
     * @param event
     */
    @FXML
    void openRoomOccupation(ActionEvent event) {
        MainViews.changeScreen("roomOccupation", null);
    }

    /**
     * Abre a tela de controle de sessões.
     * 
     * @param event
     */
    @FXML
    void openSessionControl(ActionEvent event) {
        MainViews.changeScreen("sessionControl", null);
    }

    /**
     * a]Adiciona os filtros na tela de histórico de compras.
     */
    private void addFilter() {
        txtBusca.setOnAction(event -> search());
    }

    /**
     * Realiza a busca de clientes com base no termo de busca.
     * 
     * @param event evento de clique do botão
     */
    @FXML
    void buscar(ActionEvent event) {
        search();
    }

    /**
     * Realiza a busca de clientes no repositorio de clientes.
     */
    @FXML
    private void search() {
        String searchTerm = txtBusca.getText().toLowerCase(Locale.ROOT).trim();
        List<Client> searchResultsList = new ArrayList<>();

        if (!searchTerm.isEmpty()) {
            for (Client client : ClientController.getAllClients()) {
                if (client.getName().toLowerCase(Locale.ROOT).contains(searchTerm)) {
                    searchResultsList.add(client);
                }
            }
        }

        searchResults(searchResultsList);
    }

    /**
     * Exibe os resultados da busca de clientes.
     * 
     * @param clientList lista de clientes encontrados
     */
    private void searchResults(List<Client> clientList) {
        containerResultados.getChildren().clear();

        if (clientList.isEmpty()) {
            Label noResultsLabel = new Label("Nenhum cliente encontrado.");
            containerResultados.getChildren().add(noResultsLabel);
            return;
        }
            
        Label labelContagemResultados = new Label(clientList.size() + " cliente(s) encontrado(s)");
        containerResultados.getChildren().add(labelContagemResultados);
        
        

        for (Client client : clientList) {
            Label labelNome = new Label("\nNome: " + client.getName());
            labelNome.setStyle("-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");
            Label labelCpf = new Label("ID: " + client.getCpf());
            labelCpf.setStyle("-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");
            Label labelId = new Label("ID: " + client.getId());
            labelId.setStyle("-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");

            Button botaoAcessar = new Button("Acessar Relatório(s)");
            botaoAcessar.setUserData(client);
            botaoAcessar.setStyle("-fx-background-color: #BB0029; -fx-text-fill: #f2e8c6; -fx-font-weight: bold;");

            botaoAcessar.setOnAction(event -> {
                Client clienteSelecionado = (Client) ((Button) event.getSource()).getUserData();
                MainViews.changeScreen("clientHistory", clienteSelecionado);
            });

            VBox caixaEntradaResultado = new VBox(5);
            caixaEntradaResultado.setPadding(new Insets(0, 0, 10, 0));
            caixaEntradaResultado.getChildren().addAll(labelNome, labelCpf, labelId, botaoAcessar);

            containerResultados.getChildren().add(caixaEntradaResultado);
        }
    }
}
