package repository;

import models.Client;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe que gerencia a coleção de clientes.
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 2.0
 */
public class ClientRepository {
    private List<Client> clients = new LinkedList<>();

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
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).getId() == id){
                return clients.get(i);
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