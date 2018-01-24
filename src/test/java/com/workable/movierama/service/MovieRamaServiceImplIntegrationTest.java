package com.workable.movierama.service;

import com.workable.movierama.base.AbstractMovieramaIntegrationTest;
import com.workable.movierama.model.Movie;
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
public class MovieRamaServiceImplIntegrationTest extends AbstractMovieramaIntegrationTest {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private MovieRamaService movieRamaService;

	private Cache moviesCache;

	@Before
	public void setup() throws Exception {
		super.setup();
		MockitoAnnotations.initMocks(getClass());
		moviesCache = cacheManager.getCache("movieCache");
		moviesCache.put("titles", new ArrayList<String>());
	}

	@Test
	public void testGetMovie_moviePresent() {
		String title = "testTitle";
		Movie movieToAdd = new Movie(title, "description", "User", new Date(), 1, 1);
		moviesCache.put(title, movieToAdd);
		Movie movie = movieRamaService.getMovie(title);
		assertEquals(movie, movieToAdd);
	}

	@Test
	public void testGetMovie_movieNotPresent() {
		String title = "otherTitle";
		Movie movie = movieRamaService.getMovie(title);
		assertEquals(null, movie);
	}

	@Test
	public void testAddMovie_newMovie() {
		String title = "testTitle";
		Movie movieToAdd = new Movie(title, "description", "User", new Date(), 1, 1);
		movieRamaService.addMovie(movieToAdd);
		Movie movie = movieRamaService.getMovie(title);
		assertEquals(movieToAdd, movie);
	}

	@Test
	public void testGetMovie_movieExists() {
		String title = "testTitle";
		Movie movieToAdd = new Movie(title, "description", "User", new Date(), 1, 1);
		moviesCache.put(title, movieToAdd);
		movieToAdd.setDescription("newDescription");
		movieRamaService.addMovie(movieToAdd);
		Movie movie = movieRamaService.getMovie(title);
		assertEquals("newDescription", movie.getDescription());
	}

	@Test
	public void listAllMovies_noMovies() {
		List<Movie> movies = movieRamaService.listAllMovies();
		assertEquals(0, movies.size());
	}

	@Test
	public void listAllMovies_MoviesPresent() {
		String title = "testTitle";
		Movie movieToAdd = new Movie(title, "description", "User", new Date(), 1, 1);
		movieRamaService.addMovie(movieToAdd);

		title = "testTitle2";
		Movie movieToAdd1 = new Movie(title, "description", "User", new Date(), 1, 1);
		movieRamaService.addMovie(movieToAdd1);

		List<Movie> movies = movieRamaService.listAllMovies();
		assertEquals(2, movies.size());
	}
}
