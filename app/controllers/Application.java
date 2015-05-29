package controllers;

import java.util.List;

import models.Episodio;
import models.Serie;
import models.Temporada;
import models.dao.GenericDAO;
import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	private static final GenericDAO dao = new GenericDAO();
	private static Form<Serie> serieForm = Form.form(Serie.class);
	
	@Transactional
    public static Result index() {		
    	List<Serie> series = dao.findAllByClass(Serie.class);
        return ok(index.render(series));
    }
	
	@Transactional
	public static Result acompanharSerie() {
		Form<Serie> filledForm = serieForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = dao.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Serie serie = dao.findByEntityId(Serie.class, id);			
			serie.setAssistida(true);
            
			dao.merge(serie);
			dao.flush();
			
			Logger.debug("Assistindo a serie: " + filledForm.data().toString() + " como " + serie.getNome() + " ID: "+serie.getId());
            
			return redirect(routes.Application.index());
		}
	}
	
	@Transactional
	public static Result assistirAEpisodio() {
		Form<Serie> filledForm = serieForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            List<Serie> result = dao.findAllByClass(Serie.class);
			return badRequest(views.html.index.render(result));
		} else {
			long id = Long.parseLong(filledForm.data().get("id"));
			Episodio episodio = dao.findByEntityId(Episodio.class, id);
			
			episodio.setAssistido(true);			
			
			dao.merge(episodio);
			dao.flush();
			
			Logger.debug("Assistiu a episodio: " + filledForm.data().toString() + " como " + episodio.getNome() + " ID: "+episodio.getId());
            
			return redirect(routes.Application.index());
		}
	}
}
