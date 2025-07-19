package repository;

import controller.business.MovieController;
import controller.business.RoomController;
import models.Movie;
import models.Room;
import models.Session;

import java.io.*; // Import genérico para todas as classes de I/O
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que gerencia as sessões (Session) do cinema usando serialização.
 *
 * @author Thiago Ferreira Ribeiro
 * @author Vinícius Nunes de Andrade
 * @since 11/06/2025
 * @version 4.0
 */
public class SessionRepository {
    private List<Session> sessions;
    private final String FILE_PATH = "data/sessions.ser";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    /**
     * Construtor do repositório.
     * Tenta carregar as sessões do arquivo ao ser instanciado.
     */
    public SessionRepository() {
        loadFromFile();
    }

    /**
     * Carrega a lista de sessões de um arquivo binário.
     * Se o arquivo não existir ou estiver vazio, inicia com uma lista nova.
     * Também repopula as filas de sessão das salas.
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        new File("data").mkdirs();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            this.sessions = (List<Session>) ois.readObject();
            System.out.println("Sessões carregadas do arquivo serializado: " + FILE_PATH);

            for (Session session : this.sessions) {
                Room room = session.getRoom();
                if (room != null) {
                    try {
                        room.addSession(session);
                    } catch (Exception e) {
                        System.err.println("Aviso: Falha ao enfileirar sessão " + session.getId() + " na sala " + room.getId() + " durante o carregamento.");
                    }
                }
            }

        } catch (FileNotFoundException | EOFException e) {
            this.sessions = new LinkedList<>();
            System.out.println("Arquivo de sessões não encontrado ou vazio. Iniciando com repositório novo.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro crítico ao carregar sessões do arquivo. Iniciando com repositório vazio.");
            e.printStackTrace();
            this.sessions = new LinkedList<>();
        }
    }

    /**
     * Salva a lista de sessões em memória em um arquivo binário.
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.sessions);
        } catch (IOException e) {
            System.err.println("Erro ao salvar sessões no arquivo: " + e.getMessage());
        }
    }

    /**
     * Adiciona uma nova sessão à lista e salva no arquivo.
     * @param session A sessão a ser adicionada.
     */
    public void add(Session session) {
        sessions.add(session);
        saveToFile();
    }

    /**
     * Retorna uma sessão pelo seu ID.
     * @param id O ID da sessão.
     * @return A sessão encontrada ou null.
     */
    public Session getById(int id) {
        for (Session session : sessions) {
            if (session.getId() == id) return session;
        }
        return null;
    }

    /**
     * Atualiza uma sessão na lista e salva no arquivo.
     * @param sessionToUpdate O objeto Sessão com as informações atualizadas.
     */
    public void update(Session sessionToUpdate) {
        if (sessionToUpdate == null) return;
        int index = getIndex(sessionToUpdate.getId());
        if (index != -1) {
            sessions.set(index, sessionToUpdate);
            saveToFile();
        }
    }

    private int getIndex(int id) {
        int index = 0;
        for (Session session : sessions) {
            if (session.getId() == id) return index;
            index++;
        }
        return -1;
    }

    /**
     * Retorna todas as sessões agendadas para uma dada data.
     * @param date A data (como objeto LocalDate) pela qual se quer filtrar.
     * @return Uma lista contendo as sessões da data informada.
     */
    public LinkedList<Session> getByDate(LocalDate date) {
        LinkedList<Session> sessionsByDate = new LinkedList<>();

        String dateToCompare = date.format(DATE_FORMATTER);

        for (Session session : sessions) {
            if (session.getDate().equals(dateToCompare)) {
                sessionsByDate.add(session);
            }
        }
        return sessionsByDate;
    }

    /**
     * Retorna todas as sessões cadastradas.
     *
     * @return Uma lista contendo todas as sessões.
     */
    public LinkedList<Session> getAll(){
        return (LinkedList<Session>) sessions;
    }

    /**
     * Remove a sessão com o ID especificado e salva no arquivo.
     */
    public boolean removeById(int id) {
        Iterator<Session> iterator = sessions.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
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