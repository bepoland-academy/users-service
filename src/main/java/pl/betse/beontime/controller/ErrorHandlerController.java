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
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "USER NOT FOUND."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserExistException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserExist() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "USER EXISTS IN DATABASE."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserBadCredentialException.class})
    public @ResponseBody
    ResponseEntity<?> sendBadCredentialLogin() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "MAIL OR PASSWORD IS WRONG."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendRoleDoesNotExist() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "ROLE DOES NOT EXIST"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DepartmentNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendDepartmentDoesNotExist() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "DEPARTMENT DOES NOT EXIST."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmptyUserListException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserListIsEmpty() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "EMPTY USER LIST"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmptyRoleListException.class})
    public @ResponseBody
    ResponseEntity<?> sendRoleListIsEmpty() {
        return new ResponseEntity<>(new CustomResponseMessage(HttpStatus.BAD_REQUEST, "EMPTY ROLE LIST"), HttpStatus.BAD_REQUEST);
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
