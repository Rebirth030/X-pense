package com.laborsoftware.xpense.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "prename")
    private String prename;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "e_mail")
    private String eMail;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "language")
    private String language;

    @Column(name = "weekly_working_hour")
    private Integer weeklyWorkingHour;

    // holiday in days?
    @Column(name = "holiday_working_schedule")
    private Integer holidayWorkingSchedule;

    /*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Expense> expenses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<WeeklyTimecard> weeklyTimecards;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Company> companies;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Project> projects;

     */

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_timecard_id", referencedColumnName = "id")
    private UserTimecard userTimecard;

    @ManyToOne
    @JoinColumn(name = "superior_id")
    private User superior;

    // role - enum ? string : FREELANCER, EMPLOYEE, (STUDENT JOB)

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getWeeklyWorkingHour() {
        return weeklyWorkingHour;
    }

    public void setWeeklyWorkingHour(Integer weeklyWorkingHour) {
        this.weeklyWorkingHour = weeklyWorkingHour;
    }

    public Integer getHolidayWorkingSchedule() {
        return holidayWorkingSchedule;
    }

    public void setHolidayWorkingSchedule(Integer holidayWorkingSchedule) {
        this.holidayWorkingSchedule = holidayWorkingSchedule;
    }

    /*
    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<WeeklyTimecard> getWeeklyTimecards() {
        return weeklyTimecards;
    }

    public void setWeeklyTimecards(Set<WeeklyTimecard> weeklyTimecards) {
        this.weeklyTimecards = weeklyTimecards;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

     */

    public UserTimecard getUserTimecard() {
        return userTimecard;
    }

    public void setUserTimecard(UserTimecard userTimecard) {
        this.userTimecard = userTimecard;
    }

    public User getSuperior() {
        return superior;
    }

    public void setSuperior(User superior) {
        this.superior = superior;
    }
}
