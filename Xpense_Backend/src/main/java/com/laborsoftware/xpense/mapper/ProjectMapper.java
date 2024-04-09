package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ProjectDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectDTO projectDTO);
}
