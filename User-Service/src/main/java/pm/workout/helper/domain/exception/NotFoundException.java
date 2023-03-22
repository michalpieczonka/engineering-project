package pm.workout.helper.domain.exception;

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
