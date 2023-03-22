package pm.workout.helper.api.external.user;

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
import pm.workout.helper.api.external.user.request.AddUserPhotoRequest;
import pm.workout.helper.api.external.user.request.ChangePasswordRequest;
import pm.workout.helper.api.external.user.request.UpdateUserDetailsRequest;
import pm.workout.helper.api.external.user.request.UpdateUserHealthDetailsRequest;
import pm.workout.helper.domain.training.plan.TrainingPlanService;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.template.TrainingUnitTemplate;
import pm.workout.helper.domain.user.dto.AppUserPhotoDto;
import pm.workout.helper.domain.user.dto.UserDetailsDto;
import pm.workout.helper.domain.user.dto.UserExtendedDetailsDto;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.infrastructure.services.UserService;
import pm.workout.helper.infrastructure.services.WorkoutService;
import pm.workout.helper.infrastructure.utils.ImageCompressor;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/users")
public class UserRestResource {

    private final TrainingPlanService trainingPlanService;
    private final WorkoutService workoutService;
    private final UserService userFeignService;

    @GetMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    UserDetailsDto getUserDetails(@PathVariable long userId){
        return userFeignService.getUserDetails(userId);
    }

    @PutMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    void updateUserDetails(@PathVariable long userId,  @RequestBody @NotNull UpdateUserDetailsRequest request){
        userFeignService.updateUserDetails(userId, request);
    }

    @PutMapping(path = "/{userId}/password")
    @ResponseStatus(HttpStatus.OK)
    void updateUserPassword(@PathVariable long userId,  @RequestBody @NotNull ChangePasswordRequest request){
        userFeignService.updateUserPassword(userId, request);
    }

    @GetMapping(path = "/{userId}/details")
    @ResponseStatus(HttpStatus.OK)
    UserExtendedDetailsDto getUserExtendedDetails(@PathVariable long userId) {
        return userFeignService.getUserExtendedDetails(userId);
    }

    @PutMapping(path="/{userId}/health-details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUserHealthDetails(@PathVariable long userId, @RequestBody @NotNull UpdateUserHealthDetailsRequest request){
        userFeignService.updateUserHealthDetails(userId, request);
    }

    @PutMapping(path = "/{userId}/training-plan/{trainingPlanId}")
    @ResponseStatus(HttpStatus.OK)
    void updateUserCurrentTrainingPlan(@PathVariable @NotNull long userId, @PathVariable @NotNull long trainingPlanId){
        userFeignService.updateCurrentTrainingPlan(userId, trainingPlanId);
    }

    @GetMapping("/{userId}/training-plan")
    @ResponseStatus(HttpStatus.OK)
    TrainingPlanDetailsDto getCurrentUserTrainingPlan(@PathVariable @NotNull long userId){
        return userFeignService.getCurrentTrainingPlan(userId);
    }

    @GetMapping("/{userId}/training-plans/{trainingPlanId}/training-units/{trainingUnitId}/template")
    @ResponseStatus(HttpStatus.OK)
    TrainingUnitTemplate getUserTrainingUnitTemplate(@PathVariable @NotNull long userId,
                                                     @PathVariable @NotNull long trainingPlanId,
                                                     @PathVariable @NotNull long trainingUnitId){
        return trainingPlanService.getTrainingUnitTemplate(userId, trainingPlanId, trainingUnitId);
    }

   @GetMapping("/{userId}/workouts")
   @ResponseStatus(HttpStatus.OK)
    List<UserWorkoutDto> getUserWorkouts(@PathVariable @NotNull long userId){
        return workoutService.getAllUserWorkouts(userId);
    }

    @PostMapping("/{userId}/workouts")
    @ResponseStatus(HttpStatus.CREATED)
    void addUserWorkoutSession(@PathVariable @NotNull long userId, @RequestBody @NotNull Object workoutSession){
        workoutService.addUserWorkoutSession(userId, workoutSession);
    }

    @PostMapping("/{userId}/photos")
    @ResponseStatus(HttpStatus.CREATED)
    void addUserPhoto(@PathVariable @NotNull long userId,
                      @RequestParam("file") MultipartFile file) throws IOException {

        userFeignService.addUserPhoto(userId, new AddUserPhotoRequest(file.getContentType(), file.getOriginalFilename(), ImageCompressor.compressImage(file.getBytes())));
    }

    @GetMapping("/photos/{userPhotoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserPhoto(@PathVariable @NotNull long userPhotoId) {
        AppUserPhotoDto photo = userFeignService.getUserPhoto(userPhotoId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(photo.getType()))
                .body(photo.getImageData());
    }

    @GetMapping("/photos/{userPhotoId}/details")
    @ResponseStatus(HttpStatus.OK)
    public AppUserPhotoDto getUserPhotoDetails(@PathVariable @NotNull long userPhotoId){
        return userFeignService.getUserPhoto(userPhotoId);
    }

    @DeleteMapping("/photos/{userPhotoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserPhoto(@PathVariable @NotNull long userPhotoId){
        userFeignService.deleteUserPhoto(userPhotoId);
    }

    @GetMapping("/{userId}/statistics")
    @ResponseStatus(HttpStatus.OK)
    public Object getUserTrainingStatistics(@PathVariable @NotNull String userId){
        return workoutService.getUserWorkoutsBasicStatistics(userId);
    }
}
