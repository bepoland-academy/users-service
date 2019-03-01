package pl.betse.beontime.utils;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponseMessage {

    private int statCode;
    private HttpStatus stat;
    private String message;

    public CustomResponseMessage(HttpStatus stat, String message) {
        this.statCode = stat.value();
        this.stat = stat;
        this.message = message;
    }

}
