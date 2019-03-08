package pl.betse.beontime.bo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import pl.betse.beontime.model.validation.CreateUserValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@ToString
public class UserBo {

    private String id;

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
