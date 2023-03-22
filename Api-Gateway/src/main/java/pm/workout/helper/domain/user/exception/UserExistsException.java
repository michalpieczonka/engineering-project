package pm.workout.helper.domain.user.exception;

import pm.workout.helper.domain.InvalidInputException;
import pm.workout.helper.domain.exception.DomainError;

public class UserExistsException extends InvalidInputException {
    public UserExistsException(String message) {
        super(message, DomainError.USER_EXISTS_ERROR);
    }
}
