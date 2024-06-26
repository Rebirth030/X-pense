package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ProjectDTO;
import com.laborsoftware.xpense.exceptions.ResourceNotFoundException;
import com.laborsoftware.xpense.mapper.ProjectMapper;
import com.laborsoftware.xpense.repository.ProjectRepository;
import com.laborsoftware.xpense.repository.UserRepository;
import com.laborsoftware.xpense.service.crud.ICrudService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RestController
@RequestMapping
public class ProjectService implements ICrudService<ProjectDTO, Long> {
    private final ProjectRepository projectRepository;

    @Autowired
    public final ProjectMapper projectMapper;

    public final UserRepository userRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            ProjectMapper projectMapper,
            UserRepository userRepository
    ) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userRepository = userRepository;
    }


    @Override
    @PostMapping("/projects")
    public ResponseEntity<ProjectDTO> save(@RequestHeader("Authorization") String token,@RequestBody ProjectDTO projectDTO) {
        logger.debug("Request to save Project {} ", projectDTO);
        try {
            Project project = projectMapper.toEntity(projectDTO);
            project = projectRepository.save(project);
            ProjectDTO result = projectMapper.toDto(project);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> update(@RequestHeader("Authorization") String token,@RequestBody ProjectDTO projectDTO, @PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectRepository.findById(id);
            if(optionalProject.isPresent()) {
                Project project = optionalProject.get();
                project = projectMapper.toEntity(projectDTO);
                project = projectRepository.save(project);
                ProjectDTO result = projectMapper.toDto(project);
                return ResponseEntity.ok().body(result);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/projects/{id}")
    public void delete(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        logger.debug("Request to delete Project {} ", id);
        try {
            Optional<Project> projectToDelete = projectRepository.findById(id);
            if (projectToDelete.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
            projectRepository.deleteById(id);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    @Override
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> findAll(@RequestHeader("Authorization") String token) {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> result = projects.stream().map(projectMapper::toDto).toList();
        return ResponseEntity.ok().body(result);
    }

    @Override
    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> findOne(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        try {
            Optional<Project> optionalProject = projectRepository.findById(id);
            if (optionalProject.isPresent()) {
                Project project = optionalProject.get();
                ProjectDTO result = projectMapper.toDto(project);
                return ResponseEntity.ok().body(result);
            } else {
                throw new ResourceNotFoundException(
                        "Ressource nicht gefunden. Kein Datensatz in der Datenbank zu finden ist."
                );
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
