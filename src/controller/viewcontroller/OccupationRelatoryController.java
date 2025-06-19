package controller.viewcontroller;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.business.MovieController;
import controller.business.RoomController;
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
import java.time.LocalDate;
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
        room = null; // Zera a referência à sala
        selected = null; // Zera o filtro selecionado

        roomName.setText(""); // Limpa o nome da sala
        totalSeat.setText(""); // Limpa o total de assentos
        filterContainer.getChildren().clear(); // Remove todos os filtros exibidos
        filterOccupation.getSelectionModel().clearSelection(); // Limpa seleção da ComboBox
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
        filter.clear(); // Limpa a lista para evitar duplicatas se o método for chamado novamente
        filter.add("Filme");
        filter.add("Data");
        filter.add("Horário de Sessão");

        items = FXCollections.observableArrayList(filter);
        filterOccupation.setItems(items);

        filterOccupation.setOnAction(event -> {
            String novoValorSelecionado = filterOccupation.getValue();

            // --- GUARDA CONTRA LOOP INFINITO ---
            // Se o novo valor for nulo ou igual ao valor já selecionado, não faz nada.
            if (novoValorSelecionado == null || novoValorSelecionado.equals(selected)) {
                return;
            }
            // ------------------------------------

            selected = novoValorSelecionado; // Atualiza a seleção
            System.out.println("Selecionado: " + selected);

            if (room != null) {
                showFilter(); // Chama o método para mostrar o filtro
            } else {
                System.err.println("Erro: Não foi possível aplicar filtro, 'room' é nulo.");
            }
        });
    }

    /**
     * Mostra os filtros para a sala 1.
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

            List<Session> sessoesList = new ArrayList<>();
            for (Session s : room.getSessions()) {
                sessoesList.add(s);
            }
            if (sessoesList.isEmpty()) {
                filterContainer.getChildren().add(new Label("Nenhuma sessão encontrada."));
                return;
            }

            sessoesList.stream()
                    .collect(Collectors.groupingBy(Session::getMovie)) // Agrupa sessões pelo objeto Movie
                    .forEach((movie, sessoesDoFilme) -> {

                        double totalOcupacao = 0;
                        for (Session session : sessoesDoFilme) {
                            int vendidos = room.getTotalSeat() - session.getTotalAvailableSeats();
                            totalOcupacao += (double) vendidos;
                        }
                        double totalAssentosOferecidos = (double) sessoesDoFilme.size() * room.getTotalSeat();
                        double ocupacaoMedia = (totalAssentosOferecidos > 0) ? (totalOcupacao / totalAssentosOferecidos) * 100 : 0;

                        Text titleText = new Text(movie.getTitle() + " ");
                        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                        titleText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

                        Text occupationText = new Text("- Ocupação Média: " + String.format("%.1f", ocupacaoMedia) + "%\n");
                        occupationText.setFont(Font.font("Arial", 18));
                        occupationText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

                        TextFlow textFlow = new TextFlow(titleText, occupationText);
                        filterContainer.getChildren().add(textFlow);
                        System.out.println("Resumo do filme '" + movie.getTitle() + "' adicionado.");
                    });
        } else if ("Data".equals(selected)) {
            // --- Criação das Abas de Filtro de Data ---
            HBox tabs = new HBox(10); // HBox para agrupar os botões de data
            tabs.setPadding(new Insets(10, 0, 10, 0));

            Button hojeBtn = new Button("Hoje");
            Button semanaBtn = new Button("Esta semana");
            Button mesBtn = new Button("Este mês");
            Button anoBtn = new Button("Este ano");

            // Agrupa os botões em uma lista para facilitar a manipulação dos estilos
            List<Button> tabButtons = List.of(hojeBtn, semanaBtn, mesBtn, anoBtn);

            // Define os estilos para os estados ativo e inativo
            String activeTabStyle = "-fx-background-color: #af0e2c; -fx-text-fill: #f2e8c6; -fx-background-radius: 5; -fx-font-size: 18px;";
            String inactiveTabStyle = "-fx-background-color: transparent; -fx-text-fill: #f2e8c6; -fx-font-size: 16px;";

            // Define o estado inicial (ex: "Hoje" começa ativo)
            hojeBtn.setStyle(activeTabStyle);
            semanaBtn.setStyle(inactiveTabStyle);
            mesBtn.setStyle(inactiveTabStyle);
            anoBtn.setStyle(inactiveTabStyle);

            // --- Criação da Caixa Vermelha com o Resultado ---
            VBox redBox = new VBox(10);
            redBox.setPadding(new Insets(10));
            redBox.setStyle("-fx-background-color: #af0e2c; -fx-background-radius: 10;");

            Text boldText = new Text("Ocupação média (todas as sessões): ");
            boldText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            boldText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

            double ocupacaoInicial = calcularOcupacaoPara("Hoje"); // Calcula para 'Hoje' (o botão inicial)
            Text ocupacaoText = new Text(String.format("%.1f", ocupacaoInicial) + "%");
            ocupacaoText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            ocupacaoText.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

            TextFlow textFlow = new TextFlow(boldText, ocupacaoText);
            redBox.getChildren().add(textFlow);

            // --- Adiciona ação para os botões (usa a redBox que já existe) ---
            for (Button btn : tabButtons) {
                btn.setOnAction(event -> {
                    // Atualiza o estilo dos botões
                    for (Button b : tabButtons) {
                        b.setStyle(inactiveTabStyle);
                    }
                    btn.setStyle(activeTabStyle);
                    btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));

                    // Calcular a ocupação com base no botão clicado
                    String filtroSelecionado = btn.getText();
                    double ocupacaoCalculada = calcularOcupacaoPara(filtroSelecionado);

                    // Atualizar caixa de resultado (redBox)
                    redBox.getChildren().clear();
                    Text boldTextNovo = new Text("Ocupação média (todas as sessões): ");
                    boldTextNovo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    boldTextNovo.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

                    Text ocupacaoTextNova = new Text(String.format("%.1f", ocupacaoCalculada) + "%");
                    ocupacaoTextNova.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                    ocupacaoTextNova.setFill(javafx.scene.paint.Color.web("#f2e8c6"));

                    TextFlow textFlowNovo = new TextFlow(boldTextNovo, ocupacaoTextNova);
                    redBox.getChildren().add(textFlowNovo);

                    System.out.println("Filtro aplicado: " + filtroSelecionado + " | Ocupação média: " + ocupacaoCalculada + "%");
                });
            }

            tabs.getChildren().addAll(tabButtons);

            // --- Adiciona tudo no contêiner principal ---
            filterContainer.getChildren().addAll(tabs, redBox);
        } else if ("Horário de Sessão".equals(selected)) {
            // Encontrar todos os filmes únicos que têm sessões nesta sala
            List<Movie> moviesNaSala = new ArrayList<>();
            if (room != null && room.getSessions() != null && !room.getSessions().isEmpty()) {
                for (Session session : room.getSessions()) {
                    if (!moviesNaSala.contains(session.getMovie())) {
                        moviesNaSala.add(session.getMovie());
                    }
                }
            }

            // VERIFICAÇÃO PRINCIPAL: Se não há filmes, exibe a mensagem e para.
            if (moviesNaSala.isEmpty()) {
                Label noMoviesLabel = new Label("Não há sessões programadas para esta sala.");
                noMoviesLabel.setStyle("-fx-text-fill: #f2e8c6; -fx-font-size: 14px; -fx-padding: 15px;");
                filterContainer.getChildren().add(noMoviesLabel);
                return; // Encerra a execução do método aqui
            }

            // --- Se encontramos filmes, continuamos a construir a UI ---

            // Definir estilos e criar componentes (como antes)
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

            // Configurar as ações dos botões (como antes)
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

            // Ativa o primeiro filme por padrão
            movieButtons.getFirst().fire();

            // Adiciona os componentes à tela
            filterContainer.getChildren().addAll(movieTabs, sessionDetailsContainer);
        }
    }

    /**
     * Limpa o contêiner de detalhes e exibe uma lista de todas as sessões
     * para um filme específico, buscando os dados através do MovieController.
     *
     * @param movie O filme cujas sessões serão exibidas.
     * @param container O VBox onde as informações das sessões serão adicionadas.
     */
    private void displaySessionsForMovie(Movie movie, VBox container) {
        container.getChildren().clear(); // Limpa a lista de sessões anterior
        List<Session> sessoesDoFilme = MovieController.getSessionsByMovie(movie.getId());

        // Verifica se a busca retornou alguma sessão
        if (sessoesDoFilme == null || sessoesDoFilme.isEmpty()) {
            Label noSessionsLabel = new Label("Não há sessões programadas para este filme.");
            noSessionsLabel.setStyle("-fx-text-fill: #f2e8c6;");
            container.getChildren().add(noSessionsLabel);
            return;
        }

        int sessionCounter = 1;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 2. Itera sobre a lista retornada pelo MovieController.
        for (Session session : sessoesDoFilme) {

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

    private double calcularOcupacaoPara(String filtro) {
        if (room == null || room.getSessions().isEmpty()) return 0.0;

        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int totalIngressosVendidos = 0;
        int totalIngressosOferecidos = 0;

        for (Session session : room.getSessions()) {
            LocalDate dataSessao;
            try {
                dataSessao = LocalDate.parse(session.getDate(), formatter);
            } catch (DateTimeParseException e) {
                System.err.println("Formato de data inválido na sessão: " + session.getDate());
                continue;
            }

            boolean incluir = false;

            switch (filtro) {
                case "Hoje":
                    incluir = dataSessao.equals(hoje);
                    break;
                case "Esta semana":
                    incluir = dataSessao.isAfter(hoje.with(java.time.DayOfWeek.MONDAY).minusDays(1)) &&
                            dataSessao.isBefore(hoje.with(java.time.DayOfWeek.SUNDAY).plusDays(1));
                    break;
                case "Este mês":
                    incluir = dataSessao.getMonth() == hoje.getMonth() &&
                            dataSessao.getYear() == hoje.getYear();
                    break;
                case "Este ano":
                    incluir = dataSessao.getYear() == hoje.getYear();
                    break;
            }

            if (incluir) {
                int vendidos = room.getTotalSeat() - session.getTotalAvailableSeats();
                totalIngressosVendidos += vendidos;
                totalIngressosOferecidos += room.getTotalSeat();
            }
        }

        if (totalIngressosOferecidos == 0) return 0.0;

        return ((double) totalIngressosVendidos / totalIngressosOferecidos) * 100.0;
    }
}
