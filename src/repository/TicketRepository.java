package repository;

import models.Ticket;

import java.util.LinkedList;
import java.util.List;

/**
 * Repositório para gerenciar os dados dos tickets.
 * Responsável por armazenar, recuperar e remover tickets.
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 2.0
 */
public class TicketRepository {
    private List<Ticket> tickets = new LinkedList<>();

    /**
     * Adiciona um ticket ao repositório.
     *
     * @param ticket O ticket a ser adicionado.
     */
    public void add(Ticket ticket){
        try {
            tickets.add(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca um ticket pelo ID.
     *
     * @param id O ID do ticket a ser buscado.
     * @return O ticket correspondente ao ID ou {@code null} se não encontrado.
     */
    public Ticket getById(int id){
        for(int i = 0; i < tickets.size(); i++){
            if(tickets.get(i).getId() == id){
                return tickets.get(i);
            }
        }
        return null;
    }

    /**
     * Atualiza um ticket selecionado
     *
     * @param id do ticket a ser atualizada
     * @param ticket novo ticket que será atualizado
     */
    public void update(int id, Ticket ticket){
        if(getById(id) == null)
            throw new IllegalArgumentException("Sessão não existe!");
        tickets.set(getIndex(id), ticket);
    }

    /**
     * Método auxiliar para pegar o index de um certo ticket
     *
     * @param id da sessão
     * @return se o id existir, retorna o index requerido
     *         caso não existe, retorna -1
     */
    private int getIndex(int id){
        for(int i = 0; i < tickets.size(); i++){
            if(tickets.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }

    /**
     * Retorna todos os tickets armazenados no repositório.
     *
     * @return Uma lista dinâmica contendo todos os tickets.
     */
    public LinkedList<Ticket> getAll(){
        return (LinkedList<Ticket>) tickets;
    }

    /**
     * Remove um ticket pelo ID.
     *
     * @param id O ID do ticket a ser removido.
     * @return {@code true} se o ticket foi removido com sucesso, {@code false} caso contrário.
     */
    public boolean removeById(int id){
        for(int i = 0; i < tickets.size(); i++){
            if (tickets.get(i).getId() == id) {
                try {
                    tickets.remove(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Remove todos os tickets do repositório.
     */
    public void clear(){
        tickets.clear();
    }
}