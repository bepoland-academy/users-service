package pl.betse.beontime.users.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"roles"})
@Builder
@Entity
@Table(name = "USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "USER_GUID", nullable = false, unique = true)
    private String guid;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ACTIVE")
    private boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DEPARTMENT_ID")
    private DepartmentEntity department;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userEntity")
    PasswordTokenEntity passwordTokenEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<RoleEntity> roles;

}

