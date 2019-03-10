package pl.betse.beontime.users.bo;

import lombok.*;
import pl.betse.beontime.users.validation.CreateUserValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserBo {

    private String userId;

    @NotNull(groups = {CreateUserValidation.class}, message = "Email can't be null")
    @NotEmpty(groups = {CreateUserValidation.class}, message = "Email can't be empty")
    @Email(groups = {CreateUserValidation.class}, message = "Email should have pattern address@domain.com")
    private String email;

    private String firstName;

    private String lastName;

    private boolean active;

    @NotNull(groups = {CreateUserValidation.class}, message = "Department can't be null")
    @NotEmpty(groups = {CreateUserValidation.class}, message = "Department can't be empty")
    private String department;

    private List<String> roles;
}
