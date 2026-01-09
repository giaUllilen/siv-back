package pe.interseguro.siv.common.persistence.rest.interseguro.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LinkPagoResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String codigoRespuesta;
    private String token;
    private String mensajeRespuesta;
    private String link;

}