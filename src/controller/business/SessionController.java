package controller.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import models.Movie;
import models.Room;
import models.Session;
import repository.SessionRepository;
import services.SessionService;

/**
 * Classe de controle responsável pela lógica de negócio das sessões de cinema.
 * @author Kaique Silva Sousa
 * @since 11/06/2023
 * @version 2.0
 */
public class SessionController {

    private static SessionService sessionService = new SessionService(new SessionRepository(true));

    /**
     * Método para inicializar o controller com suas dependências.
     * Deve ser chamado no início da aplicação.
     */
    public static void initialize(SessionRepository repository) {
        if (sessionService == null) {
            sessionService = new SessionService(repository);
        }
    }

    /**
     * Adiciona uma nova sessão ao sistema.
     * @param date Data da sessão (não pode ser anterior à data atual).
     * @param time Horário da sessão (HH:mm:ss).
     * @param room Sala onde a sessão ocorrerá (não pode ser {@code null}).
     * @param movie Filme que será exibido (não pode ser {@code null}).
     * @param ticketValue Valor do ticket (não pode ser {@code null} ou negativo).
     */
     public static void addSession(String date, String time, Room room, Movie movie, Double ticketValue){
        LocalDate dateParsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime timeParsed = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        sessionService.addSession(dateParsed, timeParsed, room, movie, ticketValue);
    }

    /**
     * Adiciona uma nova sessão ao sistema.
     * @param date Data da sessão (não pode ser anterior à data atual).
     * @param time Horário da sessão (HH:mm:ss).
     * @param room Sala onde a sessão ocorrerá (não pode ser {@code null}).
     * @param movie Filme que será exibido (não pode ser {@code null}).
     * @param ticketValue Valor do ticket (não pode ser {@code null} ou negativo).
     * @param totalAvailabelSeats total de assentos disponíveis
     */
    public static void addSession(String date, String time, Room room, Movie movie, Double ticketValue, int totalAvailabelSeats){
        LocalDate dateParsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime timeParsed = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        sessionService.addSession(dateParsed, timeParsed, room, movie, ticketValue, totalAvailabelSeats);
    }

    /**
     * Atualiza uma sessão existente.
     * @param id ID da sessão a ser atualizada.
     * @param date Data da sessão (não pode ser anterior à data atual).
     * @param time Horário da sessão (HH:mm:ss).
     * @param room Sala onde a sessão ocorrerá (não pode ser {@code null}).
     * @param movie Filme que será exibido (não pode ser {@code null}).
     * @param ticketValue Valor do ticket (não pode ser {@code null} ou negativo).
     */
    public static void updateSession(int id, String date, String time, Room room, Movie movie, Double ticketValue){
        LocalDate dateParsed = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalTime timeParsed = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        sessionService.updateSession(id, dateParsed, timeParsed, room, movie, ticketValue);
    }

    /**
     * Pede ao repositório para salvar o estado atual de um objeto de sessão.
     * Útil quando o estado da sessão (como assentos disponíveis) é modificado por outro serviço.
     *
     * @param session O objeto sessão com seu estado atualizado.
     */
    public static void updateSession(Session session) {
        sessionService.updateSession(session);
    }

    /**
     * Pega uma sessão pelo ID.
     * @param id ID da sessão a ser buscada.
     * @return A sessão encontrada, ou {@code null} se não existir.
     */
    public static Session getSessionById(int id){
        return sessionService.getSessionById(id);
    }
    
    /**
     * Pega todas as sessões cadastradas no sistema.
     * @return Uma lista de todas as sessões.
     */
    public static LinkedList<Session> getAllSessions(){
        return sessionService.getAllSessions();
    }

    /**
     * Remove uma sessão pelo ID.
     * @param id ID da sessão a ser removida.
     * @return retorna a sessão que foi removida.
     */
    public static Session removeSession(int id){
        return sessionService.removeSession(id);
    }

    /**
     * Remove todas as sessões cadastradas no sistema.
     */
    public static void removeAllSessions(){
        sessionService.removeAllSessions();
    }
}
