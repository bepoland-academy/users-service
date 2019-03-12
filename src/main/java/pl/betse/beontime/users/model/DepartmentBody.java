package pl.betse.beontime.users.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DepartmentBody extends ResourceSupport {
    private String departmentId;
    private String name;
}
