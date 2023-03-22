package pm.workout.helper.api.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.JsonLocalDateDeserializer;
import pm.workout.helper.domain.user.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateUserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;
    @JsonLocalDateDeserializer
    private LocalDate dateOfBirth;
    @JsonLocalDateDeserializer
    private LocalDate trainingStartDate;
    @NotNull
    private Gender gender;
}
