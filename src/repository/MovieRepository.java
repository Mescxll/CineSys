package repository;

import models.Movie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
    private final List<Movie> movies;
    private final String FILE_PATH = "data/movies.txt";
    private final boolean useFilePersistence;

    /**
     * Construtor padrão. Opera em modo de memória, ideal para testes.
     */
    public MovieRepository() {
        this(false);
    }

    /**
     * Construtor principal que define o modo de operação.
     *
     * @param useFilePersistence Se true, o repositório lerá e salvará em arquivo.
     */
    public MovieRepository(boolean useFilePersistence) {
        this.movies = new LinkedList<>();
        this.useFilePersistence = useFilePersistence;

        if (this.useFilePersistence) {
            ensureDataFileExists();
            loadFromFile();
        }
    }

    private void ensureDataFileExists() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();
            File moviesFile = new File(FILE_PATH);
            if (!moviesFile.exists()) moviesFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Erro crítico ao criar diretório ou arquivo de dados: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(new File(FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";", -1); // -1 para incluir campos vazios no final
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String genre = parts[2];
                    int duration = Integer.parseInt(parts[3]);
                    String classification = parts[4];
                    String synopsis = parts[5];

                    // Usa o construtor que aceita o ID para recriar o objeto
                    Movie movie = new Movie(id, title, genre, duration, classification, synopsis);
                    this.movies.add(movie);
                }
            }
            System.out.println("Filmes carregados do arquivo: " + FILE_PATH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ou analisar o arquivo de filmes: " + e.getMessage());
        }
    }

    private void saveToFile() {
        if (!useFilePersistence) return;

        try (PrintWriter writer = new PrintWriter(new File(FILE_PATH))) {
            for (Movie movie : this.movies) {
                // Formato: id;titulo;genero;duracao;classificacao;sinopse
                String line = String.format("%d;%s;%s;%d;%s;%s",
                        movie.getId(),
                        movie.getTitle(),
                        movie.getGenre(),
                        movie.getDuration(),
                        movie.getClassification(),
                        movie.getSynopsis());
                writer.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao salvar filmes no arquivo: " + e.getMessage());
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