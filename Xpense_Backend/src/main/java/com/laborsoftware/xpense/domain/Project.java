package com.laborsoftware.xpense.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    // just date ?

    private ZonedDateTime releaseDate;


    private Double expectedExpense;


    private Double currentExpense;

    @ManyToOne()
    private ApplicationUser applicationUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ZonedDateTime release) {
        this.releaseDate = release;
    }

    public Double getExpectedExpense() {
        return expectedExpense;
    }

    public void setExpectedExpense(Double expectedExpense) {
        this.expectedExpense = expectedExpense;
    }

    public Double getCurrentExpense() {
        return currentExpense;
    }

    public void setCurrentExpense(Double currentExpense) {
        this.currentExpense = currentExpense;
    }

    public ApplicationUser getUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
