package repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import models.Session;

/**
 * Classe que gerencia as sessões (Session) do cinema.
 * Usamos uma LinkedList como "banco de dados".
 *
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 2.0
 */
public class    SessionRepository {
    private List<Session> sessions = new LinkedList<>();

    /**
     * Adiciona uma nova sessão a lista.
     *
     * @param session A sessão a ser adicionada.
     */
    public void add(Session session){
        try {
            sessions.add(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca uma sessão pelo seu ID.
     *
     * @param id Identificador da sessão.
     * @return A sessão com o ID fornecido, ou null se não existir.
     */
    public Session getById(int id){
        for(int i = 0; i < sessions.size(); i++){
            if(sessions.get(i).getId() == id){
                return sessions.get(i);
            }
        }
        return null;
    }

    /**
     * Atualiza uma sessão selecionada
     *
     * @param id da sessão a ser atualizada
     * @param session nova sessão que será atualizada
     */
    public void update(int id, Session session){
        if(getById(id) == null)
            throw new IllegalArgumentException("Sessão não existe!");
        sessions.set(getIndex(id), session);
    }

    /**
     * Método auxiliar para pegar o index de uma certa sessão
     *
     * @param id da sessão
     * @return se o id existir, retorna o index requerido
     *         caso não existe, retorna -1
     */
    private int getIndex(int id){
        for(int i = 0; i < sessions.size(); i++){
            if(sessions.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }

    /**
     * Retorna todas as sessões agendadas para uma dada data.
     *
     * @param date A data pela qual se quer filtrar as sessões.
     * @return Uma LinkedList contendo todas as sessões cuja data
     *         seja igual à informada (pode retornar lista vazia se não houver nenhuma).
     */
    public LinkedList<Session> getByDate(LocalDate date) {
        LinkedList<Session> sessionsByDate = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateString = date.format(formatter);

        for (Session session : sessions) {
            if (session.getDate().equals(dateString)) {
                sessionsByDate.add(session);
            }
        }
        return sessionsByDate;
    }

    /**
     * Retorna todas as sessões cadastradas.
     *
     * @return A LinkedList contendo todas as sessões.
     */
    public LinkedList<Session> getAll(){
        return (LinkedList<Session>) sessions;
    }
    
    /**
     * Remove a sessão com o ID especificado.
     *
     * @param id Identificador da sessão a ser removida.
     * @return true se a sessão foi removida; false se não encontrou nenhuma com aquele ID.
     */
    public boolean removeById(int id){
        for(int i = 0; i < sessions.size(); i++){
            if (sessions.get(i).getId() == id) {
                try {
                    sessions.remove(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Remove todas as sessões cadastradas.
     */
    public void clear(){
        sessions.clear();
    }
}