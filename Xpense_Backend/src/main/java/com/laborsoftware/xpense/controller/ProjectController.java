package com.laborsoftware.xpense.controller;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ProjectDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.ProjectMapper;
import com.laborsoftware.xpense.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class ProjectController {

    public final ProjectService projectService;

    @Autowired
    public final ProjectMapper projectMapper;

    public ProjectController(
            ProjectService projectService,
            ProjectMapper projectMapper
    ) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }


    @PostMapping("/projects")
    ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        try {
            Project project = projectMapper.toEntity(projectDTO);
            project = projectService.save(project);
            ProjectDTO result = projectMapper.toDto(project);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/projects/{id}")
    ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO, @PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectService.findOne(id);
            if(optionalProject.isPresent()) {
                Project project = optionalProject.get();
                project = projectMapper.toEntity(projectDTO);
                project = projectService.save(project);
                ProjectDTO result = projectMapper.toDto(project);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/projects")
    ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        List<ProjectDTO> result = projects.stream().map(projectMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/projects/{id}")
    ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectService.findOne(id);
            if(optionalProject.isPresent()) {
                Project project = optionalProject.get();
                ProjectDTO result = projectMapper.toDto(project);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/projects/{id}")
    ResponseEntity<ProjectDTO> deleteProject(@PathVariable Long id) {
        try {
            Project projectToDelete = projectService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            projectService.delete(id);
            ProjectDTO result = projectMapper.toDto(projectToDelete);
            return ResponseEntity.ok().body(result);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
