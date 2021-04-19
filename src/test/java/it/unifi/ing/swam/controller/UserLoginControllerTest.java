/*
 * package it.unifi.ing.swam.controller;
 * 
 * import static org.junit.Assert.assertEquals; import static
 * org.junit.Assert.assertFalse; import static org.junit.Assert.assertNull;
 * import static org.junit.Assert.assertTrue; import static
 * org.mockito.Matchers.any; import static org.mockito.Mockito.mock; import
 * static org.mockito.Mockito.when;
 * 
 * import it.unifi.ing.swam.model.User; import
 * org.apache.commons.lang3.reflect.FieldUtils; import org.junit.Before; import
 * org.junit.Test; import org.junit.runners.model.InitializationError;
 * 
 * import it.unifi.ing.swam.components.LoggedUserComponent; import
 * it.unifi.ing.swam.dao.UserDao; import it.unifi.ing.swam.model.ModelFactory;
 * import it.unifi.ing.swam.util.Util;
 * 
 * public class UserLoginControllerTest {
 * 
 * private LoginController loginController;
 * 
 * private LoggedUserComponent administratorSession;
 * 
 * private UserDao userDao;
 * 
 * private User user;
 * 
 * 
 * @Before public void setUp() throws InitializationError { loginController =
 * new LoginController();
 * 
 * userDao = mock(UserDao.class);
 * 
 * administratorSession = new LoggedUserComponent();
 * 
 * user = ModelFactory.user(); user.setUsername("admin");
 * user.setPassword(Util.digest("examplePassword"));
 * 
 * try { FieldUtils.writeField(user, "id", Long.valueOf(10), true);
 * FieldUtils.writeField(loginController, "administratorDao", userDao, true);
 * FieldUtils.writeField(loginController, "administratorSession",
 * administratorSession, true); FieldUtils.writeField(administratorSession,
 * "administratorDao", userDao, true); FieldUtils.writeField(loginController,
 * "administratorData", user, true); } catch (IllegalAccessException e) { throw
 * new InitializationError(e); } }
 * 
 * @Test public void testLogin() { when(userDao.login( any(User.class)
 * )).thenReturn(user); when(userDao.findById( any(Long.class)
 * )).thenReturn(user);
 * 
 * String result = loginController.login();
 * 
 * assertTrue(result.contains("add-flights")); assertEquals(user.getId(),
 * administratorSession.getUserID());
 * assertTrue(administratorSession.isLoggedIn()); }
 * 
 * @Test public void testLoginError() { when(userDao.login( any(User.class)
 * )).thenReturn(null);
 * 
 * assertTrue(loginController.login().contains("user-login")); }
 * 
 * @Test public void testLogout() { when(userDao.login( any(User.class)
 * )).thenReturn(user); when(userDao.findById( any(Long.class)
 * )).thenReturn(user);
 * 
 * loginController.login(); String result = loginController.logout();
 * 
 * assertTrue(result.contains("index"));
 * assertNull(administratorSession.getUserID());
 * assertFalse(administratorSession.isLoggedIn()); } }
 */