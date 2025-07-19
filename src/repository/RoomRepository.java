package repository;

import models.Room;

import java.io.*; // Import genérico para todas as classes de I/O
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que gerencia a coleção de salas (Rooms) do cinema.
 * Utiliza serialização para persistir os dados em arquivo.
 *
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 2.0
 */
public class RoomRepository {
    private List<Room> rooms;
    private final String FILE_PATH = "data/rooms.ser"; // Arquivo binário para objetos serializados

    /**
     * Construtor do repositório.
     * Tenta carregar as salas do arquivo ao ser instanciado.
     */
    public RoomRepository() {
        loadFromFile();
    }

    /**
     * Carrega a lista de salas de um arquivo binário.
     * Se o arquivo não existir ou estiver vazio, inicia com uma lista nova.
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        new File("data").mkdirs(); // Garante que a pasta 'data' exista
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            this.rooms = (List<Room>) ois.readObject();
            System.out.println("Salas carregadas do arquivo serializado: " + FILE_PATH);
        } catch (FileNotFoundException | EOFException e) {
            this.rooms = new LinkedList<>();
            System.out.println("Arquivo de salas não encontrado ou vazio. Iniciando com repositório novo.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro crítico ao carregar salas do arquivo. Iniciando com repositório vazio.");
            e.printStackTrace();
            this.rooms = new LinkedList<>();
        }
    }

    /**
     * Salva a lista de salas em memória em um arquivo binário.
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.rooms);
        } catch (IOException e) {
            System.err.println("Erro ao salvar salas no arquivo: " + e.getMessage());
        }
    }

    /**
     * Adiciona uma nova sala à lista e salva no arquivo.
     *
     * @param room A sala a ser adicionada.
     */
    public void add(Room room) {
        rooms.add(room);
        saveToFile();
    }

    /**
     * Busca uma sala pelo seu ID de forma eficiente.
     *
     * @param id Identificador da sala.
     * @return A sala com o ID fornecido, ou null se não existir.
     */
    public Room getById(int id) {
        for (Room room : rooms) {
            if (room.getId() == id) {
                return room;
            }
        }
        return null;
    }

    /**
     * Retorna todas as salas cadastradas.
     *
     * @return Um ArrayDeque contendo todas as salas.
     */
    public LinkedList<Room> getAll(){
        return (LinkedList<Room>) rooms;
    }

    /**
     * Remove a sala com o ID especificado de forma eficiente.
     *
     * @param id Identificador da sala a ser removida.
     * @return true se a sala foi removida; false caso contrário.
     */
    public boolean removeById(int id) {
        // Usar um Iterator é a forma mais segura e eficiente de remover
        // de uma LinkedList durante a iteração.
        Iterator<Room> iterator = rooms.iterator();
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
     * Remove todas as salas do repositório.
     */
    public void clear() {
        rooms.clear();
        saveToFile();
    }
}