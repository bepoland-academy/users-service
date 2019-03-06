package pl.betse.beontime.bo;

import lombok.Data;
import pl.betse.beontime.model.validation.CredentialsValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserCredentials {

    @NotNull(groups = {CredentialsValidation.class})
    @NotEmpty(groups = {CredentialsValidation.class})
    private String username;

    @NotNull(groups = {CredentialsValidation.class})
    @NotEmpty(groups = {CredentialsValidation.class})
    private String password;
}
