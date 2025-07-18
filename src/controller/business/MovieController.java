package controller.business;

import models.Movie;
import models.Session;
import repository.MovieRepository;
import services.MovieService;

import java.util.LinkedList;

/**
 * Classe de controle para a lógica de negócio dos filmes.
 * @author Gabryelle Beatriz Duarte Moraes 
 * @since 11/06/2025
 * @version 1.0
 */
public class MovieController {

    private static MovieService movieService = new MovieService(new MovieRepository(true));

    /**
     * Método para inicializar o controller com suas dependências.
     * Deve ser chamado no início da aplicação.
     */
    public static void initialize(MovieRepository repository) {
        if (movieService == null) {
            movieService = new MovieService(repository);
        }
    }

    /**
     * adiciona um filme chamando o método addMovie da classe MovieService.
     * @param title título do filme
     * @param genre gênero do filme
     * @param duration duração do filme
     * @param classification classificação indicativa do filme
     * @param synopsis sinopse do filme
     * @return true se o filme foi adicionado com sucesso, ou false se já existe um filme com o mesmo título ou com duração menor que zero
     */
    public static String addMovie(String title, String genre, int duration, String classification, String synopsis){
        return movieService.addMovie(title, genre, duration, classification, synopsis);
    }

    /**
     * Atualiza um filme existente pelo ID fornecido.
     * @param id O ID do filme.
     * @param title título do filme
     * @param genre gênero do filme
     * @param duration duração do filme
     * @param classification classificação indicativa do filme
     * @param synopsis sinopse do filme
     * @return Mensagem de sucesso ou erro.
     */
    public static String updateMovie(int id, String title, String genre, int duration, String classification, String synopsis){
        return movieService.updateMovie(id, title, genre, duration, classification, synopsis);
    }

    /**
     * Pega todos os filmes cadastrados.
     * @return Lista de filmes.
     */
    public static LinkedList<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }

    /**
     * Retorna um filme existente pelo ID fornecido.
     * @param id O ID do filme.
     * @return O filme encontrado ou null caso não exista
     */
    public static Movie getMovieById(int id){
        return movieService.getMovieById(id);
    }

    /**
     * Remove o filme pelo ID fornecido.
     * @param id O ID do filme.
     * @return retorna true se o filme foi removido, false se não foi encontrado.
     */
    public static boolean removeMovieById(int id){
        return movieService.removeMovieById(id);
    }

    /**
     * Retorna um filme com o mesmo nome fornecido.
     *
     * @param name nome do filme
     * @return filme com o mesmo nome
     */
    public static Movie getMovieByName(String name){
        return movieService.getMovieByName(name);
    }

    /**
     * Retorna uma lista de sessões que irão passar um certo filme
     *
     * @param id id do filme a ser pesquisado
     * @return lista de sessões que irão passar o filme fornecido
     */
    public static LinkedList<Session> getSessionsByMovie(int id) {
        return movieService.getSessionsByMovie(id);
    }

    /**
     * Remove todos os filmes cadastrados.
     */
    public static void removeAllMovies(){
        movieService.removeAllMovies();
    }
    
}
