package repository;

import models.Room;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe que gerencia as salas (Rooms) do cinema.
 * Usamos uma LinkedList como "banco de dados" de salas.
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 1.0
 */
public class RoomRepository {
    private List<Room> rooms = new LinkedList<>();

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
    public Room getById(int id){
        for(int i = 0; i < rooms.size(); i++){
            if (rooms.get(i).getId() == id) {
                return rooms.get(i);
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
     * Remove a sala com o ID especificado.
     *
     * @param id Identificador da sala a ser removida.
     * @return true se a sala foi removida; false se não encontrou nenhuma com aquele ID.
     */
    public boolean removeById(int id){
        for(int i = 0; i < rooms.size(); i++){
            if (rooms.get(i).getId() == id) {
                try {
                    rooms.remove(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

}