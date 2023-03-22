package pm.workout.helper.domain.user;

import pm.workout.helper.domain.user.health.AppUserPhoto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<AppUser> findUserByUsername(String login);
    Optional<AppUser> findUserByEmail(String email);
    void saveUser(AppUser user);
    Optional<AppUser> findUserById(long userId);
    void saveUserPhoto(AppUserPhoto photo);
    Optional<AppUserPhoto> findUserPhoto(long userId);
    void deleteUserPhoto(long userPhotoId);
    List<AppUser> getAllUsers();
}
