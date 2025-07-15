package exceptions;

public class CrowdedRoomException extends RuntimeException{
    public CrowdedRoomException(int sessionId){
        super("Sessão de ID " + sessionId + " está lotada!");
    }
}
