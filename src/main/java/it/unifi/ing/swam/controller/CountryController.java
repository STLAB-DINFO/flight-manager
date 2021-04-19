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

import it.unifi.ing.swam.dao.CountryDao;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.ModelFactory;

@Named
@ConversationScoped
public class CountryController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private CountryDao countryDao;

	private List<Country> countryList;

	private Country country;

	public void init() {
		country = ModelFactory.country();
	}

	public int stop() {
		String id = conversation.getId();
		if (!conversation.isTransient())
			conversation.end();
		return Integer.valueOf(id);
	}

	public List<Country> getCountryList() {
		if (countryList == null)
			countryList = countryDao.getAllCountries();
		return countryList;
	}

	public String startCountryAddition() {
		if (conversation.isTransient()) {
			conversation.begin();
			conversation.setTimeout(1000 * 60 * 20);
		}
		init();
		return "add-country?faces-redirect=true";
	}

	public Country getCountry() {
		return country;
	}

	public void redirectToHome() throws IOException {
		stop();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(ec.getRequestContextPath() + "/administrator-page.xhtml");
	}

	@Transactional
	public void saveCountry() throws IOException {
		countryDao.save(country);
		redirectToHome();
	}

	public String cancel() {
		stop();
		return "administator-page?faces-redirect=true";
	}

}
