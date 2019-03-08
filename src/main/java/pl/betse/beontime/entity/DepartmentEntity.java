package pl.betse.beontime.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID")
    private int id;

    @Column(name = "DEPARTMENT_GUID")
    private String guid;


    @Column(name = "NAME", nullable = false, unique = true)
    private String name;



    /* DIGITAL,
    SALESFORCE,
    BANKING*/
}
