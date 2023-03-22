package pm.workout.helper.domain.user.authentication.exception;

import pm.workout.helper.domain.NotFoundException;
import pm.workout.helper.domain.exception.DomainError;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message, DomainError.USER_NOT_EXISTS_ERROR);
    }
}
