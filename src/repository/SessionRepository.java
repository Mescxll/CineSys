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
 * @since 11/06/2025
 * @version 3.0
 */
public class SessionRepository {
    private final List<Session> sessions;
    private final String FILE_PATH = "data/sessions.txt";
    private final boolean useFilePersistence;

    /**
     * Construtor padrão. Opera em modo de memória, ideal para testes.
     */
    public SessionRepository() {
        this(false);
    }

    /**
     * Construtor principal que define o modo de operação.
     *
     * @param useFilePersistence Se true, o repositório lerá e salvará em arquivo.
     */
    public SessionRepository(boolean useFilePersistence) {
        this.sessions = new LinkedList<>();
        this.useFilePersistence = useFilePersistence;

        if (this.useFilePersistence) {
            ensureDataFileExists();
            loadFromFile();
        }
    }

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

    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File(FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length >= 7) {
                    int sessionId = Integer.parseInt(parts[0]);
                    LocalDate date = LocalDate.parse(parts[1]);
                    LocalTime time = LocalTime.parse(parts[2]);
                    int roomId = Integer.parseInt(parts[3]);
                    int movieId = Integer.parseInt(parts[4]);
                    double ticketValue = Double.parseDouble(parts[5]);
                    int totalAvailableSeats = Integer.parseInt(parts[6]);

                    Room room = RoomController.getRoomById(roomId);
                    Movie movie = MovieController.getMovieById(movieId);

                    if (room != null && movie != null) {
                        Session session = new Session(sessionId, date, time, room, movie, ticketValue, totalAvailableSeats);
                        this.sessions.add(session);
                    } else {
                        System.err.println("Erro ao carregar sessão: Sala ou Filme com ID não encontrado. Linha: " + line);
                    }
                }
            }
            System.out.println("Sessões carregadas do arquivo: " + FILE_PATH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ou analisar o arquivo de sessões: " + e.getMessage());
        }
    }

    private void saveToFile() {
        if (!useFilePersistence) return;

        try (PrintWriter writer = new PrintWriter(new File(FILE_PATH))) {
            for (Session session : this.sessions) {
                // Formato: sessionId;data;hora;salaId;filmeId;valorIngresso;assentosDisponiveis
                String line = String.format("%d;%s;%s;%d;%d;%f;%d",
                        session.getId(),
                        session.getDate().toString(), // Salva em formato AAAA-MM-DD
                        session.getTime().toString(), // Salva em formato HH:mm ou HH:mm:ss
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
            if (session.getId() == id) {
                return session;
            }
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
     *
     * @param date A data pela qual se quer filtrar as sessões.
     * @return Uma lista contendo todas as sessões da data informada.
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