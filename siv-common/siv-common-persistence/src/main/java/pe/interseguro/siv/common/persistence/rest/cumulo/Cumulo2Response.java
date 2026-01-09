package pe.interseguro.siv.common.persistence.rest.cumulo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cumulo2Response {
    private String tptId;
    private String tipoDocumento;
    private String numeroDocumento;
    private Double cumuloDolar;
    private Double cumuloSoles;
}