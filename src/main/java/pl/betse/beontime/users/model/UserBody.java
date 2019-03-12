package pl.betse.beontime.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;
import pl.betse.beontime.users.validation.CreateUserValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserBody extends ResourceSupport {

    @JsonIgnore
    private String userId;

    @NotNull(groups = {CreateUserValidation.class}, message = "Email can't be null")
    @NotEmpty(groups = {CreateUserValidation.class}, message = "Email can't be empty")
    @Email(groups = {CreateUserValidation.class}, message = "Email should have pattern address@domain.com")
    private String email;

    @NotNull(groups = {CreateUserValidation.class}, message = "First name can't be null")
    @NotEmpty(groups = {CreateUserValidation.class}, message = "First name can't be empty")
    private String firstName;

    @NotNull(groups = {CreateUserValidation.class}, message = "Last name can't be null")
    @NotEmpty(groups = {CreateUserValidation.class}, message = "Last name can't be empty")
    private String lastName;

    private boolean active;

    @NotNull(groups = {CreateUserValidation.class}, message = "Department can't be null")
    @NotEmpty(groups = {CreateUserValidation.class}, message = "Department can't be empty")
    private String department;

    private List<String> roles = new ArrayList<>();

}
