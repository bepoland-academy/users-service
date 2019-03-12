package pl.betse.beontime.users.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserBadCredentialException extends RuntimeException {

    @Getter
    private String email;

    public UserBadCredentialException(String email) {
        this.email = email;
    }
}
