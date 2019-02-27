package pl.betse.beontime.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.betse.beontime.model.enums.DepartmentEnum;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DEPARTMENTS")
public class DepartmentEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPARTMENT_ID")
    private int id;

     public DepartmentEntity(DepartmentEnum name) {
        this.name = name.name();
    }


    @Column(nullable = false,unique = true)
    private String name;





    /* DIGITAL,
    SALESFORCE,
    BANKING*/
}
