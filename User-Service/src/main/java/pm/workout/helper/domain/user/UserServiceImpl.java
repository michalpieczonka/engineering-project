package pm.workout.helper.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pm.workout.helper.api.authentication.request.CreateUserRequest;
import pm.workout.helper.api.authentication.request.SignInUserRequest;
import pm.workout.helper.api.user.request.AddUserPhotoRequest;
import pm.workout.helper.api.user.request.UpdateUserDetailsRequest;
import pm.workout.helper.api.user.request.UpdateUserHealthDetailsRequest;
import pm.workout.helper.domain.notification.Notification;
import pm.workout.helper.domain.notification.NotificationRepository;
import pm.workout.helper.domain.notification.NotificationType;
import pm.workout.helper.domain.training.plan.TrainingDay;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.user.authentication.dto.AppUserDto;
import pm.workout.helper.domain.user.dto.AppUserPhotoDto;
import pm.workout.helper.domain.user.dto.HealthDetailsDto;
import pm.workout.helper.domain.user.dto.UserDetailsDto;
import pm.workout.helper.domain.user.dto.UserExtendedDetailsDto;
import pm.workout.helper.domain.user.exception.UserNotFoundException;
import pm.workout.helper.domain.user.health.AppUserHealthDetails;
import pm.workout.helper.domain.user.health.AppUserPhoto;
import pm.workout.helper.infrastructure.services.TrainingPlanConfigurationService;
import pm.workout.helper.infrastructure.utils.ImageCompressor;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TrainingPlanConfigurationService trainingPlanConfigurationService;
    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void createUser(CreateUserRequest createUserRequest) {
        if (userRepository.findUserByEmail(createUserRequest.getEmail()).isPresent() || userRepository.findUserByUsername(createUserRequest.getUsername()).isPresent()) {
            throw new IllegalStateException("User with given email or username already exists");
        }
        AppUser appUser = createNewUser(createUserRequest);
        AppUserHealthDetails healthDetails = createNewUserHealthDetails();
        appUser.updateHealthDetails(healthDetails);
        userRepository.saveUser(appUser);
    }

    @Transactional(readOnly = true)
    @Override
    public AppUserDto getUserByLogin(String username) {
        AppUser appUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException("User not exists"));
        return new AppUserDto(appUser.getId(), appUser.getUsername(), appUser.getPassword(), appUser.getEmail(), appUser.getUserAuthority());
    }

    @Override
    public UserDetailsDto getUserDetails(long userId) {
        return userRepository.findUserById(userId)
                .map(userMapper::mapUserDetails)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", userId)));
    }

    @Transactional
    @Override
    public void updateUserDetails(long userId, UpdateUserDetailsRequest request) {
        AppUser appUser = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %s not found", userId)));
        appUser.setNotificationsEnabled(request.isNotificationsEnabled());

        if(!Objects.equals(appUser.getUsername(), request.getUsername())){
            Optional<AppUser> optionalAppUser = userRepository.findUserByUsername(request.getUsername());
            if(optionalAppUser.isPresent()){
                throw new IllegalStateException("Username already taken");
            } else {
                appUser.setUsername(request.getUsername());
            }
        }

        if(!Objects.equals(appUser.getEmail(), request.getEmail())){
            Optional<AppUser> optionalAppUser = userRepository.findUserByEmail(request.getEmail());
            if(optionalAppUser.isPresent()){
                throw new IllegalStateException("Email already taken");
            } else {
                appUser.setEmail(request.getEmail());
            }
        }
        appUser.setDateOfBirth(request.getDateOfBirth());
        appUser.setGender(request.getGender());
        appUser.setTrainingStartDate(request.getTrainingStartDate());
    }

    @Transactional
    @Override
    public void changeUserPassword(long userId, String currentPassword, String newPassword) {
        AppUser appUser = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %s not found", userId)));
        if(passwordEncoder.matches(currentPassword, appUser.getPassword())){
            appUser.setPassword(passwordEncoder.encode(newPassword));
        } else {
            throw new IllegalStateException("Current password is incorrect");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserExtendedDetailsDto getAllUserDetails(long userId) {
        return userRepository.findUserById(userId)
                .map(this::mapUserDetails)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", userId)));
    }

    @Transactional
    @Override
    public void updateUserHealthDetails(long userId, UpdateUserHealthDetailsRequest request){
        AppUser appUser = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %s not found", userId)));

        AppUserHealthDetails healthDetails = appUser.getHealthDetails();
        healthDetails.setWeight(request.getWeight());
        healthDetails.setHeight(request.getHeight());
        healthDetails.setWaistCircuit(request.getWaistCircuit());
        healthDetails.setWaistCircumference(request.getWaistCircumference());
        healthDetails.setArmCircumference(request.getArmCircumference());
        healthDetails.setLatestUpdatedTime(LocalDateTime.now());
        healthDetails.setThighCircumference(request.getThighCircumference());
    }

    @Transactional
    @Override
    public void setCurrentTrainingPlan(long userId, long trainingPlanId) {
        AppUser appUser = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %s not found", userId)));
        Optional<TrainingPlanDetailsDto> trainingPlan =  Optional.ofNullable(trainingPlanConfigurationService.getTrainingPlan(trainingPlanId));
        if (trainingPlan.isPresent()) {
            if (trainingPlan.get().getPlanUsersIds().contains(userId)){
                appUser.setCurrentTrainingPlanId(trainingPlanId);
                appUser.setTrainingDays(trainingPlan.get().getTrainingDays());
            } else throw new IllegalStateException("Training plan not found");
        }
        else
            throw new IllegalStateException("Training plan not found");
    }

    @Transactional(readOnly = true)
    @Override
    public TrainingPlanDetailsDto getCurrentTrainingPlan(long userId) {
        AppUser appUser = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %s not found", userId)));

        if(appUser.getCurrentTrainingPlanId() != null){
            return trainingPlanConfigurationService.getTrainingPlan(appUser.getCurrentTrainingPlanId());
        } else {
            return TrainingPlanDetailsDto.builder().id(0).build();
        }
    }

    @Transactional
    @Override
    public void saveUserPhoto(long userId, AddUserPhotoRequest file) {
        AppUser appUser = userRepository.findUserById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id %s not found", userId)));
        AppUserPhoto appUserPhoto = AppUserPhoto.builder()
                .name(file.getFileName())
                .type(file.getContentType())
                .uploadedTime(LocalDateTime.now())
                .imageData(file.getFile())
                .build();
        appUserPhoto.linkWithUser(appUser);
        appUser.addPhoto(appUserPhoto);
    }

    @Transactional(readOnly = true)
    @Override
    public AppUserPhotoDto getUserPhotoDetails(long photoId) {
        AppUserPhoto photo = userRepository.findUserPhoto(photoId).orElseThrow(
                () -> new UserNotFoundException(String.format("Photo with id %s not found", photoId)));
        return new AppUserPhotoDto(photoId, photo.getName(),
                photo.getUploadedTime(),
                photo.getType(),
                ImageCompressor.decompressImage(photo.getImageData()));
    }

    @Transactional
    @Override
    public void deleteUserPhoto(long userPhotoId) {
        userRepository.deleteUserPhoto(userPhotoId);
    }

    @Transactional
    @Override
    public void findUsersWithUpdateRequiredAndSendNotification() {
        userRepository.getAllUsers().stream().filter(user -> Optional.ofNullable(user.getHealthDetails().getLatestUpdatedTime())
                        .orElseGet(() -> LocalDateTime.now().minusYears(1L))
                        .toLocalDate()
                        .isAfter(LocalDate.now().minusDays(14)))
                .filter(AppUser::isNotificationsEnabled)
                .forEach(user -> {
                    String latestUpdateProfileDate = user.getHealthDetails().getLatestUpdatedTime().toLocalDate().toString();
                    notificationRepository.sendNotification(new Notification(NotificationType.PROFILE_UPDATE_REMINDER, user.getId(), user.getEmail(),
                            "<h1> Drogi użytkowniku </h1> <br> <p> Nie aktualizowałeś swoich danych od "+latestUpdateProfileDate+" dni. Zaktualizuj je w panelu użytkownika. </p> " +
                                    "<br /> Zespół WorkoutHelper"));
                });
    }

    @Transactional
    @Override
    public void findUsersWithTrainingTodayAndSendReminderNotification() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        TrainingDay today = TrainingDay.values()[dayOfWeek-1];

        userRepository.getAllUsers().stream()
                .filter(user -> user.getTrainingDays().contains(today))
                .filter(AppUser::isNotificationsEnabled)
                .forEach(user -> {
                    notificationRepository.sendNotification(new Notification(NotificationType.TRAINING_TODAY_REMINDER, user.getId(), user.getEmail(),
                            "<h1> Drogi użytkowniku </h1> <br> <p> Pamiętaj, że dzisiaj masz trening !" +
                                    "<br /> Zespół WorkoutHelper"));
                });
    }

    private UserExtendedDetailsDto mapUserDetails(AppUser appUser){
        return new UserExtendedDetailsDto(appUser.getId(), appUser.getUsername(), appUser.getEmail(), appUser.getGender(),
                appUser.getRegistrationDateTime(), appUser.getDateOfBirth(), Optional.ofNullable(appUser.getCurrentTrainingPlanId()),
                Optional.ofNullable(appUser.getHealthDetails()).isEmpty() ? new HealthDetailsDto() :
                        getHealthDetails(appUser),
                Optional.ofNullable(appUser.getHealthDetails()).isEmpty() ? Collections.emptyList() :
                        appUser.getUserPhotos().stream().map(photo -> new AppUserPhotoDto(photo.getId(), photo.getName(),
                                photo.getUploadedTime(), photo.getType(), ImageCompressor.decompressImage(photo.getImageData()))).toList(),
                appUser.isNotificationsEnabled());
    }

    private HealthDetailsDto getHealthDetails(AppUser appUser){
        return appUser.getHealthDetails() == null ? new HealthDetailsDto() :
                new HealthDetailsDto(
                        appUser.getHealthDetails().getWeight(),
                        appUser.getHealthDetails().getHeight(),
                        appUser.getHealthDetails().getWaistCircuit(),
                        appUser.getHealthDetails().getWaistCircumference(),
                        appUser.getHealthDetails().getArmCircumference(),
                        appUser.getHealthDetails().getThighCircumference(),
                        appUser.getHealthDetails().getLatestUpdatedTime(),
                        appUser.getTrainingStartDate());
    }



    private AppUser createNewUser(CreateUserRequest createUserRequest){
        return AppUser.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .registrationDateTime(LocalDateTime.now())
                .email(createUserRequest.getEmail())
                .dateOfBirth(createUserRequest.getDateOfBirth())
                .trainingStartDate(createUserRequest.getTrainingStartDate())
                .gender(createUserRequest.getGender())
                .notificationsEnabled(true)
                .userAuthority(UserAuthority.USER_AUTHORITY)
                .build();
    }

    private AppUserHealthDetails createNewUserHealthDetails(){
    return AppUserHealthDetails.builder()
            .height(0)
            .weight(0)
            .thighCircumference(0)
            .waistCircumference(0)
            .waistCircuit(0)
            .armCircumference(0)
            .latestUpdatedTime(LocalDateTime.now())
            .build();
    }
}
