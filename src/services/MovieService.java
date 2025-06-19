package services;

import controller.business.SessionController;
import models.Movie;
import models.Session;
import repository.MovieRepository;

import java.util.List;

/**
 * Classe Service responsável pela lógica denegícios relacionada a filmes.
 *
 * @author Gabryelle Beatriz Duarte Moraes
 * @version 1.0
 * @since 14/06/2024
 */
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Adiciona um novo filme
     *
     * @param title          título do filme
     * @param genre          gênero do filme
     * @param duration       duração do filme
     * @param classification classificação indicativa do filme
     * @param synopsis       sinopse do filme
     * @return true se o filme foi adicionado com sucesso, ou false se já existe um filme com o mesmo título ou com duração menor que zero
     */
    public String addMovie(String title, String genre, int duration, String classification, String synopsis) {
        //Verificações básicas
        if (title == null || title.isBlank()) {
            throw new IllegalAccessError("O título do filme não pode ser vazio!");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("A duração do filme deve ser maior que zero!");
        }

        //Verifica se já existe um filme com o mesmo título
        List<Movie> existingMovies = movieRepository.getAll();
        for (int i = 0; i < existingMovies.size(); i++) {
            if (existingMovies.get(i).getTitle().equalsIgnoreCase(title)) {
                return "Filme não foi adicionado."; //Indica filme duplicado
            }
        }

        //Cria o filme e envia para o Repository
        Movie movie = new Movie(title, genre, duration, classification, synopsis);
        movieRepository.add(movie);
        return "Filme adicionado com sucesso!";
    }

    /**
     * Método que atualiza um certo filme
     *
     * @param id             do filme a ser atualizado
     * @param title          titulo do filme atualizado
     * @param genre          genero do filme atualizado
     * @param duration       duração do filme atualizado
     * @param classification classificação indicativa do filme atualizado
     * @param synopsis       sinopse do filme atualizado
     * @return {@code Filme adicionado com sucesso} se o filme for adicionado
     */
    public String updateMovie(int id, String title, String genre, int duration, String classification, String synopsis) {
        Movie movie = getMovieById(id);
        if (movie == null)
            throw new IllegalAccessError("O id do filme não existe!");

        //Verificações básicas
        if (title == null || title.isBlank()) {
            throw new IllegalAccessError("O título do filme não pode ser vazio!");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("A duração do filme deve ser maior que zero!");
        }

        movie.setClassification(classification);
        movie.setDuration(duration);
        movie.setGenre(genre);
        movie.setSynopsis(synopsis);
        movie.setTitle(title);
        return "Filme adicionado com sucesso!";
    }

    /**
     * Retorna a lista de todos os filmes cadastrados.
     *
     * @return Lista de filmes.
     */
    public GenericDynamicList<Movie> getAllMovies() {
        return movieRepository.getAll();
    }

    /**
     * Retorna um filme pelo ID fornecido.
     *
     * @param id O ID do filme.
     * @return O filme encontrado ou null caso não exista
     * @throws IllegalArgumentException se o ID for menor ou igual a zero.
     * @throws RuntimeException         se não existir um filme com esse ID.
     */
    public Movie getMovieById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID deve ser maior que zero!");
        }

        Movie movie = movieRepository.getById(id);
        if (movie == null) {
            throw new IllegalAccessError("Nenhum filme encontrado com o ID " + id);
        }

        return movieRepository.getById(id);
    }

    /**
     * Remove o filme pelo ID fornecido.
     *
     * @param id O ID do filme.
     * @return true se o filme foi removido, false se não foi encontrado.
     * @throws IllegalArgumentException se o ID for menor ou igual a zero.
     */
    public boolean removeMovieById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID deve ser maior que zero!");
        }

        boolean removed = movieRepository.removeById(id);
        if (!removed) {
            throw new RuntimeException("Nenhum filme encontrado com o ID" + id);
        }

        return removed;
    }

    /**
     * Retorna um filme com o mesmo nome fornecido.
     *
     * @param name nome do filme
     * @return filme com o mesmo nome
     */
    public Movie getMovieByName(String name) {
        return movieRepository.getMovieByName(name);
    }

    /**
     * Retorna uma lista de sessões que irão passar um certo filme
     *
     * @param id id do filme a ser pesquisado
     * @return lista de sessões que irão passar o filme fornecido
     */
    public GenericDynamicList<Session> getSessionsByMovie(int id) {
        Movie movie = getMovieById(id);
        if (movie == null)
            throw new IllegalAccessError("O id do filme não existe!");
        GenericDynamicList<Session> sessions = SessionController.getAllSessions();
        GenericDynamicList<Session> sessionsWithMovie = new GenericDynamicList<>();
        for (Session session : sessions) {
            try {
                if (session.getMovie() == movie) {
                    sessionsWithMovie.append(session);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return sessionsWithMovie;
    }

    /**
     * Remove todas os filmes cadastrados
     */
    public void removeAllMovies() {
        movieRepository.clear();
    }
}
