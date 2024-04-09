package com.laborsoftware.xpense.interfaces;

import com.laborsoftware.xpense.domain.Project;

import java.util.List;
import java.util.Optional;

public interface IProjectService {
    Project save(Project project);

    void delete(Long id);

    List<Project> findAll();

    Optional<Project> findOne(Long id);
}
