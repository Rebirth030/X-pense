package com.laborsoftware.xpense.domain;

import com.laborsoftware.xpense.domain.enumeration.ApplicationUserRole;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "application_user")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //region Properties
    private String prename;
    private String lastname;
    private String eMail;
    @Column(unique = true)
    private String username;
    private String password;
    private String country;
    private String language;
    private Double weeklyWorkingHour;
    private Double holidayWorkingSchedule;
    @ManyToOne
    private ApplicationUser superior;
    @Enumerated(EnumType.STRING)
    private ApplicationUserRole role;
    private String token;
    private LocalDateTime tokenExpirationDate;

    // role - enum ? string : FREELANCER, EMPLOYEE, (STUDENT JOB)
    //endregion

    //region Getter and Setter
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
    //endregion



    //TODO: implement Methods and Properties from UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
