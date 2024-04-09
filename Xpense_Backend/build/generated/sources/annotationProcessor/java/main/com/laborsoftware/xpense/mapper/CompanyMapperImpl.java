package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.Company;
import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.dto.CompanyDTO;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-09T13:36:14+0200",
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

        company.setId( companyDTO.getId() );
        company.setName( companyDTO.getName() );
        company.setAddress( companyDTO.getAddress() );
        company.setUser( userDTOToUser( companyDTO.getUser() ) );

        return company;
    }

    protected User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setPrename( userDTO.getPrename() );
        user.setLastname( userDTO.getLastname() );
        user.seteMail( userDTO.geteMail() );
        user.setUsername( userDTO.getUsername() );
        user.setPassword( userDTO.getPassword() );
        user.setCountry( userDTO.getCountry() );
        user.setLanguage( userDTO.getLanguage() );
        user.setWeeklyWorkingHour( userDTO.getWeeklyWorkingHour() );
        user.setHolidayWorkingSchedule( userDTO.getHolidayWorkingSchedule() );
        user.setUserTimecard( userDTO.getUserTimecard() );
        user.setSuperior( userDTO.getSuperior() );

        return user;
    }
}
