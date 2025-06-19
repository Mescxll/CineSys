package controller.business;

import models.Room;
import models.Session;
import repository.RoomRepository;
import services.RoomService;

import java.util.List;

/**
 * Classe de controle para a lógica de negócio das salas que já se inicia com 5 salas cadastradas.
 * @author Kaique Silva Sousa 
 * @since 11/06/2025
 * @version 1.0
 */
public class RoomController {

    private static final RoomService roomService = new RoomService(new RoomRepository());
    
    /**
     * Pega todas as salas cadastradas.
     */
    public static ArrayDeque<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    /**
     * Pega uma sala existeente pelo seu ID.
     * @param id ID da sala.
     */
    public static Room getRoomById(int id) {
        return roomService.getRoomById(id);
    }

    /**
     * Adiciona uma sessão a uma sala existente.
     * @param roomId ID da sala.
     * @param session Sessão a ser adicionada.
     * @return true se a sessão foi adicionada com sucesso.
     */
    public static boolean addSessionToRoom(int roomId, Session session) {
        return roomService.addSessionToRoom(roomId, session);
    }
    
    /**
     * Remove a próxima sessão da sala.
     * @param roomId ID da sala.
     * @return Sessão removida.
     */
    public static Session removeNextSessionFromRoom(int roomId) {
        return roomService.removeNextSessionFromRoom(roomId);
    }
}
