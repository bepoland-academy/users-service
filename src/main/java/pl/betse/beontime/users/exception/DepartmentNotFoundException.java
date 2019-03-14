package pl.betse.beontime.users.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DepartmentNotFoundException extends RuntimeException {

    @Getter
    private String departmentName;

    public DepartmentNotFoundException(String departmentName) {
        this.departmentName = departmentName;
    }
}
