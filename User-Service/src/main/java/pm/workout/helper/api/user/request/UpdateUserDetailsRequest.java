package pm.workout.helper.api.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.user.Gender;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDetailsRequest {
   @NotNull
   private String username;
   @NotNull
   private String email;
   @NotNull
   private Gender gender;
   @NotNull
   private LocalDate dateOfBirth;
   @NotNull
   private LocalDate trainingStartDate;
   @NotNull
   private boolean notificationsEnabled;
}
