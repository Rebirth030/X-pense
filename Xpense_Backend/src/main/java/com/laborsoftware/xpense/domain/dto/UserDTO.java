package com.laborsoftware.xpense.domain.dto;

import com.laborsoftware.xpense.domain.enumeration.ApplicationUserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


    private Long superiorId;

    private String token;
    private LocalDateTime tokenExpirationDate;

    private ApplicationUserRole role;

    private Double forcedBreakAfter;

    private Boolean forcedBreakAfterOn;

    private Double forcedEndAfter;

    private Boolean forcedEndAfterOn;

    private Boolean notification;

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


    public LocalDateTime getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(LocalDateTime tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public Double getForcedBreakAfter() {
        return forcedBreakAfter;
    }

    public void setForcedBreakAfter(Double forcedBreakAfter) {
        this.forcedBreakAfter = forcedBreakAfter;
    }

    public Double getForcedEndAfter() {
        return forcedEndAfter;
    }

    public void setForcedEndAfter(Double forcedEndAfter) {
        this.forcedEndAfter = forcedEndAfter;
    }

    public Boolean getForcedBreakAfterOn() {
        return forcedBreakAfterOn;
    }

    public void setForcedBreakAfterOn(Boolean forcedBreakAfterOn) {
        this.forcedBreakAfterOn = forcedBreakAfterOn;
    }

    public Boolean getForcedEndAfterOn() {
        return forcedEndAfterOn;
    }

    public void setForcedEndAfterOn(Boolean forcedEndAfterOn) {
        this.forcedEndAfterOn = forcedEndAfterOn;
    }

    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }
}
