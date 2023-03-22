package pm.workout.helper.domain;

import pm.workout.helper.domain.exception.DomainError;

public abstract class NotFoundException extends RuntimeException {
    private DomainError domainError;

    public NotFoundException(String message, DomainError domainError) {
        super(message);
        this.domainError = domainError;
    }

    public DomainError getDomainError() {
        return domainError;
    }
}
