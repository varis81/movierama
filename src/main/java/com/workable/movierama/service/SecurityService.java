package com.workable.movierama.service;

import com.workable.movierama.exceptions.UserExistsException;
import com.workable.movierama.model.User;

/**
 *  Basic MovieRamaService Interface that handles interface with the db (ehcache here)
 */
public interface SecurityService {

    String findLoggedInUsername();

    void autologin(String username, String password);
}
