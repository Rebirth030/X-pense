package com.laborsoftware.xpense.domain;

import com.laborsoftware.xpense.domain.enumeration.ApplicationUserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "application_user")
public class ApplicationUser {

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
    private Double weeklyWorkingHour;

    @Column(name = "holiday_working_schedule")
    private Double holidayWorkingSchedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_timecard_id", referencedColumnName = "id")
    private UserTimecard userTimecard;

    @ManyToOne
    @JoinColumn(name = "superior_id")
    private ApplicationUser superior;

    @Enumerated(EnumType.STRING)
    private ApplicationUserRole role;

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

    public Double getWeeklyWorkingHour() {
        return weeklyWorkingHour;
    }

    public void setWeeklyWorkingHour(Double weeklyWorkingHour) {
        this.weeklyWorkingHour = weeklyWorkingHour;
    }

    public Double getHolidayWorkingSchedule() {
        return holidayWorkingSchedule;
    }

    public void setHolidayWorkingSchedule(Double holidayWorkingSchedule) {
        this.holidayWorkingSchedule = holidayWorkingSchedule;
    }

    public UserTimecard getUserTimecard() {
        return userTimecard;
    }

    public void setUserTimecard(UserTimecard userTimecard) {
        this.userTimecard = userTimecard;
    }

    public ApplicationUser getSuperior() {
        return superior;
    }

    public void setSuperior(ApplicationUser superior) {
        this.superior = superior;
    }

    public ApplicationUserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUserRole role) {
        this.role = role;
    }
}
