package pm.workout.helper.domain.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pm.workout.helper.domain.user.dto.HealthDetailsDto;
import pm.workout.helper.domain.user.dto.UserDetailsDto;
import pm.workout.helper.domain.user.health.AppUserHealthDetails;

@Mapper(componentModel="spring")
public interface UserMapper {
    HealthDetailsDto mapUserHealthDetails(AppUserHealthDetails appUserHealthDetails);
    @Mapping(source = "appUser.id", target = "userId")
    UserDetailsDto mapUserDetails(AppUser appUser);
}
