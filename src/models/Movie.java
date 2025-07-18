package models;

/**
 * Classe que representa um filme.
 * 
 * @author Gabryelle Beatriz Duarte Moraes
 * @since 01/06/2024
 * @version 1.0
 */
public class Movie {
	private static int _idGenerator = 1;
	private int id;
	private String title;
	private String genre;
	private int duration;
	private String classification;
	private String synopsis;
	

	/**
	 * Construtor da classe Movie.
	 * Cria um novo filme com os dados fornecidos e atribui automaticamente um ID único.
	 * 
	 * @param title Título do filme
	 * @param genre Gênero do filme
	 * @param duration Duração do filme em minutos
	 * @param classification Classificação indicativa do filme
	 * @param synopsis Sinopse do filme
	 */
	public Movie(String title, String genre, int duration, String classification, String synopsis) {
		this.title = title;
		this.genre = genre;
		this.duration = duration;
		this.classification = classification;
		this.synopsis = synopsis;
		id=_idGenerator++;
	}

	/**
	 * Construtor para RECONSTRUIR filmes a partir de dados salvos.
	 * Recebe o ID existente do arquivo.
	 */
	public Movie(int id, String title, String genre, int duration, String classification, String synopsis) {
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.duration = duration;
		this.classification = classification;
		this.synopsis = synopsis;

		if (id >= _idGenerator) {
			_idGenerator = id + 1;
		}
	}

	/**
	 * Obtém o ID único do filme.
	 * 
	 * @return O ID do filme
	 */
    public int getId() {
		return id;
	}
    
	/**
	 * Obtém o título do filme.
	 * 
	 * @return O título do filme
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Define o título do filme.
	 * 
	 * @param title O novo título do filme
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Obtém o gênero do filme.
	 * 
	 * @return O gênero do filme
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * Define o gênero do filme.
	 * 
	 * @param genre O novo gênero do filme
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * Obtém a duração do filme.
	 * 
	 * @return A duração do filme em minutos
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Define a duração do filme.
	 * 
	 * @param duration A nova duração do filme em minutos
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Obtém a classificação indicativa do filme.
	 * 
	 * @return A classificação indicativa do filme
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * Define a classificação indicativa do filme.
	 * 
	 * @param classification A nova classificação indicativa do filme
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * Obtém a sinopse do filme.
	 * 
	 * @return A sinopse do filme
	 */
	public String getSynopsis() {
		return synopsis;
	}

	/**
	 * Define a sinopse do filme.
	 * 
	 * @param synopsis A nova sinopse do filme
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	/**
	 * Retorna uma representação em string do filme.
	 * Inclui todas as informações do filme formatadas para exibição.
	 * 
	 * @return String contendo todas as informações do filme
	 */
	@Override
	public String toString() {
		return " Movie id=" + id + 
				"\nTitle=" + title + 
				"\nGenre=" + genre +
				"\nDuration=" + duration+
				"\nClassification=" + classification + 
				"\nSynopsis=" + synopsis ;
	}
	
	/**
	 * Resetar o ID gerado para o próximo teste.
	 */
	public static void resetIdGenerator() {
		_idGenerator = 1;
	}
}
