package pm.workout.helper.domain.user.authentication;

import pm.workout.helper.api.external.authentication.request.SignInUserRequest;
import pm.workout.helper.domain.user.authentication.dto.UserAuthDetailsDto;

public interface AuthenticationService {
    UserAuthDetailsDto signInAndReturnJwt(SignInUserRequest signInUserRequest);
}
