package controller.viewcontroller;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import controller.business.MovieController;

/**
 * Classe responsável por controlar a tela de cadastro de um filme.
 * 
 * @author Gabryelle Beatriz Duarte Moraes
 * @author Maria Eduarda Campos
 * @since 01/06/2024
 * @version 2.0
 */
public class RegisterMovieController implements Initializable {

    @FXML private TextField titleField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private TextField ratingField;
    @FXML private TextField synopsisField;

    /**
     * Add commentMore actions
     * inicializa mudando a cor do texto e do fundo dos campos de texto
     * 
     * @param url O URL de onde o controlador foi carregado.
     * @param rb  O ResourceBundle associado ao controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Muda a cor do texto e do fundo dos campos de texto
        titleField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        genreField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        durationField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        ratingField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        synopsisField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
    }

    /**
     * Reseta a tela de controle de filmes.
     * 
     * @param event evento de clique no botão de voltar
     */
    @FXML
    void backMovieControl(ActionEvent event) {
        MainViews.changeScreen("movieControl", null);
    }

    /**
     * Cadastra um filme no sistema.
     * 
     * @param event evento de clique no botão de cadastrar filme
     */
    @FXML
    void registerMovie(ActionEvent event) {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String duration = durationField.getText().trim();
        int drtn = Integer.parseInt(duration);
        String classification = ratingField.getText().trim();
        String synopsis = synopsisField.getText().trim();

        if (title.isEmpty() || genre.isEmpty() || duration.isEmpty() || classification.isEmpty() || synopsis.isEmpty()) {
            return;
        } else {
            MovieController.addMovie(title, genre, drtn, classification, synopsis);
            titleField.clear();
            genreField.clear();
            durationField.clear();
            ratingField.clear();
            synopsisField.clear();
            MovieControlController.mostrarPopUp("cadastrado");
        }
    }
}