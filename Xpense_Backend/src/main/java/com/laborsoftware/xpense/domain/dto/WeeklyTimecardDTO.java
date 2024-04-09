package com.laborsoftware.xpense.domain.dto;

import java.time.ZonedDateTime;
import java.util.Set;

public class WeeklyTimecardDTO {
    private Long id;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    private Integer calendarWeek;

    private Double weeklyWorkingHours;

    private Double weeklyBalance;

    // private Set<ExpenseDTO> expenses;

    private UserDTO user;

    private UserTimecardDTO userTimecard;

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
    public Set<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }

     */

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserTimecardDTO getUserTimecard() {
        return userTimecard;
    }

    public void setUserTimecard(UserTimecardDTO userTimecard) {
        this.userTimecard = userTimecard;
    }
}
