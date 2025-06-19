package models;

import structures.queue.GenericDynamicQueue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe que representa uma sala de cinema no sistema.
 * 
 * Esta classe gerencia as informações básicas de uma sala, incluindo:
 * - Identificação única da sala
 * - Capacidade total de assentos
 * - Fila de sessões programadas para a sala
 * 
 * A classe utiliza um gerador automático de IDs para garantir que cada sala
 * tenha um identificador único no sistema.
 * 
 * @author Maria Eduarda Campos
 * @since 25/05/2025
 * @version 1.0
 */
public class Room {
    /** Gerador automático de IDs para as salas */
    private static int _idGenerator = 1;
    
    /** Identificador único da sala */
    private int id;
    
    /** Número total de assentos disponíveis na sala */
    private int totalSeat;
    
    /** Fila dinâmica que armazena as sessões programadas para esta sala */
    private Queue<Session> sessions;

    /**
     * Construtor da classe Room.
     * 
     * Cria uma nova sala com o número especificado de assentos.
     * O ID da sala é gerado automaticamente de forma sequencial.
     * Inicializa uma fila vazia para as sessões.
     * 
     * @param totalSeat número total de assentos da sala (deve ser maior que 0)
     */
    public Room(int totalSeat){
        this.totalSeat = totalSeat;
        this.id = _idGenerator++;
        this.sessions = new ArrayDeque<>();
    }

    /**
     * Retorna o identificador único da sala.
     * 
     * @return o ID da sala
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o número total de assentos da sala.
     * 
     * @return a capacidade total de assentos da sala
     */
    public int getTotalSeat() {
        return totalSeat;
    }

    /**
     * Adiciona uma nova sessão à fila de sessões da sala.
     * 
     * A sessão é adicionada ao final da fila, seguindo a ordem FIFO
     * (First In, First Out).
     * 
     * @param session a sessão a ser adicionada à sala
     * @throws Exception se ocorrer erro ao adicionar a sessão na fila
     */
    public void addSession(Session session) throws Exception{
        sessions.add(session);
    }

    /**
     * Remove e retorna a próxima sessão da fila.
     * 
     * Remove a sessão que está no início da fila (primeira a ser adicionada)
     * seguindo o princípio FIFO.
     * 
     * @return a próxima sessão da fila, ou null se a fila estiver vazia
     */
    public Session removeSession(){
        return sessions.remove();
    }

    /**
     * Retorna a fila completa de sessões da sala.
     * 
     * Este método permite acesso direto à estrutura de dados que armazena
     * as sessões, possibilitando operações mais complexas como consultas
     * e iterações.
     * 
     * @return a fila dinâmica contendo todas as sessões da sala
     */
    public ArrayDeque<Session> getSessions() {
        return (ArrayDeque<Session>) sessions;
    }

    /**
     * Retorna uma representação textual da sala.
     * 
     * A string inclui o ID da sala, número total de assentos e
     * informações sobre as sessões programadas.
     * 
     * @return string formatada com as informações da sala
     */
    @Override
    public String toString() {
        return "Room " + id + ":" + 
        "\nTotal Seat=" + totalSeat + 
        "\nSessions="+ sessions;
    }

    /**
     * Reinicia o gerador de IDs para o valor inicial (1).
     * 
     * Este método é útil principalmente para testes ou quando se deseja
     * reinicializar o sistema de numeração das salas.
     * 
     * <strong>Atenção:</strong> Este método afeta todas as instâncias da classe
     * e deve ser usado com cuidado em ambiente de produção.
     */
    public static void resetIdGenerator() {
        _idGenerator = 1;
    }
}