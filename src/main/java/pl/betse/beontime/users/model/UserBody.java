package pl.betse.beontime.users.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserBody extends ResourceSupport {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private String department;
    private List<String> roles;

}
