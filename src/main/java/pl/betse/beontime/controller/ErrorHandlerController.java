package pl.betse.beontime.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.betse.beontime.model.custom_exceptions.*;
import pl.betse.beontime.utils.CustomResponseMessage;

@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserNotFoundMessage() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.NOT_FOUND, "USER NOT FOUND."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserExistException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserExist() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.CONFLICT, "USER ALREADY EXISTS"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserBadCredentialException.class})
    public @ResponseBody
    ResponseEntity<?> sendBadCredentialLogin() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "MAIL OR PASSWORD IS WRONG."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendRoleDoesNotExist() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.NOT_FOUND, "ROLE DOES NOT EXIST"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DepartmentNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendDepartmentDoesNotExist() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.NOT_FOUND, "DEPARTMENT DOES NOT EXIST."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmptyUserListException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserListIsEmpty() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.NOT_FOUND, "EMPTY USER LIST"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmptyRoleListException.class})
    public @ResponseBody
    ResponseEntity<?> sendRoleListIsEmpty() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.NOT_FOUND, "EMPTY ROLE LIST"), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
