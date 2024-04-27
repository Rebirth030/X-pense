package com.laborsoftware.xpense.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "weekly_timecard")
public class WeeklyTimecard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    private Integer calendarWeek;

    private Double weeklyWorkingHours;
    private Double weeklyBalance;

    @ManyToOne()
    private ApplicationUser applicationUser;

    @ManyToOne()
    private UserTimecard userTimecard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getCalendarWeek() {
        return calendarWeek;
    }

    public void setCalendarWeek(Integer calendarWeek) {
        this.calendarWeek = calendarWeek;
    }

    public Double getWeeklyWorkingHours() {
        return weeklyWorkingHours;
    }

    public void setWeeklyWorkingHours(Double weeklyWorkingHours) {
        this.weeklyWorkingHours = weeklyWorkingHours;
    }

    public Double getWeeklyBalance() {
        return weeklyBalance;
    }

    public void setWeeklyBalance(Double weeklyBalance) {
        this.weeklyBalance = weeklyBalance;
    }

    /*
    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

     */

    public UserTimecard getUserTimecard() {
        return userTimecard;
    }

    public void setUserTimecard(UserTimecard userTimecard) {
        this.userTimecard = userTimecard;
    }

    public ApplicationUser getUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
