package pl.betse.beontime.bo;

import lombok.*;
import pl.betse.beontime.model.validation.CreateUserValidation;
import pl.betse.beontime.model.validation.LoginUserValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;

    @NotNull(groups = {CreateUserValidation.class, LoginUserValidation.class})
    @NotEmpty(groups = {CreateUserValidation.class, LoginUserValidation.class})
    @Email(groups = {CreateUserValidation.class, LoginUserValidation.class})
    private String email;

    private String firstName;

    private String lastName;

    private boolean active;

    @NotNull(groups = {CreateUserValidation.class})
    @NotEmpty(groups = {CreateUserValidation.class})
    private String department;

    private List<String> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return getUserId().equals(userDTO.getUserId()) &&
                getEmail().equals(userDTO.getEmail()) &&
                getFirstName().equals(userDTO.getFirstName()) &&
                getLastName().equals(userDTO.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmail(), getFirstName(), getLastName());
    }
}
