package pm.workout.helper.domain.user.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAuthDetailsDto {
    private final long userId;
    private final String email;
    private final String idToken;
    private final long expiresIn;
}
