package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name="Temporada")
public class Temporada {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private int numero;
	@ManyToOne(cascade=CascadeType.ALL)
	Serie serie;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="EPI")
	List<Episodio> episodios;
	/*-1: nenhum episodio assistido 
	 * 0: ao menos um episodio assistido, mas nao todos
	 1: todos os episodios da temporada assistidos*/
	@Column
	private int assistida = -1;
	
	public Temporada(){
		this.episodios = new ArrayList<Episodio>();
	}
	
	public Temporada(int numero,Serie serie){
		this();
		this.numero = numero;
		this.serie = serie;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	public List<Episodio> getEpisodios() {
		return episodios;
	}
	public void addEpisodio(Episodio episodio) {
		this.episodios.add(episodio);
		checarSeAssistida();
	}
	public int getAssistida() {
		return assistida;
	}
	public void setAssistida(int assistida) {
		if (assistida>=-1 && assistida<=1)
			this.assistida = assistida;
	}
	public void checarSeAssistida() {
		int contador = 0;
		for (int i = 0; i < episodios.size(); i++) {
			if (episodios.get(i).isAssistido()){
				contador+=1;
			}
		}
		if (contador==episodios.size())
			setAssistida(1);
		else if (contador>0)
			setAssistida(0);
		else
			setAssistida(-1);
	}
}
