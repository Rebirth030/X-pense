package com.laborsoftware.xpense.domain.dto;

import com.laborsoftware.xpense.domain.enumeration.ApplicationUserRole;

import java.util.Date;

public class UserDTO {
    private Long id;

    private String prename;

    private String lastname;

    private String eMail;

    private String username;

    private String password;

    private String country;

    private String language;

    private Double weeklyWorkingHour;

    private Double holidayWorkingSchedule;

    private Long userTimecardId;

    private Long superiorId;

    private String token;
    private Date tokenExpirationDate;

    private ApplicationUserRole role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getHolidayWorkingSchedule() {
        return holidayWorkingSchedule;
    }

    public void setHolidayWorkingSchedule(Double holidayWorkingSchedule) {
        this.holidayWorkingSchedule = holidayWorkingSchedule;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getWeeklyWorkingHour() {
        return weeklyWorkingHour;
    }

    public void setWeeklyWorkingHour(Double weeklyWorkingHour) {
        this.weeklyWorkingHour = weeklyWorkingHour;
    }

    public Long getUserTimecardId() {
        return userTimecardId;
    }

    public void setUserTimecardId(Long userTimecardId) {
        this.userTimecardId = userTimecardId;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public ApplicationUserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUserRole role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(Date tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
