package com.laborsoftware.xpense.domain.dto;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.WeeklyTimecard;

import java.util.Set;

public class UserDTO {
    private Long id;

    private String prename;

    private String lastname;

    private String eMail;

    private String username;

    private String password;

    private String country;

    private String language;

    private Integer weeklyWorkingHour;

    private Integer holidayWorkingSchedule;

    /*
    private Set<ExpenseDTO> expenseDTOs;

    private Set<WeeklyTimecardDTO> weeklyTimecardDTOs;

    private Set<CompanyDTO> companyDTOs;

    private Set<ProjectDTO> projectDTOs;
     */

    private UserTimecard userTimecard;

    private User superior;

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

    public Integer getHolidayWorkingSchedule() {
        return holidayWorkingSchedule;
    }

    public void setHolidayWorkingSchedule(Integer holidayWorkingSchedule) {
        this.holidayWorkingSchedule = holidayWorkingSchedule;
    }

    /*
    public Set<ExpenseDTO> getExpenseDTOs() {
        return expenseDTOs;
    }

    public void setExpenseDTOs(Set<ExpenseDTO> expenseDTOs) {
        this.expenseDTOs = expenseDTOs;
    }

    public Set<WeeklyTimecardDTO> getWeeklyTimecardDTOs() {
        return weeklyTimecardDTOs;
    }

    public void setWeeklyTimecardDTOs(Set<WeeklyTimecardDTO> weeklyTimecardDTOs) {
        this.weeklyTimecardDTOs = weeklyTimecardDTOs;
    }

    public Set<CompanyDTO> getCompanyDTOs() {
        return companyDTOs;
    }

    public void setCompanyDTOs(Set<CompanyDTO> companyDTOs) {
        this.companyDTOs = companyDTOs;
    }



    public Set<ProjectDTO> getProjectDTOs() {
        return projectDTOs;
    }

    public void setProjectDTOs(Set<ProjectDTO> projectDTOs) {
        this.projectDTOs = projectDTOs;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getWeeklyWorkingHour() {
        return weeklyWorkingHour;
    }

    public void setWeeklyWorkingHour(Integer weeklyWorkingHour) {
        this.weeklyWorkingHour = weeklyWorkingHour;
    }
}
