package it.unifi.ing.swam.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import it.unifi.ing.swam.dao.UserDao;
import it.unifi.ing.swam.model.ModelFactory;
import it.unifi.ing.swam.model.User;

public class UserSessionBeanTest {
	
	private LoggedUserComponent loggedUserComponent;
	
	private UserDao userDao;
	
	private User user;
	
	@Before
	public void setUp() throws InitializationError {
		
		loggedUserComponent = new LoggedUserComponent();
		
		userDao = mock(UserDao.class);
		
		user = ModelFactory.user();
		user.setUsername("user");
		user.setPassword("pass");
		
		try {
			FieldUtils.writeField(loggedUserComponent, "administratorDao", userDao, true);
		} catch (IllegalAccessException e) {
			throw new InitializationError(e);
		}
	}

	@Ignore
	@Test
	public void testIsLoggedIn() {
		assertFalse(loggedUserComponent.isLoggedIn());
		
		when(userDao.findById(Long.valueOf(10))).thenReturn(user);
//		loggedUserComponent.setUserID(Long.valueOf(10));
		assertTrue(loggedUserComponent.isLoggedIn());
	}
}
