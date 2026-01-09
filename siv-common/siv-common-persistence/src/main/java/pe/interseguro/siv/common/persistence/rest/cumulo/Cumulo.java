package pe.interseguro.siv.common.persistence.rest.cumulo;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.MontoDetalleResponse;

import java.util.List;

@Getter
@Setter
public class Cumulo {
    private int idTercero;
    private List<MontoDetalleResponse> montosdet;
}
