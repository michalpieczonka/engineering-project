package pm.workout.helper.infrastructure.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pm.workout.helper.api.external.authentication.request.CreateUserRequest;
import pm.workout.helper.api.external.authentication.request.SignInUserRequest;
import pm.workout.helper.api.external.user.request.AddUserPhotoRequest;
import pm.workout.helper.api.external.user.request.ChangePasswordRequest;
import pm.workout.helper.api.external.user.request.UpdateUserDetailsRequest;
import pm.workout.helper.api.external.user.request.UpdateUserHealthDetailsRequest;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.user.dto.AppUserDto;
import pm.workout.helper.domain.user.dto.AppUserPhotoDto;
import pm.workout.helper.domain.user.dto.UserDetailsDto;
import pm.workout.helper.domain.user.dto.UserExtendedDetailsDto;
import pm.workout.helper.infrastructure.configuration.FeignConfiguration;

import javax.validation.constraints.NotNull;

@FeignClient(value = "user-service",
        path = "/user-service/api",
        configuration = FeignConfiguration.class
)
public interface UserService {
    @PostMapping("/authentication/register")
    void registerNewUser(@RequestBody @NotNull CreateUserRequest createUserRequest);

    @PostMapping("/authentication/login")
    AppUserDto signInUser(@RequestBody @NotNull String username);

    @GetMapping("/users/{userId}")
    UserDetailsDto getUserDetails(@PathVariable @NotNull Long userId);

    @PutMapping("/users/{userId}")
    void updateUserDetails(@PathVariable @NotNull Long userId, @RequestBody @NotNull UpdateUserDetailsRequest userDetailsDto);

    @PutMapping("/users/{userId}/password")
    void updateUserPassword(@PathVariable @NotNull Long userId, @RequestBody @NotNull ChangePasswordRequest request);

    @GetMapping("/users/{userId}/details")
    UserExtendedDetailsDto getUserExtendedDetails(@PathVariable @NotNull Long userId);

    @PutMapping("users/{userId}/health-details")
    void updateUserHealthDetails(@PathVariable @NotNull Long userId, @RequestBody @NotNull UpdateUserHealthDetailsRequest request);

    @PutMapping("/users/{userId}/training-plan/{trainingPlanId}")
    void updateCurrentTrainingPlan(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long trainingPlanId);

    @GetMapping("/users/{userId}/training-plan")
    TrainingPlanDetailsDto getCurrentTrainingPlan(@PathVariable @NotNull Long userId);

    @PostMapping("/users/{userId}/photos")
    void addUserPhoto(@PathVariable @NotNull long userId,
                      @RequestBody @NotNull AddUserPhotoRequest file);

    @GetMapping("/users/photos/{userPhotoId}")
    AppUserPhotoDto getUserPhoto(@PathVariable @NotNull Long userPhotoId);

    @DeleteMapping("/users/photos/{userPhotoId}")
    void deleteUserPhoto(@PathVariable @NotNull Long userPhotoId);
}
