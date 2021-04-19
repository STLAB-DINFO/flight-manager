//package it.unifi.ing.swam.dao;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import it.unifi.ing.swam.model.User;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.junit.Test;
//import org.junit.runners.model.InitializationError;
//
//import it.unifi.ing.swam.model.ModelFactory;
//
//public class UserDaoTest extends JpaTest {
//	
//	private UserDao userDao;
//	private User user;
//	
//	@Override
//	protected void init() throws InitializationError {
//		user = ModelFactory.user();
//		user.setUsername("admin");
//		user.setPassword("password");
//		
//		entityManager.persist(user);
//		
//		userDao = new UserDao();
//		
//		try {
//			FieldUtils.writeField(userDao, "entityManager", entityManager, true);
//		} catch (IllegalAccessException e) {
//			throw new InitializationError(e);
//		}
//	}
//	
//	@Test
//	public void testSave() {
//		User anotherAdmin = ModelFactory.user();
//		anotherAdmin.setUsername("admin2");
//		anotherAdmin.setPassword("examplepassword2");
//		
//		userDao.save(anotherAdmin);
//		
//		assertEquals(anotherAdmin, entityManager
//				.createQuery("from Administrator where uuid = :uuid", User.class)
//				.setParameter("uuid", anotherAdmin.getUuid())
//				.getSingleResult());
//	}
//	
//	@Test
//	public void testFindById() {
//		User result = userDao.findById(user.getId());
//		
//		assertEquals(user, result);
//	}
//	
//	@Test
//	public void testFindByWrongId() {
//		User result = userDao.findById(new Long(9999));
//		
//		assertNull(result);
//	}
//	
//	@Test
//	public void testDelete() {
//		userDao.delete(user);
//		
//		assertNull(userDao.findById(user.getId()));
//	}
//	
//	@Test
//	public void testExistsAdministrator() {
//		assertTrue(userDao.existsAdministrator());
//		
//		userDao.delete(user);
//		
//		assertFalse(userDao.existsAdministrator());
//	}
//	
//	@Test
//	public void testLogin() {
//		User credentials = ModelFactory.user();
//		credentials.setUsername("admin");
//		credentials.setPassword("password");
//		
//		User result = userDao.login(credentials);
//		
//		assertEquals(user, result);
//	}
//	
//	@Test
//	public void testWrongLogin() {
//		User credentials = ModelFactory.user();
//		credentials.setUsername("admin2");
//		credentials.setPassword("examplewrongpassword");
//		
//		User result = userDao.login(credentials);
//		
//		assertNull(result);
//	}
//}
