package com.workable.movierama.service;

import com.workable.movierama.exceptions.UserExistsException;
import com.workable.movierama.model.Movie;
import com.workable.movierama.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private static final Log log = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    private Cache userCache;

    @PostConstruct
    private void init() {
        userCache = cacheManager.getCache("userCache");
    }

    public User getUser(String username) {
        if (userCache.get(username) != null) {
            return (User) userCache.get(username).get();
        }
        return null;
    }

    public User saveUser(User user) throws UserExistsException{
        if (userCache.get(user.getUsername()) != null) {
            log.debug("Username: " + user.getUsername() + " already exists.");
            throw new UserExistsException("Username: " + user.getUsername() + " already exists." );
        }

        userCache.put(user.getUsername(), user);

        return user;
    }
}
