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
    ResponseEntity<Project> createEvent(@RequestBody ProjectDTO projectDTO) {
        try {
            Project project = projectMapper.toEntity(projectDTO);
            return ResponseEntity.ok().body(projectService.save(project));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/projects/{id}")
    ResponseEntity<Project> updateEvent(@RequestBody ProjectDTO projectDTO, @PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectService.findOne(id);
            if(optionalProject.isPresent()) {
                return ResponseEntity.ok().body(projectService.save(projectMapper.toEntity(projectDTO)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/projects")
    ResponseEntity<List<Project>> getAllEvents() {
        return new ResponseEntity<>(projectService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/projects/{id}")
    ResponseEntity<Project> getEvent(@PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectService.findOne(id);
            if(optionalProject.isPresent()) {
                optionalProject.get();
                return ResponseEntity.ok().body(optionalProject.get());
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
    ResponseEntity<Project> deleteEvent(@PathVariable Long id) {
        try {
            Project projectToDelete = projectService.findOne(id).orElseThrow(() -> new ResourceNotFoundException(
                    "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
            ));
            projectService.delete(id);
            return new ResponseEntity<>(projectToDelete, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
