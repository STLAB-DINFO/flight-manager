//package it.unifi.ing.swam.controller;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//import it.unifi.ing.swam.controller.booking.VisitorBookingController;
//import it.unifi.ing.swam.dao.BookingDao;
//import it.unifi.ing.swam.model.*;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runners.model.InitializationError;
//
//import it.unifi.ing.swam.dao.FlightDao;
//import it.unifi.ing.swam.model.Booking;
//import it.unifi.ing.swam.model.temp.TemporaryReservationSeats;
//
//public class VisitorBookingControllerTest {
//	
//	private VisitorBookingController visitorBookingController;
//	
//	private BookingDao bookingDao;
//	
//	private FlightDao flightDao;
//	
//	private TemporaryReservationSeatsDao temporaryReservationSeatsDao;
//	
//	private Booking booking;
//	
//	private TemporaryReservationSeats trsOut;
//	
//	private TemporaryReservationSeats trsBack;
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
//		visitorBookingController = new VisitorBookingController();
//		
//		bookingDao = mock(BookingDao.class);
//		
//		flightDao = mock(FlightDao.class);
//		
//		temporaryReservationSeatsDao = mock(TemporaryReservationSeatsDao.class);
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
//		trsOut = ModelFactory.temporaryReservationSeats();
//		trsOut.setDate( new Date() );
//		trsOut.setFlight(flightOut);
//		trsOut.setnPassengers(1);
//		
//		trsBack = ModelFactory.temporaryReservationSeats();
//		trsBack.setDate( new Date() );
//		trsBack.setFlight(flightBack);
//		trsBack.setnPassengers(1);
//		
//		try {
//			
//			FieldUtils.writeField(visitorBookingController, "bookingDao", bookingDao, true);
//			FieldUtils.writeField(visitorBookingController, "flightDao", flightDao, true);
//			FieldUtils.writeField(visitorBookingController, "temporaryReservationSeatsDao", temporaryReservationSeatsDao, true);
//			FieldUtils.writeField(visitorBookingController, "flightOut", Long.valueOf(10), true);
//			FieldUtils.writeField(visitorBookingController, "flightBack", Long.valueOf(20), true);
//			FieldUtils.writeField(visitorBookingController, "nPassengers", 1, true);
//			FieldUtils.writeField(booking, "id", Long.valueOf(100), true);
//			FieldUtils.writeField(visitorBookingController, "booking", booking, true);
//			FieldUtils.writeField(visitorBookingController, "temporaryReservationSeatsList", Arrays.asList(Long.valueOf(500), Long.valueOf(600)), true);
//			FieldUtils.writeField(trsOut, "id", Long.valueOf(500), true);
//			FieldUtils.writeField(trsBack, "id", Long.valueOf(600), true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//	}
//	
//	@Test
//	public void testGetReservation() throws IllegalAccessException {
//		FieldUtils.writeField(visitorBookingController, "booking", null, true);
//
//		when(flightDao.findById(Long.valueOf(10))).thenReturn(flightOut);
//		when(flightDao.findById(Long.valueOf(20))).thenReturn(flightBack);
//		
//		Booking result = visitorBookingController.getBooking();
//		
//		assertTrue(result instanceof Booking);
//		assertNull(result.getEmail());
//		assertEquals(2, result.getFlights().size());
//		
//		FieldUtils.writeField(visitorBookingController, "booking", booking, true);
//	}
//	
//	@Test
//	public void testGetFlights() {
//		when(flightDao.findById(Long.valueOf(10))).thenReturn(flightOut);
//		when(flightDao.findById(Long.valueOf(20))).thenReturn(flightBack);
//		
//		List<Flight> result = visitorBookingController.getFlights();
//		
//		assertEquals(2, result.size());
//		assertEquals(Arrays.asList(flightOut, flightBack), result);
//	}
//	
//	@Test
//	public void testGetForwardFlight() {
//		when(flightDao.findById(Long.valueOf(10))).thenReturn(flightOut);
//		
//		assertEquals(flightOut, visitorBookingController.getForwardFlight());
//	}
//	
//	@Test
//	public void testGetReturnFlight() {
//		when(flightDao.findById(Long.valueOf(20))).thenReturn(flightBack);
//		
//		assertEquals(flightBack, visitorBookingController.getReturnFlight());
//	}
//	
//
//	@Test
//	public void testUpdateReservedSeats() {
//		visitorBookingController.updateReservedSeats();
//		
//		assertEquals(1, visitorBookingController.getBooking().getFlights().get(0).getReservedSeats());
//		assertEquals(1, visitorBookingController.getBooking().getFlights().get(1).getReservedSeats());
//		
//		verify(flightDao, times(1)).save(flightOut);
//		verify(flightDao, times(1)).save(flightBack);
//	}
//
//	@Test
//	public void testCleanTemporaryReservations() {
//		visitorBookingController.cleanTemporaryReservations();
//		
//		verify(temporaryReservationSeatsDao, times(1)).delete(Long.valueOf(500));
//		verify(temporaryReservationSeatsDao, times(1)).delete(Long.valueOf(600));
//		
//		//verify(temporaryReservationSeatsDao, times(1)).cleanExpiredTemporaryReservation(10);
//	}
//	
//}
