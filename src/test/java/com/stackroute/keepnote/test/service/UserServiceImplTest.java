package com.stackroute.keepnote.test.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserServiceImpl;

public class UserServiceImplTest {

	@Mock private UserDAO userDAO;
	@InjectMocks UserServiceImpl userServiceImpl;
     
	private User user;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		user = new User("Jhon123", "Jhon Simon", "123456", "9872367384", new Date());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRegisterUserSuccess() throws UserAlreadyExistException {
		when(userDAO.getUserById(user.getUserId())).thenReturn(null);
		when(userDAO.registerUser(user)).thenReturn(true);
		boolean status = userServiceImpl.registerUser(user);
		assertEquals(true, status);
		verify(userDAO, times(1)).registerUser(user);
	}

	@Test(expected = UserAlreadyExistException.class)
	public void testRegisterUserFailure() throws UserAlreadyExistException {
		when(userDAO.getUserById(user.getUserId())).thenReturn(user);
		when(userDAO.registerUser(user)).thenReturn(false);
		boolean status = userServiceImpl.registerUser(user);
		assertEquals(false, status);
		verify(userDAO, times(1)).getUserById(user.getUserId());
	}

	@Test
	public void testUpdateUserSuccess() throws Exception {

		when(userDAO.getUserById("Jhon123")).thenReturn(user);
		user.setUserMobile("777777777");
		user.setUserAddedDate(new Date());
		when(userDAO.updateUser(user)).thenReturn(true);
		System.out.println("testUpdateUserSuccess update call START");
		User updatedUser = userServiceImpl.updateUser(user, "Jhon123");
		System.out.println("testUpdateUserSuccess update call END");
		assertEquals("777777777", updatedUser.getUserMobile());
		verify(userDAO, times(1)).getUserById(user.getUserId());
		verify(userDAO, times(1)).updateUser(user);
	}

	@Test(expected = Exception.class)
	public void testUpdateUserFailure() throws Exception {

		when(userDAO.getUserById("Jhon123")).thenReturn(null);
		when(userDAO.updateUser(user)).thenReturn(true);
		System.out.println("before unused annotation");
		@SuppressWarnings("unused")
		User updatedUser = userServiceImpl.updateUser(user, "Jhon123");
		System.out.println("updatedUser"+updatedUser);
	}

	@Test
	public void testGetUserByIdSuccess() throws UserNotFoundException {

		when(userDAO.getUserById("Jhon123")).thenReturn(user);
		User userDetail = userServiceImpl.getUserById("Jhon123");
		assertEquals(user, userDetail);
		assertEquals(user.getUserId(), userDetail.getUserId());
		verify(userDAO, times(1)).getUserById("Jhon123");

	}

	@Test(expected = UserNotFoundException.class)
	public void testGetUserByIdFailure() throws UserNotFoundException {

		when(userDAO.getUserById("Jhon123")).thenReturn(null);
		User userDetail = userServiceImpl.getUserById("Jhon123");
		assertEquals(user, userDetail);
		assertEquals(user.getUserId(), userDetail.getUserId());
		verify(userDAO, times(1)).getUserById("Jhon123");
	}

	@Test
	public void testValidateUserSuccess() throws UserNotFoundException {

		when(userDAO.validateUser("Jhon123", "123456")).thenReturn(true);
		boolean status = userServiceImpl.validateUser("Jhon123", "123456");
		System.out.println("status "+status); 
		assertEquals(true, status);
		verify(userDAO, times(1)).validateUser("Jhon123", "123456");
	}

	@Test(expected = UserNotFoundException.class)
	public void testValidateUserFailure() throws UserNotFoundException {
		System.out.println("before "); 
		when(userDAO.validateUser("Jhon123", "123456")).thenReturn(false);
		System.out.println("userDAO.validateUser status: "+userDAO.validateUser("Jhon123", "123456")); 
		@SuppressWarnings("unused")
		boolean status = userServiceImpl.validateUser("Jhon123", "123456");
		System.out.println("status: "+status );
	}

	@Test
	public void testDeleteUserSuccess() {

		when(userDAO.deleteUser("Jhon123")).thenReturn(true);
		boolean status = userServiceImpl.deleteUser("Jhon123");
		assertEquals(true, status);
		verify(userDAO, times(1)).deleteUser("Jhon123");
	}

	@Test
	public void testDeleteUserFailure() {
		when(userDAO.deleteUser("Jhon123")).thenReturn(false);
		boolean status = userServiceImpl.deleteUser("Jhon123");
		assertEquals(false, status);
		verify(userDAO, times(1)).deleteUser("Jhon123");
	}

}
