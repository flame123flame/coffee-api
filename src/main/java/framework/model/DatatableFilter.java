package framework.model;

import lombok.Data;

@Data
public class DatatableFilter {
    private String column;
    private String value;
    private String op;
}
