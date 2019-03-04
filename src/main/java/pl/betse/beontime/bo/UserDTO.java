package pl.betse.beontime.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.betse.beontime.model.validation.CreateUserValidation;
import pl.betse.beontime.model.validation.LoginUserValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonIgnore
    private Integer userId;

    @JsonProperty("userId")
    private String userGUID;

    @NotNull(groups = {CreateUserValidation.class, LoginUserValidation.class})
    @NotEmpty(groups = {CreateUserValidation.class, LoginUserValidation.class})
    @Email(groups = {CreateUserValidation.class, LoginUserValidation.class})
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return getUserId().equals(userDTO.getUserId()) &&
                getEmailLogin().equals(userDTO.getEmailLogin()) &&
                getFirstName().equals(userDTO.getFirstName()) &&
                getLastName().equals(userDTO.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmailLogin(), getFirstName(), getLastName());
    }
}
