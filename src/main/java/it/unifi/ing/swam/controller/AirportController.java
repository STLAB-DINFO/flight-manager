package it.unifi.ing.swam.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.dao.AirportDao;
import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.model.Airport;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.ModelFactory;

@ConversationScoped
@Named
public class AirportController implements Serializable {

    static final long serialVersionUID = 1L;

    @Inject
    private AirportDao airportDao;
    
    @Inject
    private CountryDao countryDao;

    @Inject
    private Conversation conversation;

    private Airport airport;
    
    private List<Country> countryList;

    public String startAndOpen() {
        if(conversation.isTransient()){
            conversation.begin();
            conversation.setTimeout(1000 * 60 * 20);
        }
        init();
        return "add-airports-main?faces-redirect=true";
    }

    public int stop() {
        String id = conversation.getId();
        if(!conversation.isTransient())
            conversation.end();
        return Integer.valueOf(id);
    }

    public String stopConversationAndReturnHome(){
        stop();
        return "administrator-page?faces-redirect=true";
    }

    public void redirectToHome() throws IOException {
        stop();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/administrator-page.xhtml");
    }

    @Transactional
    public String saveAndAddNewAirport(){
        airportDao.save(airport);
        int oldcid = stop();
         return startAndOpen();

    }
    @Transactional
    public void saveAndGoHome() throws IOException{
        airportDao.save(airport);
        redirectToHome();
    }


// ---- private methods ----
    private void init(){
        if(airport == null) airport = ModelFactory.airport();
    }

// ---- getters & setters ----

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

	public List<Country> getCountryList() {
		if(countryList == null)
			countryList = countryDao.getAllCountries();
		return countryList;
	}

}
