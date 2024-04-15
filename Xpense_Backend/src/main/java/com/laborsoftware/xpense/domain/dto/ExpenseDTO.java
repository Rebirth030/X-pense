package com.laborsoftware.xpense.domain.dto;

import com.laborsoftware.xpense.domain.enumeration.ExpenseState;

import java.time.ZonedDateTime;

public class ExpenseDTO {
    private Long id;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    private ExpenseState state;

    private Long userId;

    private Long projectId;

    private Long weeklyTimecardId;

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

    public ExpenseState getState() {
        return state;
    }

    public void setState(ExpenseState state) {
        this.state = state;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getWeeklyTimecardId() {
        return weeklyTimecardId;
    }

    public void setWeeklyTimecardId(Long weeklyTimecardId) {
        this.weeklyTimecardId = weeklyTimecardId;
    }
}
