package com.laborsoftware.xpense.domain.dto;

import java.util.Set;

public class UserTimecardDTO {
    private Long id;

    private Double weeklyWorkingHours;

    private Double balance;

    private Double userBalance;

    // private Set<WeeklyTimecardDTO> weeklyTimecards;

    // private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeeklyWorkingHours() {
        return weeklyWorkingHours;
    }

    public void setWeeklyWorkingHours(Double weeklyWorkingHours) {
        this.weeklyWorkingHours = weeklyWorkingHours;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }
/*
    public Set<WeeklyTimecardDTO> getWeeklyTimecards() {
        return weeklyTimecards;
    }

    public void setWeeklyTimecards(Set<WeeklyTimecardDTO> weeklyTimecards) {
        this.weeklyTimecards = weeklyTimecards;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

     */
}
