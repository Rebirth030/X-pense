package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.dto.UserTimecardDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-30T11:57:36+0200",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserTimecardMapperImpl implements UserTimecardMapper {

    @Override
    public UserTimecard toEntity(UserTimecardDTO userTimecardDTO) {
        if ( userTimecardDTO == null ) {
            return null;
        }

        UserTimecard userTimecard = new UserTimecard();

        userTimecard.setId( userTimecardDTO.getId() );
        userTimecard.setWeeklyWorkingHours( userTimecardDTO.getWeeklyWorkingHours() );
        userTimecard.setBalance( userTimecardDTO.getBalance() );
        userTimecard.setUserBalance( userTimecardDTO.getUserBalance() );

        return userTimecard;
    }

    @Override
    public UserTimecardDTO toDto(UserTimecard userTimecard) {
        if ( userTimecard == null ) {
            return null;
        }

        UserTimecardDTO userTimecardDTO = new UserTimecardDTO();

        userTimecardDTO.setId( userTimecard.getId() );
        userTimecardDTO.setWeeklyWorkingHours( userTimecard.getWeeklyWorkingHours() );
        userTimecardDTO.setBalance( userTimecard.getBalance() );
        userTimecardDTO.setUserBalance( userTimecard.getUserBalance() );

        return userTimecardDTO;
    }
}
