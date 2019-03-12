package pl.betse.beontime.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordBody {
    private String token;
    private String password;
}
