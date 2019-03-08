package pl.betse.beontime.utils;


import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}
