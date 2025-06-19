package services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import models.Movie;
import models.Room;
import models.Session;
import repository.SessionRepository;

/**
 * Classe de serviço responsável pela lógica de negócio das sessões de cinema.
 * Faz validações (data, conflito de horário na mesma sala, campos obrigatórios, etc.)
 * antes de delegar ao repositório.
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 2.0
 */
public class SessionService {
    private final SessionRepository sessionRepository;

    /**
     * Construtor do SessionService.
     *
     * @param sessionRepository O repositório de sessões a ser utilizado.
     */
    public SessionService(SessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    /**
     * Cria e adiciona uma nova sessão ao sistema, após realizar validações de negócio.
     *
     * @param date        Data da sessão (não pode ser anterior à data atual).
     * @param time        Horário da sessão (HH:mm:ss).
     * @param room        Sala onde a sessão ocorrerá (não pode ser {@code null}).
     * @param movie       Filme que será exibido (não pode ser {@code null}).
     * @param ticketValue Valor do ticket (não pode ser {@code null} ou negativo).
     * @throws IllegalArgumentException Se alguma validação falhar:
     *                                  <ul>
     *                                    <li>Data anterior à data atual;</li>
     *                                    <li>Room, Movie ou ticketValue {@code null};</li>
     *                                    <li>ticketValue menor que zero;</li>
     *                                    <li>Sessão conflito de horário na mesma sala.</li>
     *                                  </ul>
     */
    public void addSession(LocalDate date, LocalTime time, Room room, Movie movie, Double ticketValue){
        //Verificação se a data é passada
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data da sessão não pode ser anterior à data atual!");
        }

        if (Objects.isNull(room) || Objects.isNull(movie) || Objects.isNull(ticketValue)) {
            throw new IllegalArgumentException("Os campos 'Room', 'Movie' e 'ticketValue' são obrigatórios!");
        }

        if (ticketValue < 0) {
            throw new IllegalArgumentException("O valor do ticket não pode ser negativo!");
        }

        Session newSession = new Session(date, time, room, movie, ticketValue);

        // Verifica se já existe uma sessão com horário conflitante na mesma sala        
        List<Session> sessionsInRoom = sessionRepository.getAll();
        for(int i = 0; i < sessionsInRoom.size(); i++){
            if( sessionsInRoom.get(i).getDate().equals(newSession.getDate()) &&
                sessionsInRoom.get(i).getTime().equals(newSession.getTime())){
                    throw new IllegalArgumentException("Já existe uma sessão nesse horário para a sala selecionada!");
            }
        }

        sessionRepository.add(newSession);
        //Adiciona a sessão na fila de sessões daquela sala.
        try {
            room.addSession(newSession);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cria e adiciona uma nova sessão ao sistema, após realizar validações de negócio.
     *
     * @param date        Data da sessão (não pode ser anterior à data atual).
     * @param time        Horário da sessão (HH:mm:ss).
     * @param room        Sala onde a sessão ocorrerá (não pode ser {@code null}).
     * @param movie       Filme que será exibido (não pode ser {@code null}).
     * @param ticketValue Valor do ticket (não pode ser {@code null} ou negativo).
     * @param totalAvailableSeats total de assentos disponíveis
     * @throws IllegalArgumentException Se alguma validação falhar:
     *                                  <ul>
     *                                    <li>Data anterior à data atual;</li>
     *                                    <li>Room, Movie ou ticketValue {@code null};</li>
     *                                    <li>ticketValue menor que zero;</li>
     *                                    <li>Sessão conflito de horário na mesma sala.</li>
     *                                  </ul>
     */
    public void addSession(LocalDate date, LocalTime time, Room room, Movie movie, Double ticketValue, int totalAvailableSeats){
        //Verificação se a data é passada
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data da sessão não pode ser anterior à data atual!");
        }

        if (Objects.isNull(room) || Objects.isNull(movie) || Objects.isNull(ticketValue)) {
            throw new IllegalArgumentException("Os campos 'Room', 'Movie' e 'ticketValue' são obrigatórios!");
        }

        if (ticketValue < 0) {
            throw new IllegalArgumentException("O valor do ticket não pode ser negativo!");
        }

        if(totalAvailableSeats < 0) {
            throw new IllegalArgumentException("O total de assentos disponíveis não pode ser negativo!");
        }

        Session newSession = new Session(date, time, room, movie, ticketValue, totalAvailableSeats);

        // Verifica se já existe uma sessão com horário conflitante na mesma sala
        List<Session> sessionsInRoom = sessionRepository.getAll();
        for(int i = 0; i < sessionsInRoom.size(); i++){
            if( sessionsInRoom.get(i).getDate().equals(newSession.getDate()) &&
                    sessionsInRoom.get(i).getTime().equals(newSession.getTime())){
                throw new IllegalArgumentException("Já existe uma sessão nesse horário para a sala selecionada!");
            }
        }

        sessionRepository.add(newSession);
        //Adiciona a sessão na fila de sessões daquela sala.
        try {
            room.addSession(newSession);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Atualiza uma sessão selecionada.
     * @param id da sessão a ser selecionada
     * @param date        Data da sessão (não pode ser anterior à data atual).
     * @param time        Horário da sessão (HH:mm:ss).
     * @param room        Sala onde a sessão ocorrerá (não pode ser {@code null}).
     * @param movie       Filme que será exibido (não pode ser {@code null}).
     * @param ticketValue Valor do ticket (não pode ser {@code null} ou negativo).
     * @throws IllegalArgumentException Se alguma validação falhar:
     *                                  <ul>
     *                                    <li>Data anterior à data atual;</li>
     *                                    <li>Room, Movie ou ticketValue {@code null};</li>
     *                                    <li>ticketValue menor que zero;</li>
     *                                    <li>Sessão conflito de horário na mesma sala.</li>
     *                                  </ul>
     */
    public void updateSession(int id, LocalDate date, LocalTime time, Room room, Movie movie, Double ticketValue){
        Session session = getSessionById(id);
        if(session == null)
            throw new IllegalArgumentException("A sessão selecionada não existe!");
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data da sessão não pode ser anterior à data atual!");
        }

        if (Objects.isNull(room) || Objects.isNull(movie) || Objects.isNull(ticketValue)) {
            throw new IllegalArgumentException("Os campos 'Room', 'Movie' e 'ticketValue' são obrigatórios!");
        }

        if (ticketValue < 0) {
            throw new IllegalArgumentException("O valor do ticket não pode ser negativo!");
        }
        session.setMovie(movie);
        session.setRoom(room);
        session.setTime(time);
        session.setDate(date);
        session.setTicketValue(ticketValue);
    }

    /**
     * Retorna a sessão correspondente ao ID fornecido.
     *
     * @param id Código identificador da sessão.
     * @return A sessão encontrada, ou {@code null} se não existir.
     */
    public Session getSessionById(int id){
        return sessionRepository.getById(id);
    }

    /**
     * Retorna todas as sessões cadastradas sem filtro.
     *
     * @return Uma LinkedList contendo todas as sessões.
     */
    public LinkedList<Session> getAllSessions(){
        return sessionRepository.getAll();
    }

    /**
     * Retorna todas as sessões agendadas para uma dada data.
     *
     * @param date Data para filtrar as sessões.
     * @return Uma LinkedList contendo as sessões cuja data coincide com a informada.
     */
    public LinkedList<Session> getSessionsByDate(LocalDate date){
        return sessionRepository.getByDate(date);
    }
    
    /**
     * Remove a sessão com o ID especificado.
     *
     * @param id Identificador da sessão a ser removida.
     */
    public Session removeSession(int id){
        Session sessionReturn = sessionRepository.getById(id);
        if(!sessionRepository.removeById(id)){
            throw new IllegalArgumentException("Sessão não existe!");
        }
        return sessionReturn;
    }

    /**
     * Remove todas as sessões cadastradas.
     */
    public void removeAllSessions(){
        sessionRepository.clear();
    }
}