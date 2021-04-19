//package it.unifi.ing.swam.controller;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import it.unifi.ing.swam.dao.BookingDao;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runners.model.InitializationError;
//
//import it.unifi.ing.swam.components.BookingSessionComponent;
//import it.unifi.ing.swam.model.Airport;
//import it.unifi.ing.swam.model.Flight;
//import it.unifi.ing.swam.model.ModelFactory;
//import it.unifi.ing.swam.model.Passenger;
//import it.unifi.ing.swam.model.Booking;
//
//public class BookingLoginControllerTest {
//	
//	private BookingLoginController bookingLoginController;
//	
//	private BookingSessionComponent reservationSession;
//	
//	private BookingDao bookingDao;
//
//	private Booking bookingData;
//	
//	private Airport sourceAirport;
//	private Airport destinationAirport;
//	
//	private Flight flightOut;
//	private Flight flightBack;
//	
//	private Passenger passenger;
//	
//	@Before
//	public void setUp() throws InitializationError {
//		bookingLoginController = new BookingLoginController();
//		
//		bookingDao = mock(BookingDao.class);
//		
//		reservationSession = new BookingSessionComponent();
//		
//		sourceAirport = ModelFactory.airport();
//		sourceAirport.setCityName("Pisa");
//		sourceAirport.setZIP(12345);
//		
//		destinationAirport = ModelFactory.airport();
//		destinationAirport.setCityName("Palermo");
//		destinationAirport.setZIP(54321);
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
//		passenger = ModelFactory.passenger();
//		passenger.setFirstName("James");
//		passenger.setSurname("Smith");
//		passenger.setIdCard("AB12345");
//		passenger.setDateOfBirth(new Date());
//		
//		List<Flight> flights = new ArrayList<Flight>();
//		flights.add(flightOut);
//		flights.add(flightBack);
//		
//		List<Passenger> passengers = new ArrayList<Passenger>();
//		passengers.add(passenger);
//		
//		bookingData = ModelFactory.reservation();
//		bookingData.setDate( new Date() );
//		bookingData.setEmail("mail@example.com");
//		bookingData.setFlights(flights);
//		bookingData.setPassengers(passengers);
//		bookingData.setPrice(110);
//		
//		try {
//			FieldUtils.writeField(bookingData, "id", Long.valueOf(10), true);
//			FieldUtils.writeField(bookingLoginController, "bookingDao", bookingDao, true);
//			FieldUtils.writeField(bookingLoginController, "reservationSession", reservationSession, true);
//			FieldUtils.writeField(reservationSession, "bookingDao", bookingDao, true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//	}
//	
//	@Test
//	public void testLogin() {
//		when(bookingDao.searchReservation( any(String.class), any(String.class) )).thenReturn(bookingData);
//		when(bookingDao.findById( any(Long.class))).thenReturn(bookingData);
//		
//		String result = bookingLoginController.login();
//		
//		assertTrue(result.contains("show-booking"));
//		assertEquals(bookingData.getId(), reservationSession.getId());
//		assertTrue(reservationSession.isLoggedIn());
//	}
//	
//	@Test
//	public void testLoginError() {
//		when(bookingDao.searchReservation( any(String.class), any(String.class) )).thenReturn(null);
//		
//		assertTrue(bookingLoginController.login().contains("mybooking-login"));
//	}
//	
//	@Test
//	public void testLogout() {
//		when(bookingDao.searchReservation( any(String.class), any(String.class) )).thenReturn(bookingData);
//		when(bookingDao.findById( any(Long.class))).thenReturn(bookingData);
//		
//		bookingLoginController.login();
//		String result = bookingLoginController.logout();
//		
//		assertTrue(result.contains("index"));
//		assertNull(reservationSession.getId());
//		assertFalse(reservationSession.isLoggedIn());
//	}
//}