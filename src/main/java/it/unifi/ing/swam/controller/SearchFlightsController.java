package it.unifi.ing.swam.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import it.unifi.ing.swam.components.FlightManagerComponent;
import it.unifi.ing.swam.components.LoggedUserComponent;
import it.unifi.ing.swam.components.TemporaryReservationComponent;
import it.unifi.ing.swam.components.billing.BillingComponent;
import it.unifi.ing.swam.controller.booking.RegisteredBookingController;
import it.unifi.ing.swam.controller.booking.VisitorBookingController;
import it.unifi.ing.swam.dao.AirportDao;
import it.unifi.ing.swam.model.Country;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.util.Util;

@SessionScoped
@Named
public class SearchFlightsController implements Serializable {

	static final long serialVersionUID = 39487298L;

	private final int maxPassengersPerBooking = 15;

	@Inject
	private TemporaryReservationComponent temporaryReservationComponent;

	@Inject
	private VisitorBookingController visitorBookingController;

	@Inject
	private RegisteredBookingController registeredBookingController;

	@Inject
	private FlightManagerComponent flightManagerComponent;

	@Inject
	private BillingComponent billingComponent;

	@Inject
	private LoggedUserComponent loggedUser;

	@Inject
	private AirportDao airportDao;

	private Long flightOut;
	private Long flightBack;
	private int nPassengers;
	private String oneWay = "oneway";
	private Date departureDate;
	private Date backDate;
	private List<String> sources;
	private List<String> destinations;
	private String source;
	private String destination;
	private String enabledDateS;
	private String enabledDateBackS;
	private float totalFee;
	private float countryFee;

	private Flight detailedFlight;

	private List<Long> flightsSearchHistory; //TODO storia user
	private String error;

	public List<String> getSources(){
		if(sources == null) {
			sources = airportDao.getAllAirportsNames();
		}
		return sources;
	}

	public List<String> getDestinations(){
		if(destinations == null) {
			destinations = airportDao.getAllAirportsNames();
		}
		return destinations;
	}

	public boolean enabledSource(String airport) {
		if(destination==null) return true;
		return !destination.equals(airport);
	}

	public String getFlightDetails(Flight flight){
		flightOut = flight.getId();
		this.detailedFlight = flightManagerComponent.fetchFlight(flight.getId());
		Country userHomeCountry = null;
		Country destinationCountry = null;

		float userHomeFee = 0;

		if(loggedUser.isLoggedIn()) {
			userHomeCountry = loggedUser.getHomeCountry();
		}
		Country flightDestinationCountry = flight.getDestinationAirport().getAirportCountry();

		if(userHomeCountry == null || !flightDestinationCountry.equals(userHomeCountry))
			destinationCountry = flightDestinationCountry;

		billingComponent.setCountry(destinationCountry);
		float dynamicFee = billingComponent.getFee();
		
		if(loggedUser.isLoggedIn()) {
			userHomeFee = loggedUser.getHomeCountryFee();
		}

		if(userHomeCountry != null && userHomeCountry.equals(flightDestinationCountry))
			countryFee = userHomeFee;
		else
			countryFee = dynamicFee;

		totalFee = Util.round(countryFee*flight.getPricePerPerson(),2);

		return "flight-details?faces-redirect=true";
	}

	public boolean enabledDestination(String airport) {
		if(source==null) return true;
		return !source.equals(airport);
	}

	@PostConstruct
	public void init(){
		System.out.println("startup di SearchFlightsController ");
	}

    public String confirmFlights(String userType){
		flightManagerComponent.stop();
		if(userType.equals("visitor")){
			visitorBookingController.start();
			visitorBookingController.setFlightOut(flightOut);
			visitorBookingController.setFlightBack(flightBack);
			visitorBookingController.setnPassengers(nPassengers);
		}
		if(userType.equals("registered")){
			registeredBookingController.setFlightOut(flightOut);
			registeredBookingController.setFlightBack(flightBack);
			registeredBookingController.setnPassengers(nPassengers);
		}
		return "booking-details?faces-redirect=true";
	}

    public void redirectToHome(String s) throws IOException {
    	error = s;
    	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
    }

	public int getMaxPassengersPerBooking() {
		return maxPassengersPerBooking;
	}

	public Long getFlightOut() {
		if(this.flightOut == null)
			this.flightOut = 0L;

		return flightOut;
	}

	@Transactional
	public boolean isAvailable(Flight flight) {
		int realSeats = flight.getTotalSeats() - flight.getReservedSeats();
		int temp = temporaryReservationComponent.getTemporaryReservedSeats(flight);
		int availableSeats = realSeats - temp;
		return (availableSeats >= nPassengers);
	}

	public int pendingSeats(){
		return temporaryReservationComponent.getTemporaryReservedSeats(detailedFlight);
	}

	public int availableSeats(Flight flight) {
		int realSeats = flight.getTotalSeats() - flight.getReservedSeats();
		int temp = temporaryReservationComponent.getTemporaryReservedSeats(flight);
		int availableSeats = realSeats - temp;

		return availableSeats;
	}
	public String isAvailableStr(Flight flight) {
		if(isAvailable(flight))
			return "";
		return "disabled";
	}

	public String backToFlightList(){
		flightManagerComponent.start();
		return "flights-result?faces-redirect=true";
	}

	public String searchAnotherFlight() {
		flightManagerComponent.searchFlightsOut(departureDate, source, destination);
		if(oneWay.equals("return")){
			flightManagerComponent.searchFlightsBack(backDate, source, destination);
		}
		flightManagerComponent.stop();
		return "flights-result?faces-redirect=true";
	}


	public String searchFlight() {
		flightManagerComponent.start();
		flightManagerComponent.searchFlightsOut(departureDate, source, destination);
		if(oneWay.equals("return")){
			flightManagerComponent.searchFlightsBack(backDate, source, destination);
		}
		return "flights-result?faces-redirect=true";
	}

	public String backToHome(){
		flightManagerComponent.stop();
		return "index?faces-redirect=true";
	}


	public void setFlightOut(Long flightOut) {
		this.flightOut = flightOut;
	}

	public Long getFlightBack() {
		if(this.flightBack == null)
			this.flightBack = 0L;

		return flightBack;
	}

	public void setFlightBack(Long flightBack) {
		this.flightBack = flightBack;
	}

	public int getnPassengers() {
		if(nPassengers == 0)
			nPassengers = 1;

		return nPassengers;
	}

	public void setnPassengers(int nPassengers) {
		this.nPassengers = nPassengers;
	}

	public Date getDepartureDate() {
		if(departureDate == null)
			departureDate = new Date();

		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public Date getBackDate() {
		if(backDate == null){
			backDate = new Date();
			if(departureDate != null)
				backDate.setTime(departureDate.getTime());
		}

		return backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}

	public String getOneWay() {
		if(oneWay == null)
			oneWay = "oneway";

		return oneWay;
	}

	public void setOneWay(String oneWay) {
		this.oneWay = oneWay;
	}

	public void setError(String err) {
		error = err;
	}

	public String getError() {
		return error;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getEnabledDateS() {
		return enabledDateS;
	}

	public void setEnabledDateS(String enabledDateS) {
		this.enabledDateS = enabledDateS;
	}

	public String getEnabledDateBackS() {
		return enabledDateBackS;
	}

	public void setEnabledDateBackS(String enabledDateBackS) {
		this.enabledDateBackS = enabledDateBackS;
	}


	public Flight getDetailedFlight() {
		return detailedFlight;
	}

	public void setDetailedFlight(Flight detailedFlight) {
		this.detailedFlight = detailedFlight;
	}

	public float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(float totalFee) {
		this.totalFee = totalFee;
	}
}
