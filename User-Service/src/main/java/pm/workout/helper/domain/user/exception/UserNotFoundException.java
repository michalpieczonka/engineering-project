package pm.workout.helper.domain.user.exception;

import org.springframework.data.crossstore.ChangeSetPersister;
import pm.workout.helper.domain.exception.DomainError;
import pm.workout.helper.domain.exception.NotFoundException;

public class UserNotFoundException  extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message, DomainError.USER_NOT_EXISTS_ERROR);
    }
}