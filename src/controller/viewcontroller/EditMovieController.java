package controller.viewcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import controller.business.MovieController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;
import models.Movie;

/**
 * Classe responsável por controlar a tela de alteração de um Filme.
 * @author Gabryelle Beatriz Duarte Moraes
 * @since 14/06/2024
 * @version 1.0
 */
public class EditMovieController implements Initializable{

    @FXML private TextField titleField;
    @FXML private TextField genreField;
    @FXML private TextField durationField;
    @FXML private TextField ratingField;
    @FXML private TextField synopsisField;

    private static Movie movie;
    
    /**
     * Inicializa o controlador.
     * 
     * @param url URL de localização do arquivo FXML, se necessário.
     * @param resourceBundle Conjunto de recursos localizados, se necessário.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        genreField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        durationField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        ratingField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        synopsisField.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        
        MainViews.addOnChangeScreenListener(new MainViews.OnChangeScreen() {
            @Override
            public void onScreenChanged(String newScreen, Object userDataObject) {
                if (userDataObject instanceof Movie) {
                    movie = (Movie) userDataObject;
                    titleField.setText(movie.getTitle());
                    genreField.setText(movie.getGenre());
                    durationField.setText(String.valueOf(movie.getDuration())); 
                    ratingField.setText(movie.getClassification()); 
                    synopsisField.setText(movie.getSynopsis());
                }
            }
        });
    }
   
    /**
     * Método que é chamado quando o botão "Voltar" é clicado e retorna para a tela anterior.
     * 
     * @param event Evento de ação do botão.
     */
    @FXML
    void backMovieControl(ActionEvent event) {
        MainViews.changeScreen("movieControl", null);
    }

    /**
     * Método que é chamado quando o botão "Editar" é clicado e edita as informações do Filme.
     * 
     * @param event Evento de ação do botão.
     */
    @FXML
    void editMovie(ActionEvent event) {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String duration = durationField.getText().trim();
        int drtn = Integer.parseInt(duration);
        String classification = ratingField.getText().trim();
        String synopsis = synopsisField.getText().trim();

        MovieController.updateMovie(movie.getId(), title, genre, drtn, classification, synopsis);
        titleField.clear();
        genreField.clear();
        durationField.clear();
        ratingField.clear();
        synopsisField.clear();
        MovieControlController.mostrarPopUp("alterado");
    }
}