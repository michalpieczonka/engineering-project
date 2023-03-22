package pm.workout.helper.infrastructure.user.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pm.workout.helper.domain.user.AppUser;
import pm.workout.helper.domain.user.UserRepository;
import pm.workout.helper.domain.user.health.AppUserPhoto;
import pm.workout.helper.infrastructure.user.repository.health_details.AppUserHealthDetailsDbRepository;
import pm.workout.helper.infrastructure.user.repository.health_details.AppUserPhotoDbRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserDbRepository userDbRepository;
    private final AppUserPhotoDbRepository appUserPhotoDbRepository;
    private final AppUserHealthDetailsDbRepository appUserHealthDetailsDbRepository;

    @Override
    public Optional<AppUser> findUserByUsername(String username) {
        return userDbRepository.findAppUserByUsername(username);
    }

    @Override
    public void saveUser(AppUser user) {
        userDbRepository.save(user);
    }
    @Override
    public Optional<AppUser> findUserByEmail(String email) {
        return userDbRepository.findAppUserByEmail(email);
    }
    @Override
    public Optional<AppUser> findUserById(long id) {
        return userDbRepository.findById(id);
    }

    @Override
    public void saveUserPhoto(AppUserPhoto photo) {
        appUserPhotoDbRepository.save(photo);
    }

    @Override
    public Optional<AppUserPhoto> findUserPhoto(long appUserPhotoId) {
        return appUserPhotoDbRepository.findById(appUserPhotoId);
    }

    @Override
    public void deleteUserPhoto(long userPhotoId) {
        appUserPhotoDbRepository.deleteById(userPhotoId);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userDbRepository.findAll();
    }


}
