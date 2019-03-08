package pl.betse.beontime.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.betse.beontime.model.exception.*;
import pl.betse.beontime.utils.ErrorResponse;

import java.util.ArrayList;

@RestControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserNotFoundMessage() {
        return new ResponseEntity<>(new ErrorResponse("USER NOT FOUND."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserExistException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserExist() {
        return new ResponseEntity<>(new ErrorResponse("USER ALREADY EXISTS"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserBadCredentialException.class})
    public @ResponseBody
    ResponseEntity<?> sendBadCredentialLogin() {
        return new ResponseEntity<>(new ErrorResponse("MAIL OR PASSWORD IS WRONG."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RoleNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendRoleDoesNotExist() {
        return new ResponseEntity<>(new ErrorResponse("ROLE DOES NOT EXIST"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DepartmentNotFoundException.class})
    public @ResponseBody
    ResponseEntity<?> sendDepartmentDoesNotExist() {
        return new ResponseEntity<>(new ErrorResponse("DEPARTMENT DOES NOT EXIST."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmptyUserListException.class})
    public @ResponseBody
    ResponseEntity<?> sendUserListIsEmpty() {
        return new ResponseEntity<>(new ErrorResponse("EMPTY USER LIST"), HttpStatus.OK);
    }

    @ExceptionHandler({EmptyRoleListException.class})
    public @ResponseBody
    ResponseEntity<?> sendRoleListIsEmpty() {
        return new ResponseEntity<>(new ErrorResponse("EMPTY ROLE LIST"), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ErrorResponse(errors.toString()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are: ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
        return new ResponseEntity<>(new ErrorResponse(builder.toString()), HttpStatus.METHOD_NOT_ALLOWED);
    }

}
