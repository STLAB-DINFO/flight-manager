//package it.unifi.ing.swam.dao;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import it.unifi.ing.swam.model.*;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.Test;
//import org.junit.runners.model.InitializationError;
//
//import it.unifi.ing.swam.model.Booking;
//
//public class BookingDaoTest extends JpaTest {
//	
//	private BookingDao bookingDao;
//	private Booking booking;
//	private Passenger passenger;
//	private Flight flightOut;
//	private Flight flightBack;
//	private Airport sourceAirport;
//	private Airport destinationAirport;
//	
//	@Override
//	protected void init() throws InitializationError {
//		
//		sourceAirport = ModelFactory.airport();
//		sourceAirport.setCityName("Pisa");
//		sourceAirport.setZIP(12345);
//		
//		entityManager.persist(sourceAirport);
//		
//		destinationAirport = ModelFactory.airport();
//		destinationAirport.setCityName("Palermo");
//		destinationAirport.setZIP(54321);
//		
//		entityManager.persist(destinationAirport);
//		
//		flightOut = ModelFactory.flight();
//		flightOut.setFlightNumber("F1234");
//		flightOut.setDepartureDate( new Date() );
//		flightOut.setDepartureTime( new Date() );
//		flightOut.setTotalSeats(100);
//		flightOut.setPricePerPerson(50);
//		flightOut.setFlightDuration(90);
//		flightOut.setSourceAirport(sourceAirport);
//		flightOut.setDestinationAirport(destinationAirport);
//		
//		entityManager.persist(flightOut);
//		
//		flightBack = ModelFactory.flight();
//		flightBack.setFlightNumber("F4321");
//		flightBack.setDepartureDate( new Date() );
//		flightBack.setDepartureTime( new Date() );
//		flightBack.setTotalSeats(100);
//		flightBack.setPricePerPerson(60);
//		flightBack.setFlightDuration(90);
//		flightBack.setSourceAirport(destinationAirport);
//		flightBack.setDestinationAirport(sourceAirport);
//		
//		entityManager.persist(flightBack);
//		
//		passenger = ModelFactory.passenger();
//		passenger.setFirstName("James");
//		passenger.setSurname("Smith");
//		passenger.setIdCard("AB12345");
//		passenger.setDateOfBirth(new Date());
//		
//		entityManager.persist(passenger);
//		
//		List<Flight> flights = new ArrayList<Flight>();
//		flights.add(flightOut);
//		flights.add(flightBack);
//		
//		List<Passenger> passengers = new ArrayList<Passenger>();
//		passengers.add(passenger);
//		
//		booking = ModelFactory.reservation();
//		booking.setDate( new Date() );
//		booking.setEmail("mail@example.com");
//		booking.setFlights(flights);
//		booking.setPassengers(passengers);
//		booking.setPrice(160);
//		
//		entityManager.persist(booking);
//		
//		bookingDao = new BookingDao();
//		
//		try {
//			FieldUtils.writeField(bookingDao, "entityManager", entityManager, true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//	}
//	
//	@Test
//	public void testSave() {
//		List<Flight> flights = new ArrayList<Flight>();
//		flights.add(flightBack);
//		flights.add(flightOut);
//		entityManager.merge(flightBack);
//		entityManager.merge(flightOut);
//		
//		Passenger anotherPassenger = ModelFactory.passenger();
//		anotherPassenger.setFirstName("Bob");
//		anotherPassenger.setSurname("Smith");
//		anotherPassenger.setIdCard("AB54321");
//		anotherPassenger.setDateOfBirth(new Date());
//		
//		List<Passenger> passengers = new ArrayList<Passenger>();
//		passengers.add(anotherPassenger);
//		
//		Booking anotherBooking = ModelFactory.reservation();
//		anotherBooking.setDate( new Date() );
//		anotherBooking.setEmail("mail2@example2.com");
//		anotherBooking.setFlights(flights);
//		anotherBooking.setPassengers(passengers);
//		anotherBooking.setPrice(160);
//		
//		bookingDao.save(anotherBooking);
//		
//		assertEquals(anotherBooking, entityManager
//				.createQuery("from Reservation where uuid = :uuid")
//				.setParameter("uuid", anotherBooking.getUuid())
//				.getSingleResult());
//	}
//	
//	@Test
//	public void testFindById() {
//		Booking result = bookingDao.findById(booking.getId());
//		
//		assertEquals(booking, result);
//	}
//	
//	@Test
//	public void testFindByWrongId() {
//		Booking result = bookingDao.findById(new Long(9999));
//		
//		assertNull(result);
//	}
//	
//	@Test
//	public void testDelete() {
//		bookingDao.delete(booking);
//		
//		assertNull(bookingDao.findById(booking.getId()));
//	}
//	
//	@Test
//	public void testGetIdFromLastReservation() {
//		assertEquals(new Long(1), bookingDao.getIdFromLastReservation());
//	}
//	
//	@Test
//	public void testEmptyGetIdFromLastReservation() {
//		bookingDao.delete(booking);
//		assertEquals(new Long(0), bookingDao.getIdFromLastReservation());
//	}
//	
//	@Test
//	public void testSearchReservation() {
//		List<Flight> flights = new ArrayList<Flight>();
//		flights.add(flightBack);
//		flights.add(flightOut);
//		entityManager.merge(flightBack);
//		entityManager.merge(flightOut);
//		
//		Passenger anotherPassenger = ModelFactory.passenger();
//		anotherPassenger.setFirstName("Bob");
//		anotherPassenger.setSurname("Smith");
//		anotherPassenger.setIdCard("AB54321");
//		anotherPassenger.setDateOfBirth(new Date());
//		
//		List<Passenger> passengers = new ArrayList<Passenger>();
//		passengers.add(anotherPassenger);
//		
//		Long lastId = bookingDao.getIdFromLastReservation();
//		
//		Booking anotherBooking = ModelFactory.reservation();
//		anotherBooking.setDate( new Date() );
//		anotherBooking.setEmail("mail2@example2.com");
//		anotherBooking.setFlights(flights);
//		anotherBooking.setPassengers(passengers);
//		anotherBooking.setReservationId("FMID-" + (lastId + 1));
//		anotherBooking.setPrice(160);
//		
//		bookingDao.save(anotherBooking);
//		
//		assertEquals(anotherBooking, bookingDao.searchReservation(anotherBooking.getReservationId(),
//																			anotherBooking.getEmail()));
//	}
//	
//	@Test
//	public void testEmptySearchReservation() {
//		assertNull(bookingDao.searchReservation("FMID-9999", "inexistent@email.com"));
//	}
//}
