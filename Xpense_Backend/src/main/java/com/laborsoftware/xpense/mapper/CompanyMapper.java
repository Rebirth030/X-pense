package com.laborsoftware.xpense.mapper;


import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    Company toEntity(CompanyDTO companyDTO);
}
