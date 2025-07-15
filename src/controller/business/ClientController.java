package controller.business;

import models.Client;
import models.Ticket;
import repository.ClientRepository;
import services.ClientService;

import java.util.LinkedList;

/**
 * Classe de controle para a lógica de negócio dos clientes.
 * @author Kaique Silva Sousa 
 * @since 11/06/2025
 * @version 1.0
 */
public class ClientController {
    
    private static final ClientService clientService = new ClientService(new ClientRepository());

    /**
     * Adiciona um novo cliente ao sistema chamando o método addClient da classe
     * ClientService.
     * 
     * @param name     Nome do cliente.
     * @param cpf      CPF do cliente.
     * @param email    Email do cliente.
     * @param birthday Data de nascimento do cliente.
     * @return Mensagem de sucesso ou erro.
     */
    public static String addClient(String name, String cpf, String email, String birthday) {
        return clientService.addClient(name, cpf, email, birthday);
    }

    /**
     * Atualiza um cliente existente chamando o método updateClient da classe
     * ClientService.
     * 
     * @param id       ID do cliente a ser atualizado.
     * @param name     Nome do cliente.
     * @param cpf      CPF do cliente.
     * @param email    Email do cliente.
     * @param birthday Data de nascimento do cliente.
     * @return Mensagem de sucesso ou erro.
     */
    public static String updateClient(int id, String name, String cpf, String email, String birthday) {
        return clientService.updateClient(id, name, cpf, email, birthday);
    }

    /**
     * Remove um cliente existente chamando o método removeClient da classe ClientService.
     * @param id ID do cliente a ser removido.
     * @return Cliente removido.
    */
    public static Client removeClient(int id) {
        return clientService.removeClient(id);
    }

    /**
     * Seleciona um cliente pelo ID chamando o método getClientById da classe ClientService.
     * @param id ID do cliente.
     * @return Cliente selecionado.
    */
    public static Client getClientById(int id) {
        return clientService.getClientById(id);
    }

    /**
     * Adiciona um ticket ao histórico de compras de um cliente chamando o método addTicketToClient da classe ClientService.
     * @param clientId ID do cliente.
     * @param ticket Ticket a ser adicionado.
     * @return Mensagem de sucesso ou erro.
    */
    public static void addTicketToClient(int clientId, Ticket ticket) {
        clientService.addTicketToClient(clientId, ticket);
    }

    /**
     * Retorna uma lista com todos os clientes cadastrados chamando o método getAllClients da classe ClientService.
     * @return lista com todos os clientes cadastrados.
     */
    public static LinkedList<Client> getAllClients(){
        return clientService.getAllClients();
    }
 
    /**
     * Pega o histórico de compras de um cliente chamando o método getClientHistory da classe ClientService.
     * @param clientId ID do cliente.
     * @return Histórico de compras do cliente.
     */
    public static LinkedList<Ticket> getClientHistory(int clientId) {
        return clientService.getClientHistory(clientId);
    }

    /**
     * Calcula o desconto de um cliente chamando o método calculateDiscount da classe ClientService.
     * 
     * @param clientId ID do cliente.
     * @return Desconto do cliente.
     */
    public static double calculateDiscount(int clientId) {
        return clientService.calculateDiscount(clientId);
    }

    /**
     * Registra os pontos de fidelidade de um cliente chamando o método registerPoints da classe ClientService.
     * 
     * @param clientId ID do cliente.
     * @param ticket Ticket que será usado para registrar os pontos.
     */
    public static void registerPoints(int clientId, Ticket ticket) {
        clientService.registerPoints(clientId, ticket);      
    }

    /**
     * Remove todos os clientes cadastrados chamando o método removeAllClients da classe ClientService.
     */
    public static void removeAllClients() {
        clientService.removeAllClients();
    }

    /**
     * Remove um ticket específico do histórico de compras de um cliente.
     * Delega a chamada para o método correspondente em ClientService.
     * * @param clientId O ID do cliente.
     * @param ticket O objeto Ticket a ser removido do histórico.
     */
    public static void removeTicketFromHistory(int clientId, Ticket ticket) {
        clientService.removeTicketFromHistory(clientId, ticket);
    }

    /**
     * Remove uma quantidade de pontos de fidelidade de um cliente.
     * Delega a chamada para o método correspondente em ClientService.
     * * @param clientId O ID do cliente.
     * @param pointsToRemove A quantidade de pontos a ser removida.
     */
    public static void removePoints(int clientId, int pointsToRemove) {
        clientService.removePoints(clientId, pointsToRemove);
    }
}
