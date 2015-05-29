package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name="Serie")
public class Serie {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String nome;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="TEMP")
	private List<Temporada> temporadas;
	@Column
	private boolean acompanhando;
	
	public Serie(){
		this.temporadas = new ArrayList<Temporada>(); 
	}
	
	public Serie(String nome){
		this();
		this.nome=nome;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Temporada> getTemporadas() {
		return temporadas;
	}
	public void addTemporada(Temporada temporada) {
		this.temporadas.add(temporada);
	}
	public boolean isAcompanhada() {
		return acompanhando;
	}

	public void setAssistida(boolean acompanhando) {
		this.acompanhando = acompanhando;
	}
	
	public int getTemporadasTotal() {
		return temporadas.size();
	}
	
	public Temporada getUltimaTemporada() throws Exception {
		if (getTemporadasTotal()==0)
			throw new Exception("Lista de Temporadas vazia! Nome da serie: "+this.getNome());
		return temporadas.get(temporadas.size()-1);
	}
	
	//pega o proximo episodio nao assistido imediatamente depois do ultimo assistido
	public Episodio getProximoEpisodio() {
		for (int i = this.getTemporadasTotal(); i > 0; i--) {
			List<Episodio> temp = this.getTemporadas().get(i-1).getEpisodios();
			for (int j = temp.size(); j >0; j--) {
				//o primeiro episodio achado, de tras para frente
				if (temp.get(j-1).isAssistido()){
					//se ultimo episodio a que assisti foi o ultimo de uma temporada
					if (j==temp.size()){
						//se for o ultimo episodio da ultima temporada
						if (i==this.getTemporadasTotal()){
							return new Episodio("Último episódio da serie já assistido", new Temporada(0, this), 0);
						}else{
							//pegue o primeiro da outra temporada
							return this.getTemporadas().get(i).getEpisodios().get(0);
						}
					}else{
						//pegue o proximo episodio da temporada
						return temp.get(j);
					}
				}
			}			
		}
		return new Episodio("Nenhum episodio assistido", new Temporada(0, this), 0);
	}
}
