package pm.workout.helper.api.external.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.api.external.authentication.request.CreateUserRequest;
import pm.workout.helper.api.external.authentication.request.SignInUserRequest;
import pm.workout.helper.domain.user.authentication.AuthenticationService;
import pm.workout.helper.domain.user.authentication.dto.UserAuthDetailsDto;
import pm.workout.helper.infrastructure.services.UserService;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationRestResource {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewUser(@RequestBody @Valid CreateUserRequest createUserRequest){
        userService.registerNewUser(createUserRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserAuthDetailsDto signInUser(@RequestBody @Valid SignInUserRequest signInUserRequest){
        return authenticationService.signInAndReturnJwt(signInUserRequest);
    }

}
