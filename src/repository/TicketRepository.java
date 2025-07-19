package repository;

import controller.business.ClientController;
import controller.business.SessionController;
import enums.PaymentMethod;
import models.Client;
import models.Session;
import models.Ticket;

import java.io.*;
import java.util.*;

/**
 * Repositório para gerenciar os dados dos tickets.
 * Pode operar em modo de memória ou com persistência em arquivo de texto.
 *
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 3.0
 */
public class TicketRepository {
    private List<Ticket> tickets;
    private final String FILE_PATH = "data/tickets.ser"; // Arquivo binário

    /**
     * Construtor do repositório.
     * Tenta carregar os tickets do arquivo ao ser instanciado.
     */
    public TicketRepository() {
        loadFromFile();
    }

    /**
     * Carrega a lista de tickets de um arquivo binário.
     * Se o arquivo não existir ou estiver vazio, inicia com uma lista nova.
     * Também repopula o histórico de compras dos clientes.
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        new File("data").mkdirs();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            this.tickets = (List<Ticket>) ois.readObject();
            System.out.println("Tickets carregados do arquivo serializado: " + FILE_PATH);
            for (Ticket ticket : this.tickets) {
                Client client = ticket.getClient();
                if (client != null) {
                    Client clientFromController = ClientController.getClientById(client.getId());
                    if (clientFromController != null) {
                        clientFromController.addTicketToHistory(ticket);
                    }
                }
            }
        } catch (FileNotFoundException | EOFException e) {
            this.tickets = new LinkedList<>();
            System.out.println("Arquivo de tickets não encontrado ou vazio. Iniciando com repositório novo.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro crítico ao carregar tickets do arquivo. Iniciando com repositório vazio.");
            e.printStackTrace();
            this.tickets = new LinkedList<>();
        }
    }

    /**
     * Salva a lista de tickets em memória em um arquivo binário.
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.tickets);
        } catch (IOException e) {
            System.err.println("Erro ao salvar tickets no arquivo: " + e.getMessage());
        }
    }

    /**
     * Adiciona um ticket ao repositório.
     *
     * @param ticket O ticket a ser adicionado.
     */
    public void add(Ticket ticket) {
        tickets.add(ticket);
        saveToFile();
    }

    /**
     * Busca um ticket pelo ID de forma eficiente.
     *
     * @param id O ID do ticket a ser buscado.
     * @return O ticket correspondente ao ID ou null se não encontrado.
     */
    public Ticket getById(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Atualiza um ticket selecionado.
     * @param id do ticket a ser atualizado.
     * @param updatedTicket novo ticket que será atualizado.
     */
    public void update(int id, Ticket updatedTicket) {
        int index = getIndex(id);
        if (index == -1) {
            throw new IllegalArgumentException("Ticket com ID " + id + " não existe!");
        }
        tickets.set(index, updatedTicket);
        saveToFile();
    }

    /**
     * Método auxiliar para pegar o índice de um certo ticket.
     */
    private int getIndex(int id) {
        int index = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Retorna todos os tickets armazenados no repositório.
     *
     * @return Uma lista dinâmica contendo todos os tickets.
     */
    public LinkedList<Ticket> getAll(){
        return (LinkedList<Ticket>) tickets;
    }

    /**
     * Remove um ticket pelo ID de forma eficiente.
     *
     * @param id O ID do ticket a ser removido.
     * @return true se o ticket foi removido com sucesso, false caso contrário.
     */
    public boolean removeById(int id) {
        Iterator<Ticket> iterator = tickets.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                saveToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Remove todos os tickets do repositório.
     */
    public void clear() {
        tickets.clear();
        saveToFile();
    }
}