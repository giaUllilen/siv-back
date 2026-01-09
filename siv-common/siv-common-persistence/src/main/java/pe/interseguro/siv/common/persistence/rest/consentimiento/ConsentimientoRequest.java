package pe.interseguro.siv.common.persistence.rest.consentimiento;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsentimientoRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String documentType;
    private String documentNumber;
    private AdditionalInfo additionalInfo;
    private String business;
    private String channel;
    private List<ConsentDetail> consents;
}
