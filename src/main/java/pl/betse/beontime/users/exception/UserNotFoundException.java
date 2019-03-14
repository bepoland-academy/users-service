package pl.betse.beontime.users.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {

    @Getter
    private String guid;

    public UserNotFoundException(String guid) {
        this.guid = guid;
    }
}
