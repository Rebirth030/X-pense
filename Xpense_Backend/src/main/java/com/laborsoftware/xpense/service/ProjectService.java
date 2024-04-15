package com.laborsoftware.xpense.service;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService implements ICrudService<Project, Long> {

    Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    public ProjectService(
            ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }


    @Override
    public Project save(Project project) {
        logger.debug("Request to save Project {} ", project);
        Project result = null;
        try {
            result = projectRepository.save(project);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Request to delete Project {} ", id);
        try {
            projectRepository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.toString());
        }
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> findOne(Long id) {
        return projectRepository.findById(id);
    }
}
