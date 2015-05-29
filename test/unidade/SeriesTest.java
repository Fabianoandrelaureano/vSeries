package unidade;


import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import models.Episodio;
import models.Serie;
import models.Temporada;
import models.dao.GenericDAO;

import org.junit.Test;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import play.GlobalSettings;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.Helpers;
import scala.Option;

public class SeriesTest {
	Serie serie1 = new Serie("South Park");
	Serie serie2 = new Serie("Family Guy");
	Temporada temp1 = new Temporada(1, serie1);
	Temporada temp2 = new Temporada(1, serie2);
	Episodio epi1 = new Episodio("Volcano", temp1, 3);
	Episodio epi2 = new Episodio("Damien",temp1,10);	
	Episodio epi3 = new Episodio("Death",temp1,6);
	Episodio epi4 = new Episodio("Death has a Shadow", temp2, 1);
	
	GenericDAO dao = new GenericDAO();
	List<Serie> series;
	
	public EntityManager em;
    
    @Before
    public void setUp() {
        FakeApplication app = Helpers.fakeApplication(new GlobalSettings());
        Helpers.start(app);
        Option<JPAPlugin> jpaPlugin = app.getWrappedApplication().plugin(JPAPlugin.class);
        em = jpaPlugin.get().em("default");
        JPA.bindForCurrentThread(em);
        em.getTransaction().begin();
    }
	@Test
	public void deveIniciarSemSeries() {
		series = dao.findAllByClass(Serie.class);
		assertThat(series.size()==0).isTrue();
	}
	
	@Test
	public void deveAdicionarSerie() {
		dao.persist(serie1);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.size()==1).isTrue();
		assertThat(series.get(0).getNome()).isEqualTo("South Park");
		dao.persist(serie2);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.size()==2).isTrue();
		assertThat(series.get(1).getNome()).isEqualTo("Family Guy");
	}
	
	@Test
	public void deveRegistrarTemporada() {
		serie1.addTemporada(temp1);
		dao.persist(serie1);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadas().size()==1).isTrue();
		assertThat(series.get(0).getTemporadas().get(0).getSerie()).isEqualTo(serie1);
		assertThat(series.get(0).getTemporadas().get(0).getEpisodios().size()==0).isTrue();
	}
	
	@Test
	public void deveRegistrarEpisodio() {
		serie1.addTemporada(temp1);
		temp1.addEpisodio(epi1);
		dao.persist(serie1);
		series = dao.findAllByClass(Serie.class);
		//verifique que a primeira temporada cadastrada da serie tem um episódio cadastrado
		assertThat(series.get(0).getTemporadas().get(0).getEpisodios().size()==1).isTrue();
		//verifique o nome do episódio
		assertThat(series.get(0).getTemporadas().get(0).getEpisodios().get(0).getNome()).isEqualTo("Volcano");
	}
	
	@Test
	public void deveRegistrarSerieAssistida() {
		serie1.addTemporada(temp1);
		temp1.addEpisodio(epi1);
		dao.persist(serie1);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.get(0).isAcompanhada()).isFalse();
		serie1.setAssistida(true);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.get(0).isAcompanhada()).isTrue();
	}
	
	@Test
	public void deveRegistrarEpisodioAssistido() {
		serie1.addTemporada(temp1);
		temp1.addEpisodio(epi1);
		dao.persist(serie1);
		series = dao.findAllByClass(Serie.class);
		//verifique que o episodio adicionado nao esta sendo assistido
		assertThat(series.get(0).getTemporadas().get(0).getEpisodios().get(0).isAssistido()).isFalse();
		epi1.setAssistido(true);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadas().get(0).getEpisodios().get(0).isAssistido()).isTrue();
	}
	//testa a funcionalidade que exige que se saiba se nenhum, algum ou todos
	//os episodios de uma temporada foram assistidos.
	@Test
	public void deveAtualizarEstadoDeTemporada() {
		serie1.addTemporada(temp1);
		temp1.addEpisodio(epi1);
		temp1.addEpisodio(epi3);
		dao.persist(serie1);
		series = dao.findAllByClass(Serie.class);
		assertThat(series.get(0).getTemporadas().get(0).getAssistida()==-1).isTrue();
		epi1.setAssistido(true);
		assertThat(series.get(0).getTemporadas().get(0).getAssistida()==0).isTrue();
		epi3.setAssistido(true);
		assertThat(series.get(0).getTemporadas().get(0).getAssistida()==1).isTrue();
		temp1.addEpisodio(epi2);
		assertThat(series.get(0).getTemporadas().get(0).getAssistida()==0).isTrue();
	}
	
	@After
    public void tearDown() {
        em.getTransaction().commit();
        JPA.bindForCurrentThread(null);
        em.close();
    }
}