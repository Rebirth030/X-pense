package com.laborsoftware.xpense.domain.dto;

import java.util.Set;

public class CompanyDTO {
    private Long id;

    private String name;

    private String address;

    //private Set<ProjectDTO> projects;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /*
    public Set<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectDTO> projects) {
        this.projects = projects;
    }

     */

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
