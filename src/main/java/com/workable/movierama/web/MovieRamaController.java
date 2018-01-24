package com.workable.movierama.web;

import com.workable.movierama.exceptions.UserExistsException;
import com.workable.movierama.model.Movie;
import com.workable.movierama.model.User;
import com.workable.movierama.service.MovieRamaService;
import com.workable.movierama.service.SecurityService;
import com.workable.movierama.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieRamaController {

    @Autowired
    private MovieRamaService movieRamaService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    @RequestMapping(value = {"", "/", "/homepage"})
    public String homepage(Model model) {
        List<Movie> movies = movieRamaService.listAllMovies();
        model.addAttribute("moviesToDisplay", movies);
        return "homepage-template";
    }

    @RequestMapping(value = {"/home"})
    public String home(Model model) {
        List<Movie> movies = movieRamaService.listAllMovies();
        assignHomeModelAttributes(model, getUsername(), false, movies, "empty", "", false);
        return "home";
    }

    @RequestMapping("/sort")
    public String sort(@RequestParam(value = "sortby", required = true) String type, Model model) {
        List<Movie> movies = sortMovies(type, null);
        model.addAttribute("moviesToDisplay", movies);
        return "homepage-template";
    }

    @RequestMapping("/sorta")
    public String sorta(@RequestParam(value = "sortby", required = true) String type, @RequestParam(value = "byuser", required = false) boolean filtered,
                        @RequestParam(value = "user", required = true) String username, Model model) {
        List<Movie> movies = sortMovies(type, filtered == true ? retrieveMoviesOfUser(username) : null);
        assignHomeModelAttributes(model, getUsername(), filtered, movies, type, username, false);
        return "home";
    }

    @RequestMapping("/vote")
    public String vote(@RequestParam(value = "v", required = true) String vote,
                    @RequestParam(value = "m", required = true) String movieTitle,
                    @RequestParam(value = "filtered", required = true) boolean filtered,
                    @RequestParam(value = "submitedBy", required = true) String userName,
                    @RequestParam(value = "sorted", required = true) String type,
                    Model model) {
        movieRamaService.voteMovie(vote, movieTitle, getUsername());
        List<Movie> movies;

        if (!type.equals("empty")) {
            movies = sortMovies(type, filtered == true ? retrieveMoviesOfUser(userName) : null);
        } else if (filtered){
            movies = retrieveMoviesOfUser(userName);
        } else {
            movies = movieRamaService.listAllMovies();
        }
        assignHomeModelAttributes(model, getUsername(), filtered, movies, type, movieRamaService.getMovieSubmitterFromTitle(movieTitle), false);
        return "home";
    }

    @RequestMapping("/retractvote")
    public String retractVote(@RequestParam(value = "v", required = true) String vote,
            @RequestParam(value = "m", required = true) String movieTitle,
            @RequestParam(value = "filtered", required = true) boolean filtered,
            @RequestParam(value = "submitedBy", required = true) String userName,
            @RequestParam(value = "sorted", required = true) String type,
            Model model) {
        movieRamaService.retractVote(vote, movieTitle, getUsername());
        List<Movie> movies;

        if (!type.equals("empty")) {
            movies = sortMovies(type, filtered == true ? retrieveMoviesOfUser(userName) : null);
        } else if (filtered){
            movies = retrieveMoviesOfUser(userName);
        } else {
            movies = movieRamaService.listAllMovies();
        }
        assignHomeModelAttributes(model, getUsername(), filtered, movies, type, movieRamaService.getMovieSubmitterFromTitle(movieTitle), false);
        return "home";
    }

    @RequestMapping("/retrieve")
    public String retrieve(@RequestParam(value = "user", required = true) String username, Model model) {
        model.addAttribute("moviesToDisplay", retrieveMoviesOfUser(username));
        return "homepage-template";
    }

    @RequestMapping("/retrievea")
    public String retrievea(@RequestParam(value = "user", required = true) String username, Model model) {
        assignHomeModelAttributes(model, getUsername(), true, retrieveMoviesOfUser(username), "empty", username, false);
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        if (isCurrentAuthenticationAnonymous()){
            return "login";
        }
        List<Movie> movies = movieRamaService.listAllMovies();
        assignHomeModelAttributes(model, getUsername(), false, movies, "empty", "", false);
        return "redirect:/home";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUser(
            @ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model,
            Errors errors) {

        User registered = new User();
        if (!result.hasErrors()) {
            try {
                registered = userService.saveUser(user);
            } catch (UserExistsException e) {
                result.addError(new ObjectError("user", e.getMessage()));
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "registration";
        }
        else {
            securityService.autologin(user.getUsername(), user.getPassword());
            List<Movie> movies = movieRamaService.listAllMovies();
            assignHomeModelAttributes(model, user.getUsername(), false, movies, "empty", "", false);
            return "redirect:/home";
        }
    }

    @RequestMapping(value = "/addmovie", method = RequestMethod.GET)
    public String addmovie(Model model) {
        return "addmovie";
    }

    @RequestMapping(value = "/addmovie", method = RequestMethod.POST)
    public String addmovieform(@RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "description", required = true) String description,
            Model model) {

        Movie movie = new Movie(title, description, getUsername(), new Date(), 0, 0);
        movieRamaService.addMovie(movie);

        List<Movie> movies = movieRamaService.listAllMovies();
        assignHomeModelAttributes(model, getUsername(), false, movies, "empty", "", true);
        return "home";
    }

    /**
     * This method returns true if users is already authenticated [logged-in], else false.
     */
    private boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    private String getUsername(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ((org.springframework.security.core.userdetails.User) principal).getUsername();
    }

    private List<Movie> retrieveMoviesOfUser(String username) {
        if (username == null) {
            return null;
        }

        List<Movie> movies = movieRamaService.listAllMovies();
        movies = movies.stream()
                .filter(movie -> movie.getNameOfTheUser().equals(username))
                .collect(Collectors.toList());
        return movies;
    }

    private void assignHomeModelAttributes(Model model, String username, boolean filtered, List<Movie> movies, String type, String retrievedUser, boolean movieAdded) {
        model.addAttribute("moviesToDisplay", movies);
        model.addAttribute("username", username);
        model.addAttribute("filtered", filtered);
        model.addAttribute("sorted", type);
        model.addAttribute("retrievedUser", retrievedUser);

        model.addAttribute("moviesLiked", movieRamaService.getMoviesLikedByUser(username));
        model.addAttribute("moviesHated", movieRamaService.getMoviesHatedByUser(username));

        model.addAttribute("movieAdded", movieAdded);
    }

    private List<Movie> sortMovies(String type, List<Movie> movies) {
        movies = (movies == null) ? movieRamaService.listAllMovies() : movies;

        if (type.equals("date")) {
            movies = movies.stream().sorted((m1, m2)->m2.getDateOfpPublication().compareTo(m1.getDateOfpPublication())).
                    collect(Collectors.toList());
        } else if (type.equals("hate")) {
            movies = movies.stream().sorted((m1, m2)->m2.getNumberOfHates().compareTo(m1.getNumberOfHates())).
                    collect(Collectors.toList());
        } else {
            movies = movies.stream().sorted((m1, m2)->m2.getNumberOfLikes().compareTo(m1.getNumberOfLikes())).
                    collect(Collectors.toList());
        }

        return movies;
    }
}
