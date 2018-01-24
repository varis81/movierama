package com.workable.movierama.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Movie implements Serializable {

    private static final long serialVersionUID = -5421447596875421369L;

    private String title;
    private String description;
    private String nameOfTheUser;
    private Date dateOfpPublication;
    private Integer numberOfLikes = 0;
    private Integer numberOfHates = 0;

    public Movie() {}

    public Movie(String title, String description, String nameOfTheUser, Date dateOfpPublication, Integer numberOfLikes, Integer numberOfHates) {
        this.title = title;
        this.description = description;
        this.nameOfTheUser = nameOfTheUser;
        this.dateOfpPublication = dateOfpPublication;
        this.numberOfLikes = numberOfLikes;
        this.numberOfHates = numberOfHates;
    }

    public void incrementLikes(){
        if (this.numberOfLikes == null) {
            this.numberOfLikes = 1;
        } else {
            this.numberOfLikes++;
        }
    }

    public void incrementHates(){
        if (this.numberOfHates == null) {
            this.numberOfHates = 1;
        } else {
            this.numberOfHates++;
        }
    }

    public void decrementLikes(){
        if (this.numberOfLikes == null) {
            this.numberOfLikes = 0;
        } else {
            this.numberOfLikes--;
        }
    }

    public void decrementHates(){
        if (this.numberOfHates == null) {
            this.numberOfHates = 0;
        } else {
            this.numberOfHates--;
        }
    }
}
