package pl.betse.beontime.users.bo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserBo {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private String department;
    private List<String> roles;
}
