package pm.workout.helper.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@ToString
public class ApiException {
    private final String message;
    private final HttpStatus httpStatus;
}
