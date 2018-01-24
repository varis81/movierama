package com.workable.movierama.service;

import com.workable.movierama.model.Movie;

import java.util.List;

/**
 *  Basic MovieRamaService Interface that handles interface with the db (ehcache here)
 */
public interface MovieRamaService {

    public Movie getMovie(String title);

    public void addMovie(Movie movie);

    public List<Movie> listAllMovies();

    public void voteMovie(String vote, String title, String username);

    public String getMovieSubmitterFromTitle(String title);

    public List<String> getMoviesLikedByUser(String username);

    public List<String> getMoviesHatedByUser(String username);

    public void retractVote(String vote, String title, String username);
}
