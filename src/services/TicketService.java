package services;

import controller.business.ClientController;
import controller.business.SessionController;
import enums.PaymentMethod;
import models.Client;
import models.Session;
import models.Ticket;
import repository.TicketRepository;
import exceptions.*;

import java.util.LinkedList;

/**
 *
 * Serviço responsável por gerenciar operações relacionadas aos tickets.
 * Utiliza o {@link TicketRepository} como fonte de dados.
 *
 * @author Helen Santos Rocha
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 1.1
 */
public class TicketService {
    private final TicketRepository ticketRepository;

    /**
     * Construtor para inicializar o serviço de tickets.
     *
     * @param ticketRepository O repositório de tickets.
     */
    public TicketService(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

     /**
     * Recupera todos os tickets armazenados.
     *
     * @return Uma lista dinâmica contendo todos os tickets.
     */
    public LinkedList<Ticket> getAllTickets(){
        return ticketRepository.getAll();
    }

    /**
     * Busca um ticket pelo ID.
     *
     * @param id O ID do ticket a ser buscado.
     * @return O ticket correspondente ao ID.
     * @throws IllegalArgumentException Se o ID for menor ou igual a zero.
     * @throws RuntimeException         Se nenhum ticket for encontrado com o ID fornecido.
     */
    public Ticket getTicketById(int id){
        if(id <= 0){
            throw new IllegalArgumentException("O ID deve ser maior que zero!");
        }

        Ticket ticket = ticketRepository.getById(id);
        if(ticket == null){
            throw new RuntimeException("Nenhum ticket encontrado com o ID " + id);
        }

        return ticket;
    }

    /**
     * Remove um ticket pelo ID.
     *
     * @param id O ID do ticket a ser removido.
     */
    public void removeTicketById(int id){
        ticketRepository.removeById(id);
    }

    /**
     * Orquestra o processo completo de compra de um ingresso.
     * <p>
     * Este método valida o cliente e a sessão, calcula o preço com desconto,
     * cria o ticket, atualiza o histórico e pontos do cliente, e decrementa
     * os assentos disponíveis na sessão, persistindo todas as alterações.
     *
     * @param clientId O ID do cliente que está comprando.
     * @param sessionId O ID da sessão desejada.
     * @param paymentMethod O método de pagamento em formato de String.
     * @return O objeto Ticket que foi criado e salvo.
     * @throws ClientNotFoundException se o cliente não for encontrado.
     * @throws IllegalArgumentException se a sessão não for encontrada.
     * @throws CrowdedRoomException se não houver assentos disponíveis.
     * @throws PaymentInvalidException se o método de pagamento for inválido.
     */
    public Ticket purchaseTicket(int clientId, int sessionId, String paymentMethod) {

        // Buscar cliente
        Client client = ClientController.getClientById(clientId);
        if (client == null) {
            throw new ClientNotFoundException(clientId);
        }

        // Buscar sessão
        Session session = SessionController.getSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Sessão com ID " + sessionId + " não encontrada.");
        }
        if (session.getTotalAvailableSeats() <= 0) {
            throw new CrowdedRoomException(sessionId);
        }

        // Validar método de pagamento
        PaymentMethod method;
        try {
            method = PaymentMethod.fromDescription(paymentMethod);
        } catch (IllegalArgumentException e) {
            throw new PaymentInvalidException(paymentMethod);
        }

        // Calcular desconto
        double discount = ClientController.calculateDiscount(clientId);
        double basePrice = session.getTicketValue();
        double finalPrice = basePrice * (1 - discount / 100.0);

        Ticket ticket = new Ticket(client, session, finalPrice, method);

        ticketRepository.add(ticket);

        // Atualiza o cliente (adiciona o ticket ao histórico e registra os pontos)
        ClientController.registerPoints(clientId, ticket);

        // Atualizando assentos disponíveis da sessão
        session.setTotalAvailableSeats(session.getTotalAvailableSeats()-1);

        SessionController.updateSession(session);

        return ticket;
    }

    /**
     * Remove todos os tickets.
     */
    public void removeAllTickets(){
        ticketRepository.clear();
    }
}