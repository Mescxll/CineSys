package models;

import structures.list.GenericDynamicList;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa um cliente.
 * 
 * @author Maria Eduarda Campos
 * @since 25/05/2025
 * @version 5.0
 */
public class Client {
    private static int _idGenerator = 1;
    private int id;
    private String name;
    private String email;
    private LocalDate birthday;
    private int points;
    private List<Ticket> purchasingHistory;
    
    /**
     * Construtor da classe Client.
     * 
     * @param name Nome do cliente.
     * @param email Email do cliente.
     * @param birthday Data de nascimento do cliente.
     */
    public Client(String name, String email, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.points = 0;
        purchasingHistory = new LinkedList<>();
        id = _idGenerator++;
    }

    /**
     * Obtém o ID do cliente.
     * 
     * @return O ID do cliente.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtém o nome do cliente.
     * @return O nome do cliente.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtém o email do cliente.
     * @return O email do cliente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtém a data de nascimento do cliente.
     * @return A data de nascimento do cliente.
     */
    public String getBirthday() {
        return birthday.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * Obtém os pontos do cliente.
     * @return Os pontos do cliente.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Seta os pontos do cliente.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Subtrai uma quantidade de pontos do cliente.
     * Garante que a pontuação não se torne negativa.
     *
     * @param pointsToRemove A quantidade de pontos a ser removida.
     */
    public void removePoints(int pointsToRemove) {
        if (pointsToRemove > 0) {
            this.points -= pointsToRemove;
            if (this.points < 0) {
                this.points = 0; // Garante que não fique negativo
            }
        }
    }

    /**
     * Obtém o histórico de compras do cliente.
     * @return Uma lista dinâmica contendo os tickets comprados pelo cliente.
     */
    public LinkedList<Ticket> getPurchasingHistory() {
        return (LinkedList<Ticket>) purchasingHistory;
    }

    /**
     * Define o nome do cliente.
     * @param name O novo nome do cliente.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Define o email do cliente.
     * @param email O novo email do cliente.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Define a data de nascimento do cliente.
     * @param birthday A nova data de nascimento do cliente.
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Adiciona um novo ticket ao histórico de compras do cliente.
     *
     * @param ticket O ticket que acabou de ser comprado.
     */
    public boolean addTicketToHistory(Ticket ticket) {
        try {
            purchasingHistory.add(ticket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Incrementa os pontos de fidelidade do cliente.
     *
     * @param additionalPoints quantidade de pontos a somar
     */
    public void addPoints(int additionalPoints) {
        this.points += additionalPoints;
    }

    /**
     * Retorna uma representação em string do histórico do cliente.
     * @return Uma string contendo o histórico de compras do cliente.
     */
    public String printHistory(){
        String result = "";
        for (int i = 0; i < purchasingHistory.size(); i++) {
            result += purchasingHistory.get(i).toString() + "\n";
        }
        return result;
    }

    /**
     * Retorna uma representação em string do cliente. sobrescreve o método toString da classe Object.
     * @return Uma string contendo as informações do cliente.
     */
    @Override
    public String toString() {
        return "Client " + id + ":" + 
        "\nName= " + name + 
        "\nEmail= "+ email + 
        "\nBirthday= "+ birthday + 
        "\nPurchasing History= " + purchasingHistory;
    }

    /**
     * Reseta o contador de IDs para testar novamente a aplicação.
     */
    public static void resetIdGenerator() {
        _idGenerator = 1;
    }
}
