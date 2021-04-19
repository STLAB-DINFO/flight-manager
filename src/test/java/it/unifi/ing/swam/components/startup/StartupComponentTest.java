//package it.unifi.ing.swam.components.startup;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//import it.unifi.ing.swam.model.User;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runners.model.InitializationError;
//
//import it.unifi.ing.swam.dao.UserDao;
//import it.unifi.ing.swam.model.ModelFactory;
//
//public class StartupComponentTest {
//	
//	private StartupComponent startupComponent;
//	
//	private UserDao userDao;
//	
//	@Before
//	public void setUp() throws InitializationError {
//		startupComponent = new StartupComponent();
//		
//		userDao = mock(UserDao.class);
//		
//		try {
//			FieldUtils.writeField(startupComponent, "administratorDao", userDao, true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//	}
//	
//	@Test
//	public void testInit() throws Exception {
//		User admin = ModelFactory.user();
//		admin.setUsername("admin");
//		admin.setPassword("password");
//		
//		startupComponent.init();
//		
//		verify(userDao, times(1)).existsAdministrator();
//	}
//
//}
