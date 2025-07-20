package repository;

import models.Movie;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que gerencia a coleção de filmes, com persistência em arquivo.
 * Pode operar em modo de memória ou com arquivo de texto.
 *
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 3.0
 */
public class MovieRepository {
    private List<Movie> movies;
    private final String FILE_PATH = "data/movies.ser";

    /**
     * Construtor do repositório.
     * Tenta carregar os filmes do arquivo ao ser instanciado.
     */
    public MovieRepository() {
        loadFromFile();
    }

    /**
     * Carrega a lista de filmes de um arquivo binário.
     * Se o arquivo não existir ou estiver vazio, inicia com uma lista nova.
     */
    private void loadFromFile() {
        new File("data").mkdirs();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            this.movies = (List<Movie>) ois.readObject();
            System.out.println("Filmes carregados do arquivo serializado: " + FILE_PATH);
        } catch (FileNotFoundException | EOFException e) {
            this.movies = new LinkedList<>();
            System.out.println("Arquivo de filmes não encontrado ou vazio. Iniciando com repositório novo.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro crítico ao carregar filmes do arquivo. Iniciando com repositório vazio.");
            e.printStackTrace();
            this.movies = new LinkedList<>();
        }
    }

    /**
     * Salva a lista de filmes em memória em um arquivo binário.
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.movies);
        } catch (IOException e) {
            System.err.println("Erro ao salvar filmes no arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adiciona um filme à lista e salva no arquivo.
     *
     * @param movie O filme a ser adicionado.
     */
    public void add(Movie movie) {
        movies.add(movie);
        saveToFile();
    }

    /**
     * Retorna um filme a partir do ID de forma eficiente.
     *
     * @param id O identificador único do filme.
     * @return O filme com o ID fornecido ou null se não for encontrado.
     */
    public Movie getById(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Atualiza um filme selecionado e salva no arquivo.
     *
     * @param id O ID do filme a ser atualizado.
     * @param updatedMovie O objeto filme com as novas informações.
     */
    public void update(int id, Movie updatedMovie) {
        int index = getIndex(id);
        if (index == -1) {
            throw new IllegalArgumentException("Filme com ID " + id + " não existe!");
        }
        movies.set(index, updatedMovie);
        saveToFile();
    }

    /**
     * Método auxiliar para pegar o índice de um certo filme.
     */
    private int getIndex(int id) {
        int index = 0;
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Retorna todos os filmes armazenados.
     * 
     * @return Uma lista contendo todos os filmes.
     */
    public LinkedList<Movie> getAll(){
        return (LinkedList<Movie>) movies;
    }

    /**
     * Remove um filme da lista com base no ID e salva no arquivo.
     *
     * @param id O ID do filme a ser removido.
     * @return true se o filme foi encontrado e removido; false caso contrário.
     */
    public boolean removeById(int id) {
        Iterator<Movie> iterator = movies.iterator();
        while (iterator.hasNext()) {
            Movie movie = iterator.next();
            if (movie.getId() == id) {
                iterator.remove();
                saveToFile();
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna um filme com o mesmo nome fornecido.
     *
     * @param name nome do filme a ser buscado.
     * @return O filme encontrado ou null.
     */
    public Movie getMovieByName(String name) {
        for (Movie movie : movies) {
            if (movie.getTitle().trim().equalsIgnoreCase(name.trim())) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Remove todos os filmes da lista e salva o estado vazio no arquivo.
     */
    public void clear() {
        movies.clear();
        saveToFile();
    }
}