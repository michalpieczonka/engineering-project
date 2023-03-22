package pm.workout.helper.domain.user;

import org.springframework.web.multipart.MultipartFile;
import pm.workout.helper.api.authentication.request.CreateUserRequest;
import pm.workout.helper.api.user.request.AddUserPhotoRequest;
import pm.workout.helper.api.user.request.UpdateUserDetailsRequest;
import pm.workout.helper.api.user.request.UpdateUserHealthDetailsRequest;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.user.authentication.dto.AppUserDto;
import pm.workout.helper.domain.user.dto.AppUserPhotoDto;
import pm.workout.helper.domain.user.dto.UserDetailsDto;
import pm.workout.helper.domain.user.dto.UserExtendedDetailsDto;

import java.io.IOException;

public interface UserService {
    void createUser(CreateUserRequest createUserRequest);
    AppUserDto getUserByLogin(String username);

    UserDetailsDto getUserDetails(long userId);
    void updateUserHealthDetails(long userId, UpdateUserHealthDetailsRequest updateUserHealthDetailsRequest);
    void setCurrentTrainingPlan(long userId, long trainingPlanId);
    TrainingPlanDetailsDto getCurrentTrainingPlan(long userId);
    AppUserPhotoDto getUserPhotoDetails(long photoId);
    void saveUserPhoto(long userId, AddUserPhotoRequest savePhotoDto) throws IOException;
    UserExtendedDetailsDto getAllUserDetails(long userId);
    void updateUserDetails(long userId, UpdateUserDetailsRequest request);
    void changeUserPassword(long userId, String currentPassword, String newPassword);
    void deleteUserPhoto(long userPhotoId);
    void findUsersWithUpdateRequiredAndSendNotification();
    void findUsersWithTrainingTodayAndSendReminderNotification();
}
