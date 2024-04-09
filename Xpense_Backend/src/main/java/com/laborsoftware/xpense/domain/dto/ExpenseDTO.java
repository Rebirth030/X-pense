package com.laborsoftware.xpense.domain.dto;

import java.time.ZonedDateTime;

public class ExpenseDTO {
    private Long id;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    private String state;

    private UserDTO user;

    private ProjectDTO project;

    private WeeklyTimecardDTO weeklyTimecard;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public WeeklyTimecardDTO getWeeklyTimecard() {
        return weeklyTimecard;
    }

    public void setWeeklyTimecard(WeeklyTimecardDTO weeklyTimecard) {
        this.weeklyTimecard = weeklyTimecard;
    }
}
