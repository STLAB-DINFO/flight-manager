//package it.unifi.ing.swam.controller;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.Date;
//
//import it.unifi.ing.swam.dao.BookingDao;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runners.model.InitializationError;
//
//import it.unifi.ing.swam.components.BookingSessionComponent;
//import it.unifi.ing.swam.dao.FlightDao;
//import it.unifi.ing.swam.model.Airport;
//import it.unifi.ing.swam.model.Flight;
//import it.unifi.ing.swam.model.ModelFactory;
//import it.unifi.ing.swam.model.Passenger;
//import it.unifi.ing.swam.model.Booking;
//
//public class ManageVisitorBookingControllerTest {
//	
//	private EditBookingController editBookingController;
//	
//	private BookingDao bookingDao;
//	
//	private FlightDao flightDao;
//	
//	private BookingSessionComponent reservationSession;
//	
//	private Booking booking;
//	
//	private Passenger passenger;
//	
//	private Flight flightOut;
//	
//	private Flight flightBack;
//	
//	private Airport sourceAirport;
//	
//	private Airport destinationAirport;
//	
//	@Before
//	public void setUp() throws InitializationError {
//		editBookingController = new EditBookingController();
//		
//		bookingDao = mock(BookingDao.class);
//		
//		flightDao = mock(FlightDao.class);
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
//		booking = ModelFactory.reservation();
//		booking.setDate( new Date() );
//		booking.setEmail("mail@example.com");
//		booking.setFlights(Arrays.asList(flightOut, flightBack));
//		booking.setPassengers(Arrays.asList(passenger));
//		booking.setPrice(160);
//		
//		reservationSession.setBooking(booking);
//		
//		try {
//			FieldUtils.writeField(flightOut, "id", Long.valueOf(10), true);
//			FieldUtils.writeField(flightBack, "id", Long.valueOf(20), true);
//			FieldUtils.writeField(editBookingController, "bookingDao", bookingDao, true);
//			FieldUtils.writeField(editBookingController, "flightDao", flightDao, true);
//			FieldUtils.writeField(editBookingController, "booking", booking, true);
//			FieldUtils.writeField(reservationSession, "bookingDao", bookingDao, true);
//			FieldUtils.writeField(editBookingController, "reservationSession", reservationSession, true);
//			FieldUtils.writeField(editBookingController, "passengers", Arrays.asList(passenger), true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//	}
//	
//	@Test
//	public void testGetReservation() {
//		when(bookingDao.findById(Long.valueOf(10))).thenReturn(booking);
//		
//		reservationSession.setId(Long.valueOf(10));
//		
//		assertEquals(booking, editBookingController.getBooking());
//	}
//	
//	@Test(expected=IllegalArgumentException.class)
//	public void testGetReservationBadIdFormat() throws InitializationError {
//		try {
//			FieldUtils.writeField(reservationSession, "id", "hello", true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//		
//		editBookingController.getBooking();
//	}
//	
//	@Test
//	public void testUpdatePassengers() {
//		String result = editBookingController.updatePassengers();
//		
//		assertTrue(result.contains("update-passengers-success"));
//		assertTrue(editBookingController.isJustUpdatePassengers());
//		
//		verify(bookingDao, times(1)).save(booking);
//	}
//	
//	@Test
//	public void testDeleteReservation() {
//		when(flightDao.findById(Long.valueOf(10))).thenReturn(flightOut);
//		when(flightDao.findById(Long.valueOf(20))).thenReturn(flightBack);
//		
//		assertTrue(editBookingController.deleteReservation().contains("delete-booking-success"));
//		
//		verify(flightDao, times(1)).save(flightOut);
//		verify(flightDao, times(1)).save(flightBack);
//		verify(bookingDao, times(1)).delete(booking);
//		
//	}
//}
