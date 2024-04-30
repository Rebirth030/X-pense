package com.laborsoftware.xpense.mapper;

import com.laborsoftware.xpense.domain.ApplicationUser;
import com.laborsoftware.xpense.domain.UserTimecard;
import com.laborsoftware.xpense.domain.WeeklyTimecard;
import com.laborsoftware.xpense.domain.dto.WeeklyTimecardDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-30T15:03:06+0200",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class WeeklyTimecardMapperImpl implements WeeklyTimecardMapper {

    @Override
    public WeeklyTimecard toEntity(WeeklyTimecardDTO weeklyTimecardDTO) {
        if ( weeklyTimecardDTO == null ) {
            return null;
        }

        WeeklyTimecard weeklyTimecard = new WeeklyTimecard();

        weeklyTimecard.setUser( weeklyTimecardDTOToApplicationUser( weeklyTimecardDTO ) );
        weeklyTimecard.setUserTimecard( weeklyTimecardDTOToUserTimecard( weeklyTimecardDTO ) );
        weeklyTimecard.setId( weeklyTimecardDTO.getId() );
        weeklyTimecard.setStartDateTime( weeklyTimecardDTO.getStartDateTime() );
        weeklyTimecard.setEndDateTime( weeklyTimecardDTO.getEndDateTime() );
        weeklyTimecard.setCalendarWeek( weeklyTimecardDTO.getCalendarWeek() );
        weeklyTimecard.setWeeklyWorkingHours( weeklyTimecardDTO.getWeeklyWorkingHours() );
        weeklyTimecard.setWeeklyBalance( weeklyTimecardDTO.getWeeklyBalance() );

        return weeklyTimecard;
    }

    @Override
    public WeeklyTimecardDTO toDto(WeeklyTimecard weeklyTimecard) {
        if ( weeklyTimecard == null ) {
            return null;
        }

        WeeklyTimecardDTO weeklyTimecardDTO = new WeeklyTimecardDTO();

        weeklyTimecardDTO.setUserId( weeklyTimecardUserId( weeklyTimecard ) );
        weeklyTimecardDTO.setUserTimecardId( weeklyTimecardUserTimecardId( weeklyTimecard ) );
        weeklyTimecardDTO.setId( weeklyTimecard.getId() );
        weeklyTimecardDTO.setStartDateTime( weeklyTimecard.getStartDateTime() );
        weeklyTimecardDTO.setEndDateTime( weeklyTimecard.getEndDateTime() );
        weeklyTimecardDTO.setCalendarWeek( weeklyTimecard.getCalendarWeek() );
        weeklyTimecardDTO.setWeeklyWorkingHours( weeklyTimecard.getWeeklyWorkingHours() );
        weeklyTimecardDTO.setWeeklyBalance( weeklyTimecard.getWeeklyBalance() );

        return weeklyTimecardDTO;
    }

    protected ApplicationUser weeklyTimecardDTOToApplicationUser(WeeklyTimecardDTO weeklyTimecardDTO) {
        if ( weeklyTimecardDTO == null ) {
            return null;
        }

        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setId( weeklyTimecardDTO.getUserId() );

        return applicationUser;
    }

    protected UserTimecard weeklyTimecardDTOToUserTimecard(WeeklyTimecardDTO weeklyTimecardDTO) {
        if ( weeklyTimecardDTO == null ) {
            return null;
        }

        UserTimecard userTimecard = new UserTimecard();

        userTimecard.setId( weeklyTimecardDTO.getUserTimecardId() );

        return userTimecard;
    }

    private Long weeklyTimecardUserId(WeeklyTimecard weeklyTimecard) {
        if ( weeklyTimecard == null ) {
            return null;
        }
        ApplicationUser user = weeklyTimecard.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long weeklyTimecardUserTimecardId(WeeklyTimecard weeklyTimecard) {
        if ( weeklyTimecard == null ) {
            return null;
        }
        UserTimecard userTimecard = weeklyTimecard.getUserTimecard();
        if ( userTimecard == null ) {
            return null;
        }
        Long id = userTimecard.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
