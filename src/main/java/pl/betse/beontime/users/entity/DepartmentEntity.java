package pl.betse.beontime.users.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "DEPARTMENT_GUID", nullable = false, unique = true)
    private String guid;

    @Column(name = "DEPARTMENT_NAME", nullable = false, unique = true)
    private String name;
}
