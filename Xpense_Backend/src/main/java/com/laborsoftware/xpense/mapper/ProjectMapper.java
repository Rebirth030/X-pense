package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProjectMapper {
    @Mapping(source = "userId", target = "user.id")
    Project toEntity(ProjectDTO projectDTO);

    @Mapping(source = "user.id", target = "userId")
    ProjectDTO toDto(Project project);
}
