package pm.workout.helper.domain.user

import org.springframework.security.crypto.password.PasswordEncoder
import pm.workout.helper.api.authentication.request.CreateUserRequest
import pm.workout.helper.api.user.request.AddUserPhotoRequest
import pm.workout.helper.api.user.request.UpdateUserDetailsRequest
import pm.workout.helper.api.user.request.UpdateUserHealthDetailsRequest
import pm.workout.helper.domain.notification.Notification
import pm.workout.helper.domain.notification.NotificationRepository
import pm.workout.helper.domain.notification.NotificationType
import pm.workout.helper.domain.training.plan.TrainingDay
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto
import pm.workout.helper.domain.user.exception.UserNotFoundException
import pm.workout.helper.domain.user.health.AppUserHealthDetails
import pm.workout.helper.domain.user.health.AppUserPhoto
import pm.workout.helper.infrastructure.services.TrainingPlanConfigurationService
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Function
import java.util.stream.Collectors

class UserServiceImplTest extends Specification {
    def "when trying to create user and user with given email and username not exists should save new user"() {
        given:
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def userRepository = new UserRepositoryMock()
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def createUserRequest = new CreateUserRequest(USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_BIRTHDAY, TRAINING_START_DAY, USER_GENDER)

        when:
        userService.createUser(createUserRequest)

        then:
        userRepository.getAllUsers().size() == 1
        userRepository.getAllUsers().get(0).getUsername() == USER_USERNAME
        userRepository.getAllUsers().get(0).getEmail() == USER_EMAIL
        userRepository.getAllUsers().get(0).getDateOfBirth() == USER_BIRTHDAY
    }

    def "when trying to create user and user with given email or username exists should throw"() {
        given:
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def createUserRequest = new CreateUserRequest(USER_USERNAME, USER_PASSWORD, USER_EMAIL, USER_BIRTHDAY, TRAINING_START_DAY, USER_GENDER)

        when:
        userService.createUser(createUserRequest)

        then:
        thrown IllegalStateException
    }

    def "when trying to get user by login and user exists should return user"() {
        given:
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        def result = userService.getUserByLogin(USER_USERNAME)

        then:
        result.authority == UserAuthority.USER_AUTHORITY
        result.username == USER_USERNAME
        result.email == USER_EMAIL
        result.userId != null
    }

    def "when trying to get user by login and user not exists should throw"() {
        given:
        final String USER_USERNAME = "username"
        def userRepository = new UserRepositoryMock(List.of())
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        userService.getUserByLogin(USER_USERNAME)

        then:
        thrown UserNotFoundException
    }

    def "when trying to update user details, and user exists should update all required details"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def request = new UpdateUserDetailsRequest("jasiu", "email@o2.pl", Gender.MALE, LocalDate.parse("2022-07-12"), LocalDate.parse("2021-07-12"), true )

        when:
        userService.updateUserDetails(USER_ID, request)

        then:
        def updatedUser = userRepository.getAllUsers().get(0)
        updatedUser.getUsername() == "jasiu"
        updatedUser.email == "email@o2.pl"
        updatedUser.gender  == Gender.MALE
        updatedUser.dateOfBirth == LocalDate.parse("2022-07-12")
        updatedUser.trainingStartDate == LocalDate.parse("2021-07-12")
        updatedUser.isNotificationsEnabled()


    }

    def "when trying to update user details, and user exists but email in update request is taken should throw"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        def existingUser = buildAppUser("valid", "email@o2.pl", 2L)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(List.of(appUser, existingUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def request = new UpdateUserDetailsRequest("valid", "email@o2.pl", Gender.MALE, LocalDate.parse("2022-07-12"), LocalDate.parse("2021-07-12"), true )

        when:
        userService.updateUserDetails(USER_ID, request)

        then:
        thrown IllegalStateException
    }

    def "when trying to get all user details, should return all details with valid mapped class"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        when:
        def response = userService.getAllUserDetails(USER_ID)

        then:
        response.userId == USER_ID
        response.username == USER_USERNAME
        response.email == USER_EMAIL
        response.gender == Gender.MALE
        response.notificationsEnabled
        response.healthDetails.height == 30
        response.healthDetails.weight == 10
        response.currentTrainingPlanId.get() == 1L
    }

    def "when trying trying to update user health details and user exists, should update health details"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def request = new UpdateUserHealthDetailsRequest(12.5, 35.5, 31.3, 10, 10, 10)

        when:
        userService.updateUserHealthDetails(USER_ID, request)

        then:
        def updatedUserHealthDetails = userRepository.getAllUsers().get(0).getHealthDetails()
        updatedUserHealthDetails.height == 35.5
        updatedUserHealthDetails.weight == 12.5
        updatedUserHealthDetails.waistCircuit == 31.3
        updatedUserHealthDetails.waistCircumference == 10
        updatedUserHealthDetails.armCircumference == 10
        updatedUserHealthDetails.thighCircumference == 10
    }

    def "when trying trying to update user health details and user not exists should throw"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def request = new UpdateUserHealthDetailsRequest(12.5, 35.5, 31.3, 10, 10, 10)

        when:
        userService.updateUserHealthDetails(5L, request)

        then:
        thrown UserNotFoundException
    }

    def "when trying to update user training plan and training plan and user exists should update training plan id"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
       TrainingPlanConfigurationService trainingPlanConfService = Stub()
        trainingPlanConfService.getTrainingPlan(5L) >> TrainingPlanDetailsDto.builder().id(5L).planUsersIds(Set.of(1L)).trainingDays(Set.of(TrainingDay.FRIDAY)).build()
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        userService.setCurrentTrainingPlan(USER_ID, 5L)

        then:
        def user = userRepository.getAllUsers().get(0)
        user.currentTrainingPlanId == 5L
        user.getTrainingDays().size() == 1
        user.getTrainingDays().contains(TrainingDay.FRIDAY)
    }

    def "when trying to update user training plan and training plan is not user plan should throw"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        TrainingPlanConfigurationService trainingPlanConfService = Stub()
        trainingPlanConfService.getTrainingPlan(5L) >> TrainingPlanDetailsDto.builder().id(5L).planUsersIds(Set.of(2L)).trainingDays(Set.of(TrainingDay.FRIDAY)).build()
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        userService.setCurrentTrainingPlan(USER_ID, 5L)

        then:
        thrown IllegalStateException
    }

    def "when trying to update user training plan and training plan not exists should throw"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        TrainingPlanConfigurationService trainingPlanConfService = Stub()
        trainingPlanConfService.getTrainingPlan(5L) >> TrainingPlanDetailsDto.builder().id(5L).planUsersIds(Set.of(2L)).trainingDays(Set.of(TrainingDay.FRIDAY)).build()
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        userService.setCurrentTrainingPlan(USER_ID, 4L)

        then:
        thrown IllegalStateException
    }

    def "when trying to save user foto and user exists should save foto"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def request = new AddUserPhotoRequest("pdf", "plik.pdf", new byte[10])

        when:
        userService.saveUserPhoto(USER_ID, request)

        then:
        def userPhotos = userRepository.getAllUsers().get(0).getUserPhotos()
        userPhotos.size() == 1
        AppUserPhoto photo = userPhotos.stream().toList().get(0)
        photo.name == "plik.pdf"
        photo.type == "pdf"
        photo.imageData.length == 10
    }

    def "when trying to save user foto and user not exists should throw"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)
        def request = new AddUserPhotoRequest("pdf", "plik.pdf", new byte[10])

        when:
        userService.saveUserPhoto(7L, request)

        then:
        thrown UserNotFoundException
    }

    def "when trying to get user photo and user photo exists should return valid data"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def appUser = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        appUser.setHealthDetails(buildHealthDetails(appUser))
        appUser.addPhoto(AppUserPhoto.builder().id(1L).name("plik.pdf").type("pdf").imageData(new byte[10]).uploadedTime(LocalDateTime.now()).build())
        def userRepository = new UserRepositoryMock(Arrays.asList(appUser))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        def result = userService.getUserPhotoDetails(1L)

        then:
        result.name == "plik.pdf"
        result.photoId == 1L
        result.type == "pdf"
        result.uploadedTime!= null
    }

    def "when trying to find users with updated required should find valid users"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def userWithRequiredUpdate = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        userWithRequiredUpdate.setHealthDetails(buildHealthDetails(userWithRequiredUpdate))
        userWithRequiredUpdate.healthDetails.latestUpdatedTime = LocalDateTime.now().minusDays(16)

        def userWithoutUpdateRequired = buildAppUser("Update not required", "email@test", 2L)
        userWithoutUpdateRequired.setHealthDetails(buildHealthDetails(userWithoutUpdateRequired))
        userWithoutUpdateRequired.healthDetails.latestUpdatedTime = LocalDateTime.now().minusDays(5)

        def userRepository = new UserRepositoryMock(Arrays.asList(userWithRequiredUpdate, userWithoutUpdateRequired))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        userService.findUsersWithUpdateRequiredAndSendNotification()

        then:
        1 * notificationRepository.sendNotification(_)
    }

    def "when trying to find users with workout reminder today should find valid users"() {
        given:
        final Long USER_ID = 1L
        final String USER_USERNAME = "username"
        final String USER_PASSWORD = "password"
        final String USER_EMAIL = "email@wp.pl"
        final LocalDate USER_BIRTHDAY = LocalDate.now()
        final LocalDate TRAINING_START_DAY = LocalDate.now()
        final Gender USER_GENDER = Gender.MALE
        def userWithRequiredUpdate = buildAppUser(USER_USERNAME, USER_EMAIL, USER_ID)
        userWithRequiredUpdate.setHealthDetails(buildHealthDetails(userWithRequiredUpdate))
        userWithRequiredUpdate.healthDetails.latestUpdatedTime = LocalDateTime.now().minusDays(16)
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        TrainingDay today = TrainingDay.values()[dayOfWeek-1]
        userWithRequiredUpdate.setTrainingDays(Set.of(today))

        def userWithoutUpdateRequired = buildAppUser("Update not required", "email@test", 2L)
        userWithoutUpdateRequired.setHealthDetails(buildHealthDetails(userWithoutUpdateRequired))
        userWithoutUpdateRequired.healthDetails.latestUpdatedTime = LocalDateTime.now().minusDays(5)

        def userRepository = new UserRepositoryMock(Arrays.asList(userWithRequiredUpdate, userWithoutUpdateRequired))
        def passwordEncoder = Mock(PasswordEncoder)
        def userMapper = Mock(UserMapper)
        def trainingPlanConfService = Mock(TrainingPlanConfigurationService)
        def notificationRepository = Mock(NotificationRepository)
        def userService = new UserServiceImpl(userRepository, passwordEncoder, userMapper, trainingPlanConfService, notificationRepository)

        when:
        userService.findUsersWithTrainingTodayAndSendReminderNotification()

        then:
        1 * notificationRepository.sendNotification(_)
    }

    private final class UserRepositoryMock implements UserRepository {
        private final Map<Long, AppUser> USERS_ID_MAP = new HashMap<>();
        private final Map<String, AppUser> USERS_LOGIN_MAP = new HashMap<>();
        private AtomicLong seq = new AtomicLong(1L)

        UserRepositoryMock() {

        }

        UserRepositoryMock(List<AppUser> usersList) {
            USERS_ID_MAP = usersList.stream().collect(Collectors.toMap(AppUser::getId, Function.identity()))
            USERS_LOGIN_MAP = usersList.stream().collect(Collectors.toMap(AppUser::getUsername, Function.identity()))
        }

        @Override
        Optional<AppUser> findUserByUsername(String login) {
            return Optional.ofNullable(USERS_LOGIN_MAP.get(login))
        }

        @Override
        Optional<AppUser> findUserByEmail(String email) {
            return Optional.ofNullable(USERS_LOGIN_MAP.get(email))
        }

        @SuppressWarnings("GroovyAccessibility")
        @Override
        void saveUser(AppUser user) {
            user.id = seq.incrementAndGet()
            USERS_ID_MAP.put(user.id, user)
            USERS_LOGIN_MAP.put(user.username, user)
        }

        @Override
        Optional<AppUser> findUserById(long userId) {
            return Optional.ofNullable(USERS_ID_MAP.get(userId))
        }

        @Override
        void saveUserPhoto(AppUserPhoto photo) {

        }

        @Override
        Optional<AppUserPhoto> findUserPhoto(long userId) {
           Optional<AppUser> appUser = USERS_ID_MAP.values().stream()
                   .filter(user -> user.getUserPhotos()
                           .stream().filter(photo -> photo.id == userId)
                           .findFirst().isPresent())
                   .findFirst()
            if (appUser.isPresent()) {
                return appUser.get().getUserPhotos().stream().filter(photo -> photo.id == userId).findFirst()
            } else
                return Optional.empty();
        }

        @Override
        void deleteUserPhoto(long userPhotoId) {

        }

        @Override
        List<AppUser> getAllUsers() {
            return USERS_ID_MAP.values().toList()
        }
    }

    private AppUserHealthDetails buildHealthDetails(AppUser appUser) {
        return AppUserHealthDetails.builder()
                .id(1L)
                .weight(10)
                .height(30)
                .appUser(appUser)
                .armCircumference(15)
                .waistCircuit(15)
                .waistCircumference(15)
                .thighCircumference(30)
                .build()
    }

    private AppUser buildAppUser(String userName, String email, Long userId = 1L){
        return AppUser.builder()
                .id(userId)
                .username(userName)
                .email(email)
        .password("test")
        .dateOfBirth(LocalDate.now())
        .registrationDateTime(LocalDateTime.now())
        .gender(Gender.MALE)
        .currentTrainingPlanId(1L)
        .userAuthority(UserAuthority.USER_AUTHORITY)
        .notificationsEnabled(true)
        .build();
    }
}
