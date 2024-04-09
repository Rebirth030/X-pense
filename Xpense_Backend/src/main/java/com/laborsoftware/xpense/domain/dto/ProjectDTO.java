package com.laborsoftware.xpense.domain.dto;

import java.time.ZonedDateTime;

public class ProjectDTO {
    private Long id;

    private String name;

    private String description;

    private ZonedDateTime releaseDate;

    private Double expectedExpense;


    private Double currentExpense;

    // private Set<ExpenseDTO> expenses;

    private CompanyDTO company;

    private UserDTO user;

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

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
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

    /*
    public Set<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }

     */

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
