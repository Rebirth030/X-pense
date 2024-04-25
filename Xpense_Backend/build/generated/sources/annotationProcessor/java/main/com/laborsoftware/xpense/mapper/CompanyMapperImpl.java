package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-25T16:16:19+0200",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public Company toEntity(CompanyDTO companyDTO) {
        if ( companyDTO == null ) {
            return null;
        }

        Company company = new Company();

        company.setUser( companyDTOToApplicationUser( companyDTO ) );
        company.setId( companyDTO.getId() );
        company.setName( companyDTO.getName() );
        company.setAddress( companyDTO.getAddress() );

        return company;
    }

    @Override
    public CompanyDTO toDto(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setUserId( companyUserId( company ) );
        companyDTO.setId( company.getId() );
        companyDTO.setName( company.getName() );
        companyDTO.setAddress( company.getAddress() );

        return companyDTO;
    }

    protected ApplicationUser companyDTOToApplicationUser(CompanyDTO companyDTO) {
        if ( companyDTO == null ) {
            return null;
        }

        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setId( companyDTO.getUserId() );

        return applicationUser;
    }

    private Long companyUserId(Company company) {
        if ( company == null ) {
            return null;
        }
        ApplicationUser user = company.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
