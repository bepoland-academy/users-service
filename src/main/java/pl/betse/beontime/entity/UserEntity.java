package pl.betse.beontime.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String emailLogin;

    private String firstName;

    private String lastName;


    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonProperty("active")
    private boolean isActive;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DEPARTMENT_ID")
    @JsonIgnore
    private DepartmentEntity departmentEntity;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<RoleEntity> roles;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return getUserId().equals(that.getUserId()) &&
                getEmailLogin().equals(that.getEmailLogin()) &&
                Objects.equals(getFirstName(), that.getFirstName()) &&
                Objects.equals(getLastName(), that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getEmailLogin(), getFirstName(), getLastName());
    }
}

