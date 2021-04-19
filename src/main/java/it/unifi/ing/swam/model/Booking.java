package it.unifi.ing.swam.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="booking")
public class Booking extends BaseEntity { //Booking
	
	private String reservationId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private float price;
	
	private float finalPrice;
	
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Passenger> passengers;

	@OneToOne
	private User registeredUserOwner;
	
	@ManyToMany
	@JoinTable(name = "reservation_flight",
	    joinColumns = @JoinColumn(name = "Reservation_id"),
	    inverseJoinColumns = @JoinColumn(name = "flights_id")
	)
	private List<Flight> flights;

/*	//TODO leva
	@ManyToOne(cascade=CascadeType.ALL)
	private Strategy priceCalculation;*/
	
	public Booking() {
		super();
	}
	
	public Booking(String uuid) {
		super(uuid);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public float getFinalPrice() {
		return finalPrice;
	}
	
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public User getRegisteredUserOwner() {
		return registeredUserOwner;
	}

	public void setRegisteredUserOwner(User registeredUserOwner) {
		this.registeredUserOwner = registeredUserOwner;
	}

	/*
	public Strategy getPriceCalculation() {
		return priceCalculation;
	}

	public void setPriceCalculation(Strategy priceCalculation) {
		this.priceCalculation = priceCalculation;
	}*/
	
}
