package repository;

import models.Room;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Classe que gerencia as salas (Rooms) do cinema.
 * Usamos uma GenericDynamicList como "banco de dados" de salas.
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 1.0
 */
public class RoomRepository {
    private Queue<Room> rooms = new ArrayDeque<>();

    /**
     * Adiciona uma nova sala a lista.
     *
     * @param room A sala a ser adicionada.
     */
    public void add(Room room){
        try {
            rooms.add(room);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca uma sala pelo seu ID.
     *
     * @param id Identificador da sala.
     * @return A sala com o ID fornecido, ou null se não existir.
     */
    public Room getById(int id) {
        Queue<Room> auxQueue = new ArrayDeque<>();
        Room found = null;

        while (!rooms.isEmpty()) {
            Room current = rooms.poll(); // remove da fila original
            if (current.getId() == id) {
                found = current; // achou o ID
            }
            auxQueue.add(current); // mantém o elemento na auxiliar
        }

        // devolve os elementos para a fila original
        rooms = auxQueue;

        return found;
    }
    
    /**
     * Retorna todas as salas cadastradas.
     *
     * @return Um ArrayDeque contendo todas as salas.
     */
    public ArrayDeque<Room> getAll(){
        return (ArrayDeque<Room>) rooms;
    }

    /**
     * Remove a sala com o ID especificado.
     *
     * @param id Identificador da sala a ser removida.
     * @return true se a sala foi removida; false se não encontrou nenhuma com aquele ID.
     */
    public boolean removeById(int id) {
        Queue<Room> auxQueue = new ArrayDeque<>();
        boolean removed = false;

        while (!rooms.isEmpty()) {
            Room current = rooms.poll(); // remove da fila original
            if (current.getId() == id && !removed) {
                removed = true; // achou e não adiciona na fila auxiliar (remove)
                continue;
            }
            auxQueue.add(current); // mantém o elemento que não foi removido
        }

        // devolve os elementos para a fila original
        rooms = auxQueue;

        return removed;
    }

}