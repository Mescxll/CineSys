package repository;

import controller.business.MovieController;
import controller.business.RoomController;
import models.Movie;
import models.Room;
import models.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe que gerencia as sessões (Session) do cinema.
 * Pode operar em modo de memória ou com persistência em arquivo de texto.
 *
 * @author Thiago Ferreira Ribeiro
 * @author Vinícius Nunes de Andrade
 * @since 11/06/2025
 * @version 3.1
 */
public class SessionRepository {
    private final List<Session> sessions;
    private final String FILE_PATH = "data/sessions.txt";
    private final boolean useFilePersistence;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Construtor padrão que inicializa o repositório em modo de memória.
     */
    public SessionRepository() { this(false); }

    /**
     * Construtor principal que define o modo de operação do repositório.
     *
     * @param useFilePersistence Se 'true', o repositório lerá e salvará dados
     * em um arquivo de texto. Se 'false', operará
     * apenas em memória.
     */
    public SessionRepository(boolean useFilePersistence) {
        this.sessions = new LinkedList<>();
        this.useFilePersistence = useFilePersistence;
        if (this.useFilePersistence) {
            ensureDataFileExists();
            loadFromFile();
        }
    }

    /**
     * Garante que o diretório 'data' e o arquivo de sessões existam no disco.
     * Se não existirem, eles são criados para evitar erros de "Arquivo Não Encontrado"
     * na primeira execução da aplicação.
     */
    private void ensureDataFileExists() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();
            File sessionsFile = new File(FILE_PATH);
            if (!sessionsFile.exists()) sessionsFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Erro crítico ao criar diretório ou arquivo de dados: " + e.getMessage());
        }
    }

    /**
     * Carrega todas as sessões do arquivo de texto para a lista em memória.
     * Este método é chamado pelo construtor quando a persistência em arquivo está
     * ativada. Ele analisa cada linha do arquivo, reconstrói os objetos 'Session'
     * e suas dependências (Room e Movie).
     */
    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File(FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length >= 7) {
                    int sessionId = Integer.parseInt(parts[0]);
                    LocalDate date = LocalDate.parse(parts[1], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(parts[2], TIME_FORMATTER);

                    int roomId = Integer.parseInt(parts[3]);
                    int movieId = Integer.parseInt(parts[4]);
                    double ticketValue = Double.parseDouble(parts[5]);
                    int totalAvailableSeats = Integer.parseInt(parts[6]);

                    Room room = RoomController.getRoomById(roomId);
                    Movie movie = MovieController.getMovieById(movieId);

                    if (room != null && movie != null) {
                        Session session = new Session(sessionId, date, time, room, movie, ticketValue, totalAvailableSeats);
                        this.sessions.add(session);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ou analisar o arquivo de sessões: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Salva a lista de sessões em memória de volta para o arquivo de texto.
     */
    private void saveToFile() {
        if (!useFilePersistence) return;

        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {
            for (Session session : this.sessions) {

                String line = String.format(java.util.Locale.US, "%d;%s;%s;%d;%d;%f;%d",
                        session.getId(),
                        session.getDate(),
                        session.getTime(),
                        session.getRoom().getId(),
                        session.getMovie().getId(),
                        session.getTicketValue(),
                        session.getTotalAvailableSeats());
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao salvar sessões no arquivo: " + e.getMessage());
        }
    }

    /**
     * Adiciona uma nova sessão à lista e salva no arquivo.
     *
     * @param session A sessão a ser adicionada.
     */
    public void add(Session session) {
        sessions.add(session);
        saveToFile();
    }

    /**
     * Busca uma sessão pelo seu ID de forma eficiente.
     *
     * @param id Identificador da sessão.
     * @return A sessão com o ID fornecido, ou null se não existir.
     */
    public Session getById(int id) {
        for (Session session : sessions) {
            if (session.getId() == id) return session;
        }
        return null;
    }

    /**
     * Atualiza uma sessão selecionada e salva no arquivo.
     *
     * @param id O ID da sessão a ser atualizada.
     * @param updatedSession A sessão com as novas informações.
     */
    public void update(int id, Session updatedSession) {
        int index = getIndex(id);
        if (index == -1) {
            throw new IllegalArgumentException("Sessão com ID " + id + " não existe!");
        }
        sessions.set(index, updatedSession);
        saveToFile();
    }

    /**
     * Método auxiliar para pegar o índice de uma certa sessão.
     */
    private int getIndex(int id) {
        int index = 0;
        for (Session session : sessions) {
            if (session.getId() == id) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Retorna todas as sessões agendadas para uma dada data.
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
     * @return Uma List contendo todas as sessões.
     */
    public LinkedList<Session> getAll(){
        return (LinkedList<Session>) sessions;
    }

    /**
     * Remove a sessão com o ID especificado e salva no arquivo.
     *
     * @param id Identificador da sessão a ser removida.
     * @return true se a sessão foi removida; false caso contrário.
     */
    public boolean removeById(int id) {
        Iterator<Session> iterator = sessions.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (session.getId() == id) {
                iterator.remove();
                saveToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Remove todas as sessões cadastradas e salva o estado vazio no arquivo.
     */
    public void clear() {
        sessions.clear();
        saveToFile();
    }
}