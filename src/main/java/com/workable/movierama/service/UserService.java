package com.workable.movierama.service;

import com.workable.movierama.exceptions.UserExistsException;
import com.workable.movierama.model.Movie;
import com.workable.movierama.model.User;

import java.util.List;

/**
 *  Basic MovieRamaService Interface that handles interface with the db (ehcache here)
 */
public interface UserService {

    public User getUser(String username);

    public User saveUser(User user) throws UserExistsException;

}
