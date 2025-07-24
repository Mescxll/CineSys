package repository;

import models.Client;
import java.io.*; // Import para todas as classes de I/O (Serializable, ObjectInputStream, etc.)
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que gerencia a coleção de clientes com persistência via serialização.
 * Salva e carrega a lista de objetos Client diretamente em um arquivo binário.
 *
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 4.0
 */
public class ClientRepository {
    private List<Client> clients;
    private final String FILE_PATH = "data/clients.ser";

    /**
     * Construtor do repositório.
     * Tenta carregar os clientes do arquivo ao ser instanciado.
     */
    public ClientRepository() {
        loadFromFile();
    }

    /**
     * Carrega a lista de clientes de um arquivo binário.
     * Se o arquivo não existir ou estiver vazio, inicia com uma lista nova.
     */
    private void loadFromFile() {
        new File("data").mkdirs();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            this.clients = (List<Client>) ois.readObject();
            System.out.println("Clientes carregados do arquivo serializado: " + FILE_PATH);
        } catch (FileNotFoundException | EOFException e) {
            this.clients = new LinkedList<>();
            System.out.println("Arquivo de clientes não encontrado ou vazio. Iniciando com repositório novo.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro crítico ao carregar clientes do arquivo. Iniciando com repositório vazio.");
            e.printStackTrace();
            this.clients = new LinkedList<>();
        }
    }

    /**
     * Salva a lista de clientes em memória em um arquivo binário.
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.clients);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes no arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adiciona um cliente ao repositório e salva a lista atualizada.
     * @param client O cliente a ser adicionado.
     */
    public void add(Client client) {
        clients.add(client);
        saveToFile();
    }

    /**
     * Busca um cliente pelo ID de forma eficiente.
     * @param id ID do cliente.
     * @return O cliente correspondente, ou null se não encontrado.
     */
    public Client getById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    /**
     * Remove todos os clientes do repositório.
     */
    public void clear() {
        clients.clear();
        saveToFile();
    }

    /**
     * Retorna todos os clientes cadastrados.
     *
     * @return Uma LinkedList de clientes.
     */
    public LinkedList<Client> getAll(){
        return (LinkedList<Client>) clients;
    }

    /**
     * Remove um cliente pelo ID informado.
     *
     * @param id ID do cliente a ser removido.
     * @return true se a remoção for bem-sucedida, false caso não exista cliente com esse ID.
     */
    public boolean removeById(int id) {
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                saveToFile();
                return true;
            }
        }
        return false;
    }
}