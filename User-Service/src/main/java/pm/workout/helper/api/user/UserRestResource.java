package pm.workout.helper.api.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pm.workout.helper.api.user.request.AddUserPhotoRequest;
import pm.workout.helper.api.user.request.ChangePasswordRequest;
import pm.workout.helper.api.user.request.UpdateUserDetailsRequest;
import pm.workout.helper.api.user.request.UpdateUserHealthDetailsRequest;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.user.UserService;
import pm.workout.helper.domain.user.dto.AppUserPhotoDto;
import pm.workout.helper.domain.user.dto.UserDetailsDto;
import pm.workout.helper.domain.user.dto.UserExtendedDetailsDto;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user-service/api/users")
public class UserRestResource {

    private final UserService userService;

    @GetMapping(path = "/{userId}")
    UserDetailsDto getUserDetails(@PathVariable long userId){
        return userService.getUserDetails(userId);
    }

    @PutMapping(path = "/{userId}")
    void updateUserDetails(@PathVariable long userId,  @RequestBody @NotNull UpdateUserDetailsRequest request){
        userService.updateUserDetails(userId, request);
    }

    @PutMapping(path = "/{userId}/password")
    void updateUserPassword(@PathVariable long userId,  @RequestBody @NotNull ChangePasswordRequest request){
        userService.changeUserPassword(userId, request.getOldPassword(), request.getNewPassword());
    }

    @GetMapping(path = "/{userId}/details")
    UserExtendedDetailsDto getUserExtendedDetails(@PathVariable long userId) {
        return userService.getAllUserDetails(userId);
    }

    @PutMapping(path="/{userId}/health-details")
    void updateUserHealthDetails(@PathVariable long userId, @RequestBody @NotNull UpdateUserHealthDetailsRequest request){
        userService.updateUserHealthDetails(userId, request);
    }

    @PutMapping(path = "/{userId}/training-plan/{trainingPlanId}")
    void updateUserCurrentTrainingPlan(@PathVariable @NotNull long userId, @PathVariable @NotNull long trainingPlanId){
        userService.setCurrentTrainingPlan(userId, trainingPlanId);
    }

    @GetMapping("/{userId}/training-plan")
    TrainingPlanDetailsDto getCurrentUserTrainingPlan(@PathVariable @NotNull long userId){
        return userService.getCurrentTrainingPlan(userId);
    }

    @PostMapping("/{userId}/photos")
    void addUserPhoto(@PathVariable @NotNull long userId,
                      @RequestBody @NotNull AddUserPhotoRequest file) throws IOException {
        userService.saveUserPhoto(userId, file);
    }

    @GetMapping("/photos/{userPhotoId}")
    public AppUserPhotoDto getUserPhoto(@PathVariable @NotNull long userPhotoId) {
        return userService.getUserPhotoDetails(userPhotoId);
    }

    @DeleteMapping("/photos/{userPhotoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserPhoto(@PathVariable @NotNull long userPhotoId){
        userService.deleteUserPhoto(userPhotoId);
    }
}
