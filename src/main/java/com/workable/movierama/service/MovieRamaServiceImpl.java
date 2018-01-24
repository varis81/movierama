package com.workable.movierama.service;

import com.workable.movierama.model.Movie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MovieRamaServiceImpl implements MovieRamaService {

    private static final Log log = LogFactory.getLog(MovieRamaServiceImpl.class);

    private static final String TITLES = "titles";

    private static final String HATEVOTES = "hate_votes_map";

    private static final String LIKEVOTES = "like_votes_map";

    @Autowired
    private CacheManager cacheManager;

    private Cache moviesCache;

    @PostConstruct
    private void init() {
        moviesCache = cacheManager.getCache("movieCache");
        moviesCache.put(TITLES, new ArrayList<String>());
        moviesCache.put(HATEVOTES, new HashMap<String, List<String>>());
        moviesCache.put(LIKEVOTES, new HashMap<String, List<String>>());
    }

    public Movie getMovie(String title) {
        if (moviesCache.get(title) != null) {
            return (Movie) moviesCache.get(title).get();
        }
        return null;
    }

    public void addMovie(Movie movie) {
        if (moviesCache.get(movie.getTitle()) != null) {
            log.info("Movie: " + movie.getTitle() + " exists. Will be updated");
            moviesCache.evict(movie.getTitle());
        }
        moviesCache.put(movie.getTitle(), movie);

        List<String> titles = (List<String>) moviesCache.get(TITLES).get();
        if (!titles.stream().anyMatch(t -> t.equals(movie.getTitle()))){
            titles.add(movie.getTitle());
            moviesCache.put(TITLES, titles);
        }
    }

    public List<Movie> listAllMovies() {
        List<String> titles = (List<String>) moviesCache.get(TITLES).get();
        List<Movie> movies = new ArrayList<>();
        for (String title : titles) {
            movies.add( (Movie)moviesCache.get(title).get());
        }
        return movies;
    }

    public void voteMovie(String vote, String title, String username) {
        Movie movie = getMovie(title);
        if (vote.equals("like")) {
            movie.incrementLikes();
        } else {
            movie.incrementHates();
        }
        addMovie(movie);

        // register the vote
        String cacheKey = (vote.equals("like")) ? LIKEVOTES : HATEVOTES;

        Map<String, List<String>> votes = (HashMap<String, List<String>>) moviesCache.get(cacheKey).get();
        if (!votes.containsKey(username)) {
            votes.put(username, new ArrayList<String>() {{add(title);}});
        } else if (!votes.get(username).stream().anyMatch(t -> t.equals(title))){
            votes.get(username).add(title);
        }
        moviesCache.put(cacheKey, votes);
    }

    public void retractVote(String vote, String title, String username) {
        Movie movie = getMovie(title);
        if (vote.equals("like")) {
            movie.decrementLikes();
        } else {
            movie.decrementHates();
        }
        addMovie(movie);

        // register the unvoting
        String cacheKey = (vote.equals("like")) ? LIKEVOTES : HATEVOTES;

        Map<String, List<String>> votes = (HashMap<String, List<String>>) moviesCache.get(cacheKey).get();
        if (votes.containsKey(username)) {
            List<String> movieTitles = votes.get(username).stream().filter(t -> !t.equals(title)).collect(Collectors.toList());
            votes.put(username,movieTitles);
        }
        moviesCache.put(cacheKey, votes);
    }

    public String getMovieSubmitterFromTitle(String title) {
        Movie movie = getMovie(title);
        if (movie != null) {
            return movie.getNameOfTheUser();
        }
        return "";
    }

    public List<String> getMoviesLikedByUser(String username) {
        Map<String, List<String>> votes = (HashMap<String, List<String>>) moviesCache.get(LIKEVOTES).get();
        if (votes.containsKey(username)) {
            return votes.get(username);
        }
        return new ArrayList<>();
    }

    public List<String> getMoviesHatedByUser(String username) {
        Map<String, List<String>> votes = (HashMap<String, List<String>>) moviesCache.get(HATEVOTES).get();
        if (votes.containsKey(username)) {
            return votes.get(username);
        }
        return new ArrayList<>();
    }
}
