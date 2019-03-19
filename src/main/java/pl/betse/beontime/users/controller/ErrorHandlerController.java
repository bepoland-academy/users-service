package pl.betse.beontime.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.betse.beontime.users.exception.*;
import pl.betse.beontime.users.model.ErrorResponse;

import java.text.MessageFormat;
import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> sendUserNotFoundMessage(UserNotFoundException e) {
        String message = MessageFormat.format("USER WITH GUID {0} NOT FOUND.", e.getGuid());
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<ErrorResponse> sendUserExist() {
        return new ResponseEntity<>(new ErrorResponse("USER ALREADY EXISTS"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserBadCredentialException.class})
    public ResponseEntity<ErrorResponse> sendBadCredentialLogin(UserBadCredentialException e) {
        return new ResponseEntity<>(new ErrorResponse("MAIL OR PASSWORD IS WRONG."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    public ResponseEntity<ErrorResponse> sendRoleDoesNotExist() {
        return new ResponseEntity<>(new ErrorResponse("ROLE DOES NOT EXIST"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DepartmentNotFoundException.class})
    public ResponseEntity<ErrorResponse> sendDepartmentDoesNotExist(DepartmentNotFoundException e) {
        String message = MessageFormat.format("Department {0} does not exist", e.getDepartmentName());
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PasswordTokenNotFoundException.class})
    public ResponseEntity<ErrorResponse> sendPasswordTokenNotFound() {
        log.error("Change password token doesn't exist.");
        return new ResponseEntity<>(new ErrorResponse("SET PASSWORD TOKEN DOES NOT EXIST."), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<ErrorResponse> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse(error.getDefaultMessage()));
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
