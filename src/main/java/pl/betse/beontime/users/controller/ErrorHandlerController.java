package pl.betse.beontime.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.betse.beontime.users.exception.*;
import pl.betse.beontime.users.model.ErrorResponse;

import java.text.MessageFormat;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> sendUserNotFoundMessage(UserNotFoundException e) {
        String message = MessageFormat.format("USER WITH GUID {0} NOT FOUND.", e.getGuid());
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyExistException.class})
    public ResponseEntity<?> sendUserExist() {
        return new ResponseEntity<>(new ErrorResponse("USER ALREADY EXISTS"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserBadCredentialException.class})
    public ResponseEntity<?> sendBadCredentialLogin(UserBadCredentialException e) {
        return new ResponseEntity<>(new ErrorResponse("MAIL OR PASSWORD IS WRONG."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    public ResponseEntity<?> sendRoleDoesNotExist() {
        return new ResponseEntity<>(new ErrorResponse("ROLE DOES NOT EXIST"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DepartmentNotFoundException.class})
    public ResponseEntity<?> sendDepartmentDoesNotExist(DepartmentNotFoundException e) {
        String message = MessageFormat.format("Department {0} doesn't exist", e.getDepartmentName());
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PasswordTokenNotFoundException.class})
    public ResponseEntity<?> sendPasswordTokenNotFound() {
        log.error("Change password token doesn't exist.");
        return new ResponseEntity<>(new ErrorResponse("SET PASSWORD TOKEN DOES NOT EXIST."), HttpStatus.NOT_FOUND);
    }
}
