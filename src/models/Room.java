package models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int _idGenerator = 1;
    private int id;
    private int totalSeat;
    // IMPORTANTE: O campo 'sessions' não pode ser salvo diretamente no arquivo da Sala.
    // Ele será reconstruído ao carregar as Sessões. Para isso, o marcamos como 'transient'.
    private transient List<Session> sessions;

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
        this.sessions = new LinkedList<>();
    }
    /**
     * Construtor para reconstruir salas a partir de dados salvos.
     * Recebe o ID existente do arquivo e ajusta o gerador de IDs.
     */
    public Room(int id, int totalSeat) {
        this.id = id;
        this.totalSeat = totalSeat;
        this.sessions = new LinkedList<>();

        if (id >= _idGenerator) {
            _idGenerator = id + 1;
        }
    }

    /**
     * Garante que a lista de sessões nunca seja nula.
     * Este método privado é chamado antes de qualquer operação na lista.
     */
    private void ensureSessionsListExists() {
        if (this.sessions == null) {
            this.sessions = new LinkedList<>();
        }
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
    public void addSession(Session session) throws Exception {
        ensureSessionsListExists();
        sessions.add(session);
    }

    /**
     * Remove e retorna a próxima sessão da lista.
     * 
     * Remove a sessão que está no início da lista (primeira a ser adicionada)
     * seguindo o princípio FIFO.
     * 
     * @return a próxima sessão da fila, ou null se a fila estiver vazia
     */
    public Session removeSession() {
        ensureSessionsListExists(); // Garante que a lista foi inicializada
        if (!sessions.isEmpty()) {
            return sessions.removeFirst();
        }
        return null;
    }

    /**
     * Retorna a fila completa de sessões da sala.
     * 
     * Este método permite acesso direto à estrutura de dados que armazena
     * as sessões, possibilitando operações mais complexas como consultas
     * e iterações.
     * 
     * @return a lista dinâmica contendo todas as sessões da sala
     */
    public List<Session> getSessions() {
        ensureSessionsListExists();
        return sessions;
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