package pm.workout.helper.api.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pm.workout.helper.api.authentication.request.CreateUserRequest;
import pm.workout.helper.api.authentication.request.SignInUserRequest;
import pm.workout.helper.domain.user.AppUser;
import pm.workout.helper.domain.user.UserService;
import pm.workout.helper.domain.user.authentication.dto.AppUserDto;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/user-service/api/authentication")
public class AuthenticationRestResource {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewUser(@RequestBody @Valid CreateUserRequest createUserRequest){
        userService.createUser(createUserRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AppUserDto signInUser(@RequestBody @Valid String username){
        return userService.getUserByLogin(username);
    }

}
