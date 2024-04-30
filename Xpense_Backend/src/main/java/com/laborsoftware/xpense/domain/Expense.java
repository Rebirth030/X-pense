package com.laborsoftware.xpense.domain;

import com.laborsoftware.xpense.domain.enumeration.ExpenseState;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // description

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private ExpenseState state;

    @ManyToOne()
    private ApplicationUser applicationUser;

    @ManyToOne()
    private Project project;

    @ManyToOne()
    private WeeklyTimecard weeklyTimecard;

    private String description;


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

    public ApplicationUser getUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public WeeklyTimecard getWeeklyTimecard() {
        return weeklyTimecard;
    }

    public void setWeeklyTimecard(WeeklyTimecard weeklyTimecard) {
        this.weeklyTimecard = weeklyTimecard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
