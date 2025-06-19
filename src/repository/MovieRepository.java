package repository;

import models.Movie;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe que manipula diretamente a lista de filmes.
 * Utiliza a estrutura LinkedList como um banco de dados para armazenar os filmes.
 * @author Vinícius Nunes de Andrade
 * @author Thiago Ferreira Ribeiro
 * @since 11/06/2025
 * @version 2.0
 */
public class MovieRepository {
    private List<Movie> movies = new LinkedList<>();
    
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