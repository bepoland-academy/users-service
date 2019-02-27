package pl.betse.beontime.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import pl.betse.beontime.model.validation.CreateUserValidation;
import pl.betse.beontime.model.validation.LoginUserValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    private Integer userId;

    @NotNull(groups = {CreateUserValidation.class, LoginUserValidation.class})
    @NotEmpty(groups = {CreateUserValidation.class, LoginUserValidation.class})
    private String emailLogin;

    private String firstName;

    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(groups = {LoginUserValidation.class})
    @NotEmpty(groups = {LoginUserValidation.class})
    private String password;

    @JsonProperty("active")
    private boolean isActive;

    @NotNull(groups = {CreateUserValidation.class})
    @NotEmpty(groups = {CreateUserValidation.class})
    private String department;

    private Set<String> roles;


}
