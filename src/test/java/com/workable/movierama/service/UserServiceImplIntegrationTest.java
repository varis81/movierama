package com.workable.movierama.service;

import com.workable.movierama.base.AbstractMovieramaIntegrationTest;
import com.workable.movierama.exceptions.UserExistsException;
import com.workable.movierama.model.Movie;
import com.workable.movierama.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by aris on 24/1/2018.
 */
public class UserServiceImplIntegrationTest extends AbstractMovieramaIntegrationTest {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private UserService userService;

	private Cache userCache;

	@Before
	public void setup() throws Exception {
		super.setup();
		MockitoAnnotations.initMocks(getClass());
		userCache = cacheManager.getCache("userCache");
	}

	@Test
	public void testGetUser_UserPresent() {
		String username = "username";
		User userToAdd = new User(username, "pass");
		userCache.put(username, userToAdd);
		User user = userService.getUser(username);
		assertEquals(user, userToAdd);
	}

	@Test
	public void testGetUser_userNotPresent() {
		String username = "username";
		User user = userService.getUser(username);
		assertEquals(null, user);
	}

	@Test
	public void testAddUser_newUser() throws UserExistsException {
		String username = "username";
		User userToAdd = new User(username, "pass");
		User user = userService.saveUser(userToAdd);
		assertEquals(user, userToAdd);
	}

	@Test(expected = UserExistsException.class)
	public void testAddUser_userExists() throws UserExistsException {
		String username = "username";
		User userToAdd = new User(username, "pass");
		userService.saveUser(userToAdd);
		User user = userService.saveUser(userToAdd);
	}
}
