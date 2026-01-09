package pe.interseguro.siv.common.persistence.rest.consentimiento;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsentDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String policy;
    private String subtype;
    private String action;
    private String product;
    private String policyDocumentUrl;
    private String channelSubtype;
}
