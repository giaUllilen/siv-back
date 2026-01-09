package pe.interseguro.siv.common.persistence.rest.cumulo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CumuloResponse {
    private int status;
    private String code;
    private String title;
    private String message;
    private Data data;
}