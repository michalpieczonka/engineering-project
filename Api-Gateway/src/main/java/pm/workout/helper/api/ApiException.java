package pm.workout.helper.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import pm.workout.helper.domain.exception.DomainError;

@AllArgsConstructor
@Getter
@ToString
public class ApiException {
    private final String message;
    private final DomainError errorCode;
    private final HttpStatus httpStatus;
}
