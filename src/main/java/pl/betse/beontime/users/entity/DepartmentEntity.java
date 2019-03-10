package pl.betse.beontime.users.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "DEPARTMENT_GUID", nullable = false, unique = true)
    private String guid;


    @Column(name = "NAME", nullable = false, unique = true)
    private String name;



    /* DIGITAL,
    SALESFORCE,
    BANKING*/
}