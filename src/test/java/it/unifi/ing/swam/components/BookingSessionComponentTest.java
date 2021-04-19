package it.unifi.ing.swam.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.unifi.ing.swam.dao.BookingDao;
import it.unifi.ing.swam.model.Booking;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import it.unifi.ing.swam.model.ModelFactory;

public class BookingSessionComponentTest {
	
	private BookingSessionComponent bookingSessionComponent;
	
	private BookingDao bookingDao;
	
	private Booking booking;
	
	@Before
	public void setUp() throws InitializationError {
		
		bookingSessionComponent = new BookingSessionComponent();
		
		bookingDao = mock(BookingDao.class);
		
		booking = ModelFactory.reservation();
		
		try {
			FieldUtils.writeField(bookingSessionComponent, "bookingDao", bookingDao, true);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
	}
	
	@Test
	public void testIsLoggedIn() {
		assertFalse(bookingSessionComponent.isLoggedIn());
		
		when(bookingDao.findById(Long.valueOf(10))).thenReturn(booking);
		bookingSessionComponent.setId(Long.valueOf(10));
		assertTrue(bookingSessionComponent.isLoggedIn());
	}

}
