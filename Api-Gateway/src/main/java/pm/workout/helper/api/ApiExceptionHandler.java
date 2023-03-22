package pm.workout.helper.api;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pm.workout.helper.domain.InvalidInputException;
import pm.workout.helper.domain.NotFoundException;
import pm.workout.helper.domain.exception.DomainError;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public ApiException handleNotFoundException(NotFoundException e){
        return new ApiException(e.getMessage(), e.getDomainError(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidInputException.class)
    public ApiException handleInvalidInputException(InvalidInputException e){
        return new ApiException(e.getMessage(), e.getDomainError(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiException handleInvalidRequestArgumentException(MethodArgumentNotValidException e){
        return new ApiException(e.getMessage(), DomainError.INVALID_REQUEST_ERROR, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.BadRequest.class)
    public ApiException handleInvalidRequestException(FeignException.BadRequest e){
        return new ApiException(e.getMessage(), DomainError.INVALID_REQUEST_ERROR, HttpStatus.BAD_REQUEST);
    }

}
