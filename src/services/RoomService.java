package services;

import models.Room;
import models.Session;
import repository.RoomRepository;

import java.util.ArrayDeque;

/**
 * Classe de serviço responsável pela lógica de negócios
 * relacionada às salas (Room) do cinema.
 * 
 * @author Thiago Ferreira Ribeiro
 * @author Kaique Silva Sousa
 */
public class RoomService {
    private final RoomRepository roomRepository;

    /**
     * Construtor do RoomService.
     *
     * @param roomRepository O repositório de salas.
     */
    public RoomService (RoomRepository roomRepository){
        this.roomRepository = roomRepository;
        addRoom(200);
        addRoom(150);
        addRoom(170);
        addRoom(100);
        addRoom(120);
    }

    /**
     * Cria e adiciona uma nova sala ao sistema.
     *
     * @param totalSeat Quantidade total de assentos da sala.
     * @return true se a sala foi adicionada com sucesso.
     */
    private boolean addRoom(int totalSeat){
        if (totalSeat <= 0) {
            System.out.println("O número total de assentos deve ser maior que zero!");
            return false;
        }

        Room newRoom = new Room(totalSeat);
        roomRepository.add(newRoom);
        return true;
    }

    /**
     * Retorna todas as salas cadastradas.
     *
     * @return Lista de todas as salas.
     */
    public ArrayDeque<Room> getAllRooms(){
        return roomRepository.getAll();
    }

    /**
     * Busca uma sala pelo ID.
     *
     * @param id O ID da sala.
     * @return A sala encontrada.
     * @throws IllegalArgumentException se o ID for menor ou igual a zero.
     * @throws RuntimeException         se não existir uma sala com esse ID.
     */
    public Room getRoomById(int id){
        if(id <= 0){
            throw new IllegalArgumentException("O ID deve ser maior que zero!");
        }

        Room room = roomRepository.getById(id);
        if(room == null){
            throw new RuntimeException("Nenhuma sala encontrada com o ID " + id);
        }

        return room;
    }

    /**
     * Enfileira (adiciona) uma sessão na sala indicada.
     *
     * @param roomId  ID da sala onde deseja agendar a sessão.
     * @param session A sessão a ser agendada.
     * @return true se a sessão foi agendada com sucesso; false se a sala não existir ou erro.
     * @throws IllegalArgumentException se o ID da sala for inválido.
     */
    public boolean addSessionToRoom(int roomId, Session session){
        if (roomId <= 0) {
            throw new IllegalArgumentException("O ID da sala deve ser maior que zero!");
        }

        Room room = roomRepository.getById(roomId);
        if (room == null) {
            System.out.println("Sala com o ID " + roomId + " não encontrada! Não foi possível agendar a sessão.");
            return false;
        }

        try {
            room.addSession(session);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove a próxima sessão da sala indicada.
     * @param roomId
     * @throws IllegalArgumentException se o ID da sala for inválido.
     * @return A sessão removida.
     */
    public Session removeNextSessionFromRoom(int roomId){
        if (roomId <= 0) {
            throw new IllegalArgumentException("O ID da sala deve ser maior que zero!");
        }

        Room room = roomRepository.getById(roomId);
        if (room == null) {
            System.out.println("Sala com o ID " + roomId + " não encontrada! Não foi possível remover a sessão.");
            return null;
        }

        return room.removeSession();
    }

}