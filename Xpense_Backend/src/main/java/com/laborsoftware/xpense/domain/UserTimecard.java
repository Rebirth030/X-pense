package com.laborsoftware.xpense.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user_timecard")
public class UserTimecard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "weekly_working_hours")
    private Double weeklyWorkingHours;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "user_balance")
    private Double userBalance;
/*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userTimecard")
    private Set<WeeklyTimecard> weeklyTimecards;

    // UserTimecard and User relation is One to one!. Still needed when user has UserTimecard

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
     */

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
    public Set<WeeklyTimecard> getWeeklyTimecards() {
        return weeklyTimecards;
    }

    public void setWeeklyTimecards(Set<WeeklyTimecard> weeklyTimecards) {
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
