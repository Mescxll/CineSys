package repository;

import controller.business.ClientController;
import controller.business.SessionController;
import enums.PaymentMethod;
import models.Client;
import models.Session;
import models.Ticket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
    private final List<Ticket> tickets;
    private final String FILE_PATH = "data/tickets.txt";
    private final boolean useFilePersistence;

    /**
     * Construtor padrão. Opera em modo de memória, ideal para testes.
     */
    public TicketRepository() {
        this(false);
    }

    /**
     * Construtor principal que define o modo de operação.
     * @param useFilePersistence Se true, o repositório lerá e salvará em arquivo.
     */
    public TicketRepository(boolean useFilePersistence) {
        this.tickets = new LinkedList<>();
        this.useFilePersistence = useFilePersistence;
        if (this.useFilePersistence) {
            ensureDataFileExists();
            loadFromFile();
        }
    }

    /**
     * Garante que o diretório 'data' e o arquivo de tickets existam no disco.
     * Se não existirem, eles são criados para evitar erros de "Arquivo Não Encontrado"
     * na primeira execução da aplicação ou após uma clonagem limpa do repositório.
     */
    private void ensureDataFileExists() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();
            File ticketsFile = new File(FILE_PATH);
            if (!ticketsFile.exists()) ticketsFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Erro crítico ao criar diretório ou arquivo de dados: " + e.getMessage());
        }
    }

    /**
     * Carrega todos os tickets do arquivo de texto para a lista em memória.
     * <p>
     * Este método é chamado pelo construtor quando a persistência em arquivo está
     * ativada. Ele lê cada linha do arquivo, analisa os dados, reconstrói
     * os objetos 'Ticket' com suas dependências (Cliente e Sessão) e os
     * adiciona à lista interna do repositório.
     */
    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File(FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    int ticketId = Integer.parseInt(parts[0]);
                    int clientId = Integer.parseInt(parts[1]);
                    int sessionId = Integer.parseInt(parts[2]);
                    double finalPrice = Double.parseDouble(parts[3]);
                    PaymentMethod paymentMethod = PaymentMethod.valueOf(parts[4]);

                    Client client = ClientController.getClientById(clientId);
                    Session session = SessionController.getSessionById(sessionId);

                    if (client != null && session != null) {
                        Ticket ticket = new Ticket(ticketId, client, session, finalPrice, paymentMethod);
                        this.tickets.add(ticket);
                    } else {
                        System.err.println("Erro ao carregar ticket: Cliente ou Sessão com ID não encontrado. Linha: " + line);
                    }
                }
            }
            System.out.println("Tickets carregados do arquivo: " + FILE_PATH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ou analisar o arquivo de tickets: " + e.getMessage());
        }
    }

    /**
     * Salva a lista de tickets em memória de volta para o arquivo de texto.
     * Este método é chamado sempre que há uma alteração na lista de tickets
     * (adição, remoção, etc.), caso a persistência em arquivo esteja ativada.
     * Ele sobrescreve o conteúdo do arquivo com os dados atuais.
     */
    private void saveToFile() {
        if (!useFilePersistence) return;
        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {
            for (Ticket ticket : this.tickets) {
                // Formato: ticketId;clientId;sessionId;precoFinal;metodoPagamento
                String line = String.format(Locale.US, "%d;%d;%d;%f;%s",
                        ticket.getId(),
                        ticket.getClient().getId(),
                        ticket.getSession().getId(),
                        ticket.getFinalPrice(),
                        ticket.getPaymentMethod().name()); // Salva o nome do enum
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
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