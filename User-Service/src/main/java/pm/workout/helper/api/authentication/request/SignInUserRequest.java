package pm.workout.helper.api.authentication.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignInUserRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}