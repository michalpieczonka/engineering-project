package pm.workout.helper.infrastructure.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pm.workout.helper.domain.user.AppUser;

import java.util.Optional;

public interface UserDbRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUsername(String login);
    Optional<AppUser> findAppUserByEmail(String email);
}
