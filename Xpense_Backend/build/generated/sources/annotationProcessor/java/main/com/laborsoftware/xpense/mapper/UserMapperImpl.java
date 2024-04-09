package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.User;
import com.laborsoftware.xpense.domain.dto.UserDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-09T13:36:14+0200",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDTO userDTO) {
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
