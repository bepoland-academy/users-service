package pl.betse.beontime.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentBo {
    private String id;
    private String name;
}
