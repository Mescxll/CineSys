package controller.viewcontroller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import models.*;

/**
 * Classe responsável por controlar a tela de relatório de ocupação de salas.
 *
 * @author Maria Eduarda Campos
 * @author Vinícius Nunes de Andrade
 * @since 28-05-2025
 * @version 3.0
 */
public class OccupationRelatoryController implements Initializable {
    private static Room room;
    private String selected;

    @FXML
    private Label roomName;

    @FXML
    private Label totalSeat;

    @FXML
    private VBox filterContainer;

    @FXML
    private ComboBox<String> filterOccupation;

    private List<String> filter = new ArrayList<>();
    private ObservableList<String> items;

    /**
     * Volta para a tela de ocupação de salas quando o botão "Voltar" é clicado.
     */
    @FXML
    void backRoomOccupation(ActionEvent event) {
        resetScreen();
        MainViews.changeScreen("roomOccupation", null);
    }

    /**
     * Método para resetar informações da tela quando o usuário sai dela
     */
    private void resetScreen() {
        room = null; 
        selected = null; 

        roomName.setText(""); 
        totalSeat.setText(""); 
        filterContainer.getChildren().clear(); 
        filterOccupation.getSelectionModel().clearSelection();
    }

    /**
     * Inicializa o controlador.
     *
     * @param url URL de localização do arquivo FXML, se necessário.
     * @param rb Conjunto de recursos localizados, se necessário.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainViews.addOnChangeScreenListener(new MainViews.OnChangeScreen() {
            @Override
            public void onScreenChanged(String newScreen, Object userDataObject) {
                if (userDataObject instanceof Room) {
                    room = (Room) userDataObject;
                    updateRoomSpecificUI();
                }
            }
        });
        addFilter();
    }

    /**
     * tualiza a interface específica da sala.
     */
    private void updateRoomSpecificUI() {
        if (room != null) {
            totalSeat.setText(room.getTotalSeat()+"");
            if (room.getId() == 1) {
                roomName.setText("Sala 1");
            } else if (room.getId() == 2) {
                roomName.setText("Sala 2");
            } else if (room.getId() == 3) {
                roomName.setText("Sala 3");
            } else if (room.getId() == 4) {
                roomName.setText("Sala 4");
            } else if (room.getId() == 5) {
                roomName.setText("Sala 5");
            }
        } else {
            roomName.setText("Sala (N/A)");
        }
    }

    /**
     * Adiciona um filtro para a ocupação de salas.
     */
    public void addFilter() {
        filter.clear(); 
        filter.add("Filme");
        filter.add("Horário de Sessão");

        items = FXCollections.observableArrayList(filter);
        filterOccupation.setItems(items);

        filterOccupation.setOnAction(event -> {
            String novoValorSelecionado = filterOccupation.getValue();

            if (novoValorSelecionado == null || novoValorSelecionado.equals(selected)) {
                return;
            }

            selected = novoValorSelecionado; 
            System.out.println("Selecionado: " + selected);

            if (room != null) {
                showFilter(); 
            } else {
                System.err.println("Erro: Não foi possível aplicar filtro, 'room' é nulo.");
            }
        });
    }

    /**
     * Mostra os filtros com base na seleção do ComboBox.
     */
    public void showFilter() {
        filterContainer.getChildren().clear();

        if (room == null || room.getSessions() == null || room.getSessions().isEmpty()) {
            Label noDataLabel = new Label("Não há sessões nesta sala para gerar relatórios.");
            noDataLabel.setStyle("-fx-text-fill: #f2e8c6; -fx-font-size: 14px; -fx-padding: 15px;");
            filterContainer.getChildren().add(noDataLabel);
            return;
        }

        if ("Filme".equals(selected)) {
            room.getSessions().stream()
                    .collect(Collectors.groupingBy(Session::getMovie))
                    .forEach((movie, sessoesDoFilme) -> {
                        double totalVendidos = 0;
                        for (Session session : sessoesDoFilme) {
                            totalVendidos += (room.getTotalSeat() - session.getTotalAvailableSeats());
                        }
                        double totalAssentosOferecidos = (double) sessoesDoFilme.size() * room.getTotalSeat();
                        double ocupacaoMedia = (totalAssentosOferecidos > 0) ? (totalVendidos / totalAssentosOferecidos) * 100 : 0;

                        Text titleText = new Text(movie.getTitle() + " ");
                        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                        titleText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));
                        Text occupationText = new Text("- Ocupação Média: " + String.format("%.1f", ocupacaoMedia) + "%\n");
                        occupationText.setFont(Font.font("Arial", 18));
                        occupationText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));
                        TextFlow textFlow = new TextFlow(titleText, occupationText);
                        filterContainer.getChildren().add(textFlow);
                    });

        } else if ("Horário de Sessão".equals(selected)) {

            List<Movie> moviesNaSala = room.getSessions().stream()
                    .map(Session::getMovie)
                    .distinct()
                    .toList();

            if (moviesNaSala.isEmpty()) {
                Label noMoviesLabel = new Label("Não há filmes com sessões programadas para esta sala.");
                noMoviesLabel.setStyle("-fx-text-fill: #f2e8c6; -fx-font-size: 14px; -fx-padding: 15px;");
                filterContainer.getChildren().add(noMoviesLabel);
                return;
            }

            String activeTabStyle = "-fx-background-color: #af0e2c; -fx-text-fill: #f2e8c6; -fx-background-radius: 5; -fx-font-weight: bold;";
            String inactiveTabStyle = "-fx-background-color: transparent; -fx-text-fill: #f2e8c6; -fx-font-size: 14px;";

            HBox movieTabs = new HBox(15);
            movieTabs.setPadding(new Insets(10, 0, 10, 0));

            List<Button> movieButtons = new ArrayList<>();
            for (Movie movie : moviesNaSala) {
                Button movieBtn = new Button(movie.getTitle());
                movieBtn.setStyle(inactiveTabStyle);
                movieBtn.setUserData(movie);
                movieButtons.add(movieBtn);
            }
            movieTabs.getChildren().addAll(movieButtons);

            VBox sessionDetailsContainer = new VBox(15);
            sessionDetailsContainer.setPadding(new Insets(15));
            sessionDetailsContainer.setStyle("-fx-background-color: #af0e2c; -fx-background-radius: 10; -fx-min-height: 200px;");

            for (Button btn : movieButtons) {
                btn.setOnAction(event -> {
                    for (Button b : movieButtons) {
                        b.setStyle(inactiveTabStyle);
                    }
                    btn.setStyle(activeTabStyle);
                    Movie filmeSelecionado = (Movie) btn.getUserData();
                    displaySessionsForMovie(filmeSelecionado, sessionDetailsContainer);
                });
            }
            if (!movieButtons.isEmpty()) {
                movieButtons.get(0).fire();
            }

            filterContainer.getChildren().addAll(movieTabs, sessionDetailsContainer);
        }
    }

    /**
     * Limpa o contêiner de detalhes e exibe uma lista de todas as sessões
     * para um filme específico QUE ESTÃO NA SALA ATUAL.
     *
     * @param movie O filme cujas sessões serão exibidas.
     * @param container O VBox onde as informações das sessões serão adicionadas.
     */
    private void displaySessionsForMovie(Movie movie, VBox container) {
        container.getChildren().clear();

        List<Session> sessoesDoFilmeNaSala = new ArrayList<>();

        if (room != null && room.getSessions() != null) {
            for (Session session : room.getSessions()) {
                if (session.getMovie().getId() == movie.getId()) {
                    sessoesDoFilmeNaSala.add(session);
                }
            }
        }

        if (sessoesDoFilmeNaSala.isEmpty()) {
            Label noSessionsLabel = new Label("Não há sessões programadas para este filme nesta sala.");
            noSessionsLabel.setStyle("-fx-text-fill: #f2e8c6;");
            container.getChildren().add(noSessionsLabel);
            return;
        }
        int sessionCounter = 1;

        for (Session session : sessoesDoFilmeNaSala) {
            String dataFormatada = session.getDate();
            String horaFormatada = session.getTime();

            int vendidos = room.getTotalSeat() - session.getTotalAvailableSeats();
            double ocupacao = (room.getTotalSeat() > 0) ? ((double) vendidos / room.getTotalSeat()) * 100.0 : 0.0;

            Text sessionTitle = new Text("Sessão" + sessionCounter + " - " + movie.getTitle() + " (" + dataFormatada + " às " + horaFormatada + ")\n");
            sessionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            sessionTitle.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

            Text occupationText = new Text("Ocupação: " + String.format("%.1f", ocupacao) + "%");
            occupationText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            occupationText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

            TextFlow textFlow = new TextFlow(sessionTitle, occupationText);
            container.getChildren().add(textFlow);

            sessionCounter++;
        }
    }
}
