package controller.business;

import enums.PaymentMethod;
import models.*;
import org.junit.Before;
import org.junit.Test;
import structures.list.GenericDynamicList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Classe de testes unitários para a classe ClientController.
 *
 * @author Kaique Silva Sousa
 * @version 2.0
 * @since 14-06-2025
 */
public class ClientControllerTest {

    /**
     * Prepara o ambiente para cada teste, limpando o repositório
     * e resetando o gerador de IDs para garantir a independência dos testes.
     */
    @Before
    public void setup() {
        ClientController.removeAllClients();
        Client.resetIdGenerator();
    }

    /**
     * Testa se o repositório de clientes está inicialmente vazio.
     */
    @Test
    public void testInitialClientRepositoryIsEmpty() {
        assertEquals(0, ClientController.getAllClients().size());
    }

    /**
     * Testa a adição de um único cliente ao repositório.
     */
    @Test
    public void testAddClient() {
        ClientController.addClient("Name", "email@email.com", "01-01-2000");
        assertEquals(1, ClientController.getAllClients().size());
    }

    /**
     * Testa a adição de múltiplos clientes em sequência.
     */
    @Test
    public void testAddMultipleClients() {
        for (int i = 0; i < 5; i++) {
            ClientController.addClient("Name" + i, "email" + i + "@mail.com", "01-01-2000");
        }
        assertEquals(5, ClientController.getAllClients().size());
    }

    /**
     * Testa a busca de um cliente específico pelo seu ID.
     */
    @Test
    public void testGetClientById() {
        ClientController.addClient("João", "joao@email.com", "01-01-1990");
        Client client = ClientController.getAllClients().get(0);
        assertEquals("João", ClientController.getClientById(client.getId()).getName());
    }

    /**
     * Testa a adição de um ticket ao histórico de compras de um cliente.
     */
    @Test
    public void testAddTicketToClient() {
        ClientController.addClient("Teste", "teste@email.com", "01-01-2000");
        Client client = ClientController.getAllClients().get(0);
        Movie movie = new Movie("Filme", "Ação", 120, "Diretor", "Sinopse");
        Session session = new Session(LocalDate.now(), LocalTime.now(), RoomController.getRoomById(1), movie, 30.0);
        Ticket ticket = new Ticket(client, session, session.getTicketValue(), PaymentMethod.CREDIT_CARD);

        ClientController.addTicketToClient(client.getId(), ticket);

        LinkedList<Ticket> history = ClientController.getClientHistory(client.getId());
        assertEquals(1, history.size());
        assertEquals(ticket, history.get(0));
    }

    /**
     * Testa se a tentativa de adicionar um cliente com nome vazio lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddClientWithEmptyName() {
        ClientController.addClient("", "email@test.com", "01-01-2000");
    }

    /**
     * Testa se a tentativa de adicionar um cliente com email vazio lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddClientWithEmptyEmail() {
        ClientController.addClient("Nome", "", "01-01-2000");
    }

    /**
     * Testa se a busca por um ID inválido (zero ou negativo) lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetClientByInvalidId() {
        ClientController.getClientById(0);
    }

    /**
     * Testa se a busca por um ID que não existe no repositório lança uma exceção.
     */
    @Test(expected = RuntimeException.class)
    public void testGetClientByNonExistentId() {
        ClientController.getClientById(999);
    }

    /**
     * Testa se os dados de um cliente são atualizados corretamente.
     */
    @Test
    public void testUpdateClientSuccessfully() {
        ClientController.addClient("Antigo Nome", "antigo@email.com", "01-01-1990");
        Client client = ClientController.getAllClients().get(0);

        ClientController.updateClient(client.getId(), "Novo Nome", "novo@email.com", "05-05-1995");
        Client updatedClient = ClientController.getClientById(client.getId());

        assertEquals("Novo Nome", updatedClient.getName());
        assertEquals("novo@email.com", updatedClient.getEmail());
        assertEquals("05-05-1995", updatedClient.getBirthday());
    }

    /**
     * Testa se a tentativa de atualizar um cliente que não existe lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNonExistentClient() {
        ClientController.updateClient(999, "Nome", "email@email.com", "01-01-1990");
    }

    /**
     * Testa se a tentativa de atualizar um cliente com um nome vazio lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateClientWithEmptyName() {
        ClientController.addClient("Nome", "email@email.com", "01-01-1990");
        Client client = ClientController.getAllClients().get(0);

        ClientController.updateClient(client.getId(), "", "novo@email.com", "01-01-1990");
    }

    /**
     * Testa se a tentativa de atualizar um cliente com um email vazio lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateClientWithEmptyEmail() {
        ClientController.addClient("Nome", "email@email.com", "01-01-1990");
        Client client = ClientController.getAllClients().get(0);

        ClientController.updateClient(client.getId(), "Nome Atualizado", "", "01-01-1990");
    }

    /**
     * Testa se a tentativa de adicionar um ticket nulo a um cliente lança uma exceção.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNullTicketToClient() {
        ClientController.addClient("Fulano", "fulano@mail.com", "01-01-2000");
        Client client = ClientController.getAllClients().get(0);
        ClientController.addTicketToClient(client.getId(), null);
    }

    /**
     * Testa se o histórico de compras de um novo cliente está inicialmente vazio.
     */
    @Test
    public void testGetEmptyClientHistory() {
        ClientController.addClient("Sem Compras", "email@email.com", "31-12-1999");
        Client client = ClientController.getAllClients().get(0);
        assertEquals(0, ClientController.getClientHistory(client.getId()).size());
    }

    /**
     * Testa a adição de múltiplos tickets a um cliente, simulando um
     * cenário para cálculo de desconto ou verificação de histórico.
     */
    @Test
    public void testCalculateDiscount() {
        ClientController.addClient("Fulano", "fulano@mail.com", "01-01-2000");
        Client client = ClientController.getAllClients().get(0);
        ClientController.addTicketToClient(
            client.getId(), 
            new Ticket(
                client, 
                new Session(LocalDate.now(), LocalTime.now(), RoomController.getRoomById(1), new Movie("Filme", "Ação", 120, "Diretor", "Sinopse"), 30.0), 
                30.0, 
                PaymentMethod.CREDIT_CARD
            )
        );
        ClientController.addTicketToClient(client.getId(), new Ticket(client, new Session(LocalDate.now(), LocalTime.now(), RoomController.getRoomById(2), new Movie("Filme", "Ação", 120, "Diretor", "Sinopse"), 30.0), 30.0, PaymentMethod.CREDIT_CARD));
    }
}