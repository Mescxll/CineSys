package exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(int clientId){
        super("Cliente com ID " + clientId + " não foi encontrado!");
    }
}
