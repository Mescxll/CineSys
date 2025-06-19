package controller.business;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import enums.PaymentMethod;
import models.Client;
import models.Session;
import models.Ticket;

/**
 * Classe responsável por controlar operações relacionadas às vendas de ingressos.
 * 
 * @author Helen Santos Rocha 
 * @since 11/06/2025
 * @version 1.0
 */
public class SaleController {

    /**
     * Processa a venda de múltiplos ingressos para um cliente, usando as regras do TicketService.
     *
     * @param client O cliente que está comprando
     * @param session A sessão desejada
     * @param quantity Quantidade de ingressos
     * @param paymentMethod Método de pagamento escolhido
     * @return Lista com todos os tickets gerados
     * @throws Exception se não houver assentos ou ocorrer erro na geração de algum ticket
     */
    public static ArrayList<Ticket> processSale(Client client, Session session, int quantity, PaymentMethod paymentMethod) throws Exception {

        if (session.getTotalAvailableSeats() < quantity) {
            throw new IllegalArgumentException("Venda excedida: não há assentos suficientes.");
        }

        ArrayList<Ticket> tickets = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Ticket ticket = TicketController.purchaseTicket(
                    client.getId(),
                    session.getId(),
                    paymentMethod.name() // usa o nome para depois converter
            );
            tickets.add(ticket);
        }

        return tickets;
    }

    /**
     * Cancela a venda de um ingresso específico.
     * <p>
     * Este método realiza as operações inversas da compra: remove o ticket do
     * histórico do cliente, devolve o assento para a sessão, ajusta os pontos
     * de fidelidade e remove o ticket do sistema.
     *
     * @param ticketId O ID do ticket a ser cancelado.
     * @throws IllegalArgumentException se o ticket com o ID fornecido não for encontrado.
     */
    public static void cancelSale(int ticketId) {
        System.out.println("Iniciando cancelamento do ticket ID: " + ticketId);

        // 1. Buscar o ticket que será cancelado.
        //    Isso também valida se o ticket existe. Se não existir, getTicketById deve lançar uma exceção.
        Ticket ticket = TicketController.getTicketById(ticketId);

        // 2. Obter o cliente e a sessão associados ao ticket.
        Client client = ticket.getClient();
        Session session = ticket.getSession();

        if (client == null || session == null) {
            throw new IllegalStateException("O ticket com ID " + ticketId + " possui dados de cliente ou sessão inválidos.");
        }

        // 3. Devolver o assento para a sessão (operação inversa da venda).
        session.setTotalAvailableSeats(session.getTotalAvailableSeats() + 1);
        System.out.println("Assento devolvido para a sessão " + session.getId() + ". Assentos disponíveis: " + session.getTotalAvailableSeats());

        // 4. Remover o ticket do histórico de compras do cliente.
        //    (Isto assume que você tem ou criará um método no ClientController para fazer isso).
        ClientController.removeTicketFromHistory(client.getId(), ticket);
        System.out.println("Ticket removido do histórico do cliente " + client.getName());

        // 5. Remover os pontos de fidelidade que foram ganhos com esta compra.
        //    (Assumindo que cada compra gera 5 pontos, como no seu ClientService).
        ClientController.removePoints(client.getId(), 5);
        System.out.println("Pontos removidos do cliente " + client.getName());

        // 6. Finalmente, remover o ticket do repositório principal de tickets.
        TicketController.removeTicketById(ticketId);
        System.out.println("Ticket ID " + ticketId + " removido do sistema.");

        System.out.println("Venda do ticket ID " + ticketId + " cancelada com sucesso.");
    }
}
