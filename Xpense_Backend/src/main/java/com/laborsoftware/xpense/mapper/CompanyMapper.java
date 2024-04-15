package com.laborsoftware.xpense.mapper;


import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CompanyMapper {

    @Mapping(source = "userId", target = "user.id")
    Company toEntity(CompanyDTO companyDTO);

    @Mapping(source = "user.id", target = "userId")
    CompanyDTO toDto(Company company);
}
