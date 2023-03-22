package pm.workout.helper.domain;

import pm.workout.helper.domain.exception.DomainError;

public class InvalidInputException extends IllegalStateException {
    private DomainError domainError;

    public InvalidInputException(String s, DomainError domainError) {
        super(s);
        this.domainError = domainError;
    }

    public DomainError getDomainError() {
        return domainError;
    }
}
