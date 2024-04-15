package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Project;
import com.laborsoftware.xpense.domain.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, UserMapper.class})
public interface ProjectMapper {
    @Mapping(source = "companyId", target = "company.id")
    @Mapping(source = "userId", target = "user.id")
    Project toEntity(ProjectDTO projectDTO);

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "user.id", target = "userId")
    ProjectDTO toDto(Project project);
}
