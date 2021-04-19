package it.unifi.ing.swam.controller.booking;

import it.unifi.ing.swam.components.TemporaryReservationComponent;
import it.unifi.ing.swam.components.billing.BillingComponent;
import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.dao.FlightDao;
import it.unifi.ing.swam.model.Booking;
import it.unifi.ing.swam.model.Flight;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.Passenger;
import it.unifi.ing.swam.util.Util;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class BookingController {

    @Inject
    protected BookingDao bookingDao;

    @Inject
    protected FlightDao flightDao;


    @Inject
    protected BillingComponent billingComponent;

    @Inject
    private TemporaryReservationComponent temporaryReservationComponent;


    protected Long flightOut;
    protected Long flightBack;
    protected Flight forwardFlight;
    protected Flight returnFlight;
    protected List<Flight> flights;
    protected Integer nPassengers;
    protected List<Passenger> passengers;
    protected Booking booking;
    protected boolean justBooked;
    protected boolean discounted;
    protected float basePrice;
    protected float finalPrice;
    protected float finalPriceFull;


    public void initBooking() {
        booking = ModelFactory.reservation();
        booking.setDate(new Date());
        booking.setPassengers(getPassengers());
        booking.setFlights(getFlights());
        basePrice = initBasePrice();
        booking.setPrice(getBasePrice());
    }

    public void initialize() {
        reserveTemporarySeats();
        initBooking();
        finalPrice = billingComponent.getFinalPrice(getBasePrice(), booking);
        finalPriceFull = billingComponent.getFinalPriceFull(getBasePrice(), booking);
        booking.setFinalPrice(finalPrice);
    }


    @Transactional
    public String save() {
        Long lastId = bookingDao.getIdFromLastReservation();
        booking.setReservationId("FMID-" + (lastId.intValue() + 1));
        booking.setPassengers(passengers);
        bookingDao.save(booking);
        justBooked = true;
        updateReservedSeats();
        return "confirmation";
    }

    private float initBasePrice() {
        return getBasePriceFlightBack() * nPassengers + getBasePriceFlightOut() * nPassengers;
    }

    private float getBasePriceFlightOut() {
        return booking.getFlights().get(0).getPricePerPerson();
    }

    private float getBasePriceFlightBack() {
        if (booking.getFlights().size() > 1) {
            return booking.getFlights().get(1).getPricePerPerson();
        }
        return 0;
    }

    public float getPriceFlightOut() {
        float basePrice = getBasePriceFlightOut();
        if(billingComponent.getCountry()==null){
            billingComponent.setCountry(booking.getFlights().get(0).getDestinationAirport().getAirportCountry());
        }
        return basePrice + basePrice * billingComponent.getFee();
    }


    public float getPriceFlightBack() {
        float basePrice = getBasePriceFlightBack();
        if(billingComponent.getCountry()==null){
            billingComponent.setCountry(booking.getFlights().get(1).getDestinationAirport().getAirportCountry());
        }
        return basePrice + basePrice * billingComponent.getFee();
    }

    public float getTotalPriceForward() {
        if (flightOut != null && flightOut != 0) {
            return Util.round(getPriceFlightOut() * getnPassengers().floatValue(), 2);
        }
        return 0;
    }

    public float getTotalPriceReturn() {
        if (flightBack != null && flightBack != 0) {
            return Util.round(getPriceFlightBack() * getnPassengers().floatValue(), 2);
        }
        return 0;
    }


    public void reserveTemporarySeats() {
        if (getFlights() != null && nPassengers != null) {
            temporaryReservationComponent.reserveTemporarySeats(getFlights(), nPassengers);
        }
    }

    @Transactional
    public void cleanTemporaryReservations() {
        if (temporaryReservationComponent != null) {
            temporaryReservationComponent.removeTemporarySeats();
        }
    }

    @Transactional
    public void updateReservedSeats() {
        Flight flightOut = booking.getFlights().get(0);
        flightOut.setReservedSeats(flightOut.getReservedSeats() + booking.getPassengers().size());
        flightDao.save(flightOut);
        if (booking.getFlights().size() > 1) {
            Flight flightBack = booking.getFlights().get(1);
            flightBack.setReservedSeats(flightBack.getReservedSeats() + booking.getPassengers().size());
            flightDao.save(flightBack);
        }
    }

    public Long getFlightOut() {
        return flightOut;
    }

    public void setFlightOut(Long flightOut) {
        this.flightOut = flightOut;
    }

    public Long getFlightBack() {
        return flightBack;
    }

    public void setFlightBack(Long flightBack) {
        this.flightBack = flightBack;
    }

    public Integer getnPassengers() {
        return nPassengers;
    }

    public void setnPassengers(Integer nPassengers) {
        this.nPassengers = nPassengers;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Passenger> getPassengers() {
        passengers = new ArrayList<Passenger>();
        for (int i = 0; i < getnPassengers(); i++) {
            passengers.add(ModelFactory.passenger());
        }

        return passengers;
    }

    public List<Flight> getFlights() {
        if (flights == null || flights.isEmpty()) {
            flights = new ArrayList<Flight>();
            if (flightOut != null && flightOut != 0)
                flights.add(flightDao.findById(flightOut));
            if (flightBack != null && flightBack != 0)
                flights.add(flightDao.findById(flightBack));
        }

        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public Flight getForwardFlight() {
        if (forwardFlight == null) {
            if (flightOut != null && flightOut != 0)
                forwardFlight = flightDao.findById(flightOut);
        }

        return forwardFlight;
    }

    public Flight getReturnFlight() {
        if (returnFlight == null) {
            if (flightBack != null && flightBack != 0)
                returnFlight = flightDao.findById(flightBack);
        }

        return returnFlight;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }


    public boolean isDiscounted() {
        return billingComponent.isDiscounted();
    }

    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }

    public float getFinalPriceFull() {
        return finalPriceFull;
    }

    public void setFinalPriceFull(float finalPriceFull) {
        this.finalPriceFull = finalPriceFull;
    }
}
