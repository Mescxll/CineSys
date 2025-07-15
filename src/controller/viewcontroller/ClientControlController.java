package controller.viewcontroller;

import controller.business.ClientController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.geometry.Insets;

import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

import models.*;

/**
 * Classe referente ao controle da interface "Controle de Clientes" presente na aplicação.
 * 
 * @author Maria Eduarda Campos
 * @since 31/05/2025
 * @version 3.0
 */
public class ClientControlController implements Initializable{
    private static Client client;
    @FXML
    private VBox containerResultados;
    @FXML
    private ScrollPane scrollResultados;
    @FXML
    private TextField txtBusca;

    /**
     * Método que inicializa o controlador da tela. Este método é chamado
     * automaticamente pelo JavaFX após o carregamento do FXML.
     * 
     * @param url URL de localização do arquivo FXML, se necessário.
     * @param resourceBundle Conjunto de recursos localizados, se necessário.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mudança de cor do fundo do campo de busca
        txtBusca.setStyle("-fx-text-fill: white !important; -fx-background-color: #F58414 !important;");
        MainViews.addOnChangeScreenListener(new MainViews.OnChangeScreen() {
            @Override
            public void onScreenChanged(String newScreen, Object userDataObject) {
                if (userDataObject instanceof Client) {
                    client = (Client) userDataObject;
                    txtBusca.setText(client.getName());
                    search();
                }
            }
        });
        addFilter();
    }

    /**
     * Método que captura uma entrada no campo
     * txtBusca e chama o método search().
     */
    private void addFilter() {
        txtBusca.setOnAction(event -> search());
    }

    /**
     * Método de pesquisa que busca na lista de clientes o termo
     * dado entrada e chama o método searchResults().
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
     * Método que utiliza dos clientes selecionados na pesquisa e os apresenta
     * na VBox declarada no fxml da interface em questão.
     * 
     * @param clientList Lista com os clientes encontrados.
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
            labelNome.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");
            Label labelId = new Label("ID: " + client.getId());
            labelId.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");
            Label labelEmail = new Label("Email: " + client.getEmail());
            labelEmail.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;"); 
            Label labelCpf = new Label("CPF: " + client.getCpf());
            labelCpf.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");       
            Label labeldt = new Label("Data de Nascimento: " + client.getBirthday());
            labeldt.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");   
            Label lblPoint = new Label("Pontos do Cliente: " + client.getPoints());
            lblPoint.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;");
            Label labelRelatory= new Label("Acessar Relatório de Compras: ");
            labelRelatory.setStyle(
                    "-fx-text-fill: #f2e8c6 !important; -fx-font-family: Arial !important; -fx-font-size: 14px;"); 

            Button botaoRel = new Button("Relatório");
            botaoRel.setUserData(client);
            botaoRel.setStyle("-fx-background-color: #F58414; -fx-text-fill: #f2e8c6; -fx-font-weight: bold;");

            Button botaoExcluir = new Button("Excluir");
            botaoExcluir.setUserData(client);
            botaoExcluir.setStyle("-fx-background-color: #F58414; -fx-text-fill: #f2e8c6; -fx-font-weight: bold;");
            Button botaoAlterar = new Button("Alterar");
            botaoAlterar.setUserData(client);
            botaoAlterar.setStyle("-fx-background-color: #F58414; -fx-text-fill: #f2e8c6; -fx-font-weight: bold;");

            botaoRel.setOnAction(event -> {
                Client clienteSelecionado = (Client) ((Button) event.getSource()).getUserData();
                MainViews.changeScreen("clientHistory", clienteSelecionado);
            });
            botaoExcluir.setOnAction(event -> {
                Client clienteSelecionado = (Client) ((Button) event.getSource()).getUserData();
                ClientController.removeClient(clienteSelecionado.getId());
                mostrarPopUp("excluído");
            });
            botaoAlterar.setOnAction(event -> {
                Client clienteSelecionado = (Client) ((Button) event.getSource()).getUserData();
                MainViews.changeScreen("changeClient", clienteSelecionado);
            });

            VBox caixaEntradaResultado = new VBox(5);
            caixaEntradaResultado.setPadding(new Insets(0, 0, 10, 0));
            caixaEntradaResultado.getChildren().addAll(labelNome, labelId, labelEmail, labelCpf, labeldt, lblPoint, labelRelatory, botaoRel, botaoExcluir, botaoAlterar);

            containerResultados.getChildren().add(caixaEntradaResultado);
        }
    }

    /**
     * Método que mostra na tela o Pop-up referente ao sucesso
     * de ação de cadastro, exclusão ou alteração de um cliente.
     * 
     * @param acao String indicando a ação que foi executada.
     */
    public static void mostrarPopUp(String acao) {
        try {
            FXMLLoader loader = new FXMLLoader(ClientControlController.class.getResource("/gui/PopUpClient.fxml"));
            Parent root = loader.load();

            PopUpClientController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStage(stage);
            controller.setMensagemPersonalizada(acao);
            stage.setScene(new Scene(root));
            stage.setTitle("Confirmação");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que abre a Tela Principal.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openHomeScreen(ActionEvent event) {
        MainViews.changeScreen("homeScreen", null);
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
     * Método que abre a Tela de Controloe de Clientes, esta mesma.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openClientControl(ActionEvent event) {
        MainViews.changeScreen("clientControl", null);
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
     * Método que abre a Tela de Histórico de Compras.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openPurchaseHistory(ActionEvent event) {
        MainViews.changeScreen("purchaseRelatory", null);
    }

    /**
     * Método que abre a Tela de Ocupação de Salas.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void openRoomOccupation(ActionEvent event) {
        MainViews.changeScreen("roomOccupation", null);
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

    /**
     * Método que busca por um cliente.
     * 
     * @param event Evento ao apertar o botão, caso necessário.
     */
    @FXML
    void buscar(ActionEvent event) {
        search();
    }
}
