package controller.viewcontroller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.tools.javac.Main;
import controller.business.ClientController;
import controller.business.MovieController;
import controller.business.RoomController;
import controller.business.SessionController;
import controller.viewcontroller.SessionControlController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import models.Movie;

/**
 * Classe responsável por controlar a tela de cadastro de uma sessão.
 * 
 * @author Carlos Moreira
 * @since 14/06/2025
 * @version 2.0
 */
public class RegisterSessionController implements Initializable {

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtMovieId;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtRoom;

    @FXML
    private TextField txtTime;


    /**
     * Add commentMore actions
     * inicializa mudando a cor do texto e do fundo dos campos de texto
     * 
     * @param url O URL de onde o controlador foi carregado.
     * @param rb  O ResourceBundle associado ao controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtDate.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        txtMovieId.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        txtPrice.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        txtRoom.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
        txtTime.setStyle("-fx-text-fill: white !important; -fx-background-color: #03002C !important;");
    }

    /**
     * Retorna para a tela de controle de sessões.
     * 
     * @param event evento de clique no botão de voltar
     */
    @FXML
    void backSessionController(ActionEvent event) {
        MainViews.changeScreen("sessionControl", null);
    }

    /**
     * Cadastra uma sessão no sistema.
     * 
     * @param event evento de clique no botão de cadastrar sessão
     */
    @FXML
    void registerSession(ActionEvent event) {
        String date = txtDate.getText().trim();
        String time = txtTime.getText().trim();
        String room = txtRoom.getText().trim();
        String movie = txtMovieId.getText().trim();
        int id = Integer.parseInt(movie);
        String ticketPrice = txtPrice.getText().trim();

        if (!date.isEmpty() && !time.isEmpty() && !room.isEmpty() && !movie.isEmpty() && !ticketPrice.isEmpty()) {
            int roomId = Integer.parseInt(room);
            double ticketPriceValue = Double.parseDouble(ticketPrice);
            SessionController.addSession(date, time, RoomController.getRoomById(roomId), MovieController.getMovieById(id), ticketPriceValue);
            txtMovieId.clear();
            txtDate.clear();
            txtTime.clear();
            txtRoom.clear();
            txtPrice.clear();
            SessionControlController.mostrarPopUp("cadastrada");
        }
    }

}
