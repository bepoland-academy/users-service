package pl.betse.beontime.entity;

import lombok.*;
import pl.betse.beontime.model.enums.RoleEnum;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private int id;

    @Column(nullable = false, unique = true)
    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> userEntities;

    public RoleEntity(RoleEnum roleEnum) {
        this.role = roleEnum.name();
    }






    /*ADMINISTRATION,
    CONSULTANT,
    MANAGER*/
}
