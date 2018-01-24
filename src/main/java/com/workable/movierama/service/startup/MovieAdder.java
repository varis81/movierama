package com.workable.movierama.service.startup;

import com.workable.movierama.exceptions.UserExistsException;
import com.workable.movierama.model.Movie;
import com.workable.movierama.model.User;
import com.workable.movierama.service.MovieRamaService;
import com.workable.movierama.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class MovieAdder {

    @Autowired
    private MovieRamaService movieRamaService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() throws UserExistsException{
        Movie movie = new Movie("The Empire Strikes Back", "After the rebels are overpowered by the Empire on the ice planet Hoth," +
                " Luke Skywalker begins Jedi training with Yoda. His friends accept shelter from a " +
                "questionable ally as Darth Vader hunts them in a plan to capture Luke. ", "Gandalf", new Date(), 111, 22);
        Movie movie1 = new Movie("Prometheus", " Following clues to the origin of mankind, a team finds a " +
                "structure on a distant moon, but they soon realize they are not alone. ", "Aris", new Date(), 0, 0);

        Movie movie2 = new Movie("Hostiles", " In 1892, a legendary Army captain reluctantly agrees to escort" +
                " a Cheyenne chief and his family through dangerous territory. ", "Scott", new Date(), 55, 1);

        Movie movie3 = new Movie("The Fighter", " A look at the early years of boxer Irish Micky Ward and his brother who helped train" +
                " him before going pro in the mid 1980s.  ", "Aris", new Date(), 4000, 0);

        Movie movie4 = new Movie("American Hustle", " A con man, Irving Rosenfeld, along with his seductive partner Sydney Prosser, " +
                "is forced to work for a wild F.B.I. Agent, Richie DiMaso, who pushes them into a world of Jersey powerbrokers and the Mafia.  ", "Tom", new Date(), 234, 554);

        Movie movie5 = new Movie("Silver Linings Playbook", " After a stint in a mental institution, former teacher Pat Solitano moves back in with his parents and tries to " +
                "reconcile with his ex-wife. Things get more challenging when Pat meets Tiffany, a mysterious girl with problems of her own.  ", "Vrasidas", new Date(), 552, 112);

        movieRamaService.addMovie(movie);
        movieRamaService.addMovie(movie1);
        movieRamaService.addMovie(movie2);
        movieRamaService.addMovie(movie3);
        movieRamaService.addMovie(movie4);
        movieRamaService.addMovie(movie5);

        userService.saveUser(new User("Gandalf", "123"));
        userService.saveUser(new User("Scott", "123"));
        userService.saveUser(new User("Tom", "123"));
        userService.saveUser(new User("Vrasidas", "123"));

    }
}
