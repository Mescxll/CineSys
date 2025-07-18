package repository;

import models.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe que gerencia a coleção de clientes.
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 3.0
 */
public class ClientRepository {
    private final List<Client> clients;
    private final String FILE_PATH = "data/clients.txt";
    private final boolean useFilePersistence; // "Interruptor" para ligar/desligar a persistência

    /**
     * Construtor padrão. Opera em modo de memória, ideal para testes.
     */
    public ClientRepository() {
        this(false);
    }

    /**
     * Construtor principal que define o modo de operação.
     *
     * @param useFilePersistence Se true, o repositório lerá e salvará em arquivo.
     * Se false, operará apenas em memória.
     */
    public ClientRepository(boolean useFilePersistence) {
        this.clients = new LinkedList<>();
        this.useFilePersistence = useFilePersistence;

        if (this.useFilePersistence) {
            ensureDataFileExists();
            loadFromFile();
        }
    }

    /**
     * Garante que o diretório 'data' e o arquivo de clientes existam.
     * Se não existirem, eles são criados.
     */
    private void ensureDataFileExists() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            File clientsFile = new File(FILE_PATH);
            if (!clientsFile.exists()) {
                clientsFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Erro crítico ao criar diretório ou arquivo de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os clientes do arquivo de texto para a lista em memória.
     */
    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File(FILE_PATH))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length >= 5) { // id;nome;email;cpf;data
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    String cpf = parts[3];
                    LocalDate birthday = LocalDate.parse(parts[4], formatter);

                    // Usa o NOVO construtor que aceita o ID
                    Client client = new Client(id, name, email, cpf, birthday);
                    this.clients.add(client);
                }
            }
            System.out.println("Clientes carregados do arquivo: " + FILE_PATH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ou analisar o arquivo de clientes: " + e.getMessage());
        }
    }

    /**
     * Adiciona um cliente ao repositório.
     *
     * @param client O cliente a ser adicionado.
     */
    public void add(Client client){
        try {
            clients.add(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id ID do cliente.
     * @return O cliente correspondente, ou null se não encontrado.
     */
    public Client getById(int id){
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }
        return null;
    }

    /**
     * Atualiza um cliente selecionado
     *
     * @param id do cliente a ser atualizada
     * @param client novo cliente que será atualizado
     */
    public void update(int id, Client client){
        if(getById(id) == null)
            throw new IllegalArgumentException("Cliente não existe!");
        clients.set(getIndex(id), client);
    }

    /**
     * Método auxiliar para pegar o index de uma certa sessão
     *
     * @param id do cliente
     * @return se o id existir, retorna o index requerido
     *         caso não existe, retorna -1
     */
    private int getIndex(int id){
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).getId() == id){
                return i;
            }
        }
        return -1;
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
    public boolean removeById(int id){
        for(int i = 0; i < clients.size(); i++){
            if (clients.get(i).getId() == id) {
                try {
                    clients.remove(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Remove todos os clientes do repositório. Limpando a lista de clientes.
     */
    public void clear(){
        clients.clear();
    }
}