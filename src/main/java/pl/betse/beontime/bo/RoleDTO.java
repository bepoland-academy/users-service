package pl.betse.beontime.bo;

import lombok.*;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private int id;

    private String role;

    private List<UserDTO> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDTO)) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return getId() == roleDTO.getId() &&
                getRole().equals(roleDTO.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRole());
    }
}
