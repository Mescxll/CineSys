package controller.viewcontroller;

import controller.business.MovieController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import models.Movie;
import javafx.fxml.Initializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.CheckBox; 
import javafx.scene.control.TableCell;
import java.util.LinkedList;

/**
 * Classe responsável por controlar a tela de Filmes.
 * 
 * @author Gabryelle Beatriz Duarte Moraes
 * @author Maria Eduarda Campos
 * @since 14/06/2024
 * @version 4.0
 */
public class MovieControlController implements Initializable, MainViews.OnChangeScreen {

    /**
     * Inicializa o controlador da tela de controle de filmes.
     */
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, Boolean> selectColumn;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, String> genreColumn;
    @FXML
    private TableColumn<Movie, String> durationColumn;
    @FXML
    private TableColumn<Movie, String> ratingColumn;
    @FXML
    private TableColumn<Movie, String> synopsisColumn;

    private final ObservableList<Movie> selectedMovies = FXCollections.observableArrayList();
    private ObservableList<Movie> moviesForTable;
    private final Map<Movie, SimpleBooleanProperty> movieSelectionMap = new HashMap<>();

    /**
     * Inicializa o controlador.
     * 
     * @param url URL de localização do arquivo FXML, se necessário.
     * @param resourceBundle Conjunto de recursos localizados, se necessário.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        moviesForTable = FXCollections.observableArrayList();

        titleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));
        genreColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGenre()));
        ratingColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getClassification()));
        synopsisColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSynopsis()));

        durationColumn.setCellValueFactory(cell -> {
            int durationMin = cell.getValue().getDuration();
            double durationHours = durationMin / 60.0;
            return new SimpleStringProperty(String.format("%.2f", durationHours));
        });

        selectColumn.setCellValueFactory(cellData -> {
            Movie movie = cellData.getValue();
            SimpleBooleanProperty selectedProp = movieSelectionMap.computeIfAbsent(movie, k -> {
                SimpleBooleanProperty prop = new SimpleBooleanProperty(false);
                prop.addListener((obs, oldValue, newValue) -> {
                    if (newValue) {
                        selectedMovies.add(movie);
                    } else {
                        selectedMovies.remove(movie);
                    }
                });
                return prop;
            });
            selectedProp.set(selectedMovies.contains(movie));
            return selectedProp;
        });

        selectColumn.setCellFactory(column -> new TableCell<Movie, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            private SimpleBooleanProperty currentProp;
            { 
                checkBox.setOnAction(event -> {
                    if (currentProp != null) {
                        currentProp.set(checkBox.isSelected());
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    if (currentProp != null) {
                        checkBox.selectedProperty().unbindBidirectional(currentProp);
                        currentProp = null;
                    }
                } else {
                    Movie movie = getTableRow().getItem(); 
                    SimpleBooleanProperty propFromMap = movieSelectionMap.get(movie); 

                    if (propFromMap != null) {
                        if (currentProp != null) {
                            checkBox.selectedProperty().unbindBidirectional(currentProp);
                        }
                        currentProp = propFromMap;
                        checkBox.selectedProperty().bindBidirectional(currentProp);
                        setGraphic(checkBox);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        MainViews.addOnChangeScreenListener(this);
        refreshTable();
    }

    /**
     * Chama o refreshTable() toda vez que a tela for chamada
     * 
     * @param newScreen      tela de mudança
     * @param userDataObject dados passados
     */
    @Override
    public void onScreenChanged(String newScreen, Object userDataObject) {
        if (newScreen.equals("movieControl")) {
            refreshTable();
        }
    }

    /**
     * Atualiza a tabela de filmes.
     */
    private void refreshTable() {
        List<Movie> currentlySelectedCopy = new ArrayList<>(selectedMovies);
        selectedMovies.clear();
        moviesForTable.clear();

        List<Movie> currentMoviesFromRepo = MovieController.getAllMovies();

        if (currentMoviesFromRepo != null) {
            for (Movie movie : currentMoviesFromRepo) {
                moviesForTable.add(movie);

                SimpleBooleanProperty prop = movieSelectionMap.computeIfAbsent(movie, k -> {
                    SimpleBooleanProperty newProp = new SimpleBooleanProperty(false);
                    newProp.addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            selectedMovies.add(movie);
                        } else {
                            selectedMovies.remove(movie);
                        }
                    });
                    return newProp;
                });

                boolean wasSelected = currentlySelectedCopy.contains(movie);
                if (prop.get() != wasSelected) {
                    prop.set(wasSelected);
                }

                if (prop.get()) {
                    selectedMovies.add(movie);
                }
            }
        }
        movieTable.setItems(moviesForTable);
        movieTable.refresh();
    }

    /**
     * Mostra uma janela de confirmação após a ação de exclusão.
     * * @param acao Ação realizada.
     */
    public static void mostrarPopUp(String acao) {
        try {
            FXMLLoader loader = new FXMLLoader(MovieControlController.class.getResource("/gui/PopUpMovies.fxml"));
            Parent root = loader.load();

            PopUpMovieController controller = loader.getController();
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
     * Método chamado quando o botão "Cadastrar Filme" é clicado e muda para a tela de cadastro de filmes.
     * @param event evento de clique do botão
     */
    @FXML
    void registerMovie(ActionEvent event) {
        MainViews.changeScreen("registerMovie", null);
    }

    /**
     * Método chamado quando o botão "Excluir Filme" é clicado e chama o método handleDelete.
     * @param event evento de clique do botão
     */
    @FXML
    void deleteMovie(ActionEvent event) {
        if (selectedMovies.isEmpty()) {
            return;
        }
        List<Movie> moviesToDelete = new ArrayList<>(selectedMovies);
        for (Movie movie : moviesToDelete) {
            MovieController.removeMovieById(movie.getId());
            movieSelectionMap.remove(movie);
        }

        selectedMovies.clear();
        refreshTable();
        mostrarPopUp("excluído");
    }

    /**
     * Método chamado quando o botão "Editar" é clicado.
     * @param event evento de clique do botão
     */
    @FXML
    void editMovie(ActionEvent event) {
        if (selectedMovies.isEmpty() || selectedMovies.size() > 1) {
            return;
        }

        Movie movieToEdit = selectedMovies.get(0);
        MainViews.changeScreen("movieEdit", movieToEdit);
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
     * Método que abre a Tela de Controle de Clientes.
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
}