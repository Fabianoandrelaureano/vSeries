package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name="Episodio")
public class Episodio {
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String nome;
	@Column
	private int numero;
	@ManyToOne(cascade=CascadeType.ALL)
	Temporada temporada;
	@Column
	private boolean assistido;
	
	public Episodio(){
	}
	public Episodio(String nome, Temporada temporada, int numero){
		this.nome = nome;
		this.temporada = temporada;
		this.numero = numero;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
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
	public Temporada getTemporada() {
		return temporada;
	}
	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}
	public boolean isAssistido() {
		return assistido;
	}
	public void setAssistido(boolean assistido) {
		this.assistido = assistido;
		temporada.checarSeAssistida();
	}
}