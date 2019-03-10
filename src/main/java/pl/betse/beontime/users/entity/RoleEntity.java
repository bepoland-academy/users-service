package pl.betse.beontime.users.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<UserEntity> userEntities;

    /*ADMINISTRATION,
    CONSULTANT,
    MANAGER*/
}
