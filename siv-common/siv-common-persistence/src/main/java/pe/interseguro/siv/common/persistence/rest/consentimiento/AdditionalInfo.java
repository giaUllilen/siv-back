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
public class AdditionalInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String firstName;
    private String paternalLastName;
    private String maternalLastName;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String gender;
}
