package pm.workout.helper.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.JsonLocalDateSerializer;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;
import pm.workout.helper.domain.user.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendedDetailsDto {
    long userId;
    String username;
    String email;
    Gender gender;
    @JsonLocalDateTimeSerializer
    LocalDateTime registrationDateTime;
    @JsonLocalDateSerializer
    LocalDate dateOfBirth;
    Optional<Long> currentTrainingPlanId;
    HealthDetailsDto healthDetails;
    List<AppUserPhotoDto> userPhotos;
    boolean notificationsEnabled;

}
