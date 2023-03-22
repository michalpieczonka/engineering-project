package pm.workout.helper.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pm.workout.helper.api.JsonLocalDateSerializer;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDetailsDto {
    private final long userId;
    private final String username;
    @JsonLocalDateSerializer
    private final LocalDate dateOfBirth;
    @JsonLocalDateTimeSerializer
    private final LocalDateTime registrationDateTime;
    private final long currentTrainingPlanId;
}
