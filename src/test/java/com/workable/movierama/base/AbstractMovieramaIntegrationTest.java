package com.workable.movierama.base;

import com.workable.movierama.MovieRamaApplication;
import com.workable.movierama.utils.EhCacheUtils;
import net.sf.ehcache.Ehcache;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test application context is defined here as well as setup and tear down
 * methods to be ran before and after each one of our tests. Initialization
 * methods concentrate mainly on the eviction of all cache elements so as
 * for the test suites to start anew.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MovieRamaApplication.class },
		webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class AbstractMovieramaIntegrationTest {

	@Before
	public void setup() throws Exception {
		clearCache();
	}

	@After
	public void tearDown() {
		clearCache();
	}

	public void clearCache() {
		EhCacheUtils.clearCache();}

}
