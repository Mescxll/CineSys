package repository;

import models.Movie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * Classe que manipula diretamente a lista de filmes.
 * Pode operar em modo de memória ou com arquivo de texto.
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

        try (PrintWriter writer = new PrintWriter(FILE_PATH)) {
            for (Movie movie : this.movies) {
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
     * Adiciona um filme à lista de filmes
     * 
     * @param movie O filme que será adicionado à lista.
     */
    public void add(Movie movie){
        try {
            movies.add(movie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna um filme a partir do ID
     * 
     * @param id O identificador único do filme.
     * @return O filme com o ID fornecido ou {@code null} se não for encontrado
     */
    public Movie getById(int id){
        for(int i = 0; i < movies.size(); i++){
            if (movies.get(i).getId() == id) {
                return movies.get(i);
            }
        }
        return null;
    }

    /**
     * Atualiza um filme selecionado
     *
     * @param id do filme a ser atualizado
     * @param movie nova sessão que será atualizada
     */
    public void update(int id, Movie movie){
        if(getById(id) == null)
            throw new IllegalArgumentException("Sessão não existe!");
        movies.set(getIndex(id), movie);
    }

    /**
     * Método auxiliar para pegar o index de um certo filme
     *
     * @param id da sessão
     * @return se o id existir, retorna o index requerido
     *         caso não existe, retorna -1
     */
    private int getIndex(int id){
        for(int i = 0; i < movies.size(); i++){
            if(movies.get(i).getId() == id){
                return i;
            }
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
     * Remove um filme da lista com base no ID fornecido.
     * 
     * @param id O identificador único do filme a ser removido.
     * @return {@code true} se o filme foi encontrado e removido; {@code false} caso contrário.
     */
    public boolean removeById(int id){
        for(int i = 0; i < movies.size(); i++){
            if (movies.get(i).getId() == id) {
                movies.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna um filme com o mesmo nome fornecido.
     *
     * @param name nome do filme
     * @return filme com o mesmo nome
     */
    public Movie getMovieByName(String name){
        for (Movie movie : movies){
            if(movie.getTitle().trim().equalsIgnoreCase(name.trim())){
                return movie;
            }
        }
        return null;
    }

    /**
     * Remove todos os filmes da lista.
     */
    public void clear(){
        movies.clear();
    }
}