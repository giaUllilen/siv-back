//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 12:05:51 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ViaCobro.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="ViaCobro"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="INTERBANK"/&gt;
 *     &lt;enumeration value="INTERSEGURO"/&gt;
 *     &lt;enumeration value="BCP"/&gt;
 *     &lt;enumeration value="SCOTIABANK"/&gt;
 *     &lt;enumeration value="BBVA"/&gt;
 *     &lt;enumeration value="BLATINO"/&gt;
 *     &lt;enumeration value="BSUDAMERICANO"/&gt;
 *     &lt;enumeration value="VISA"/&gt;
 *     &lt;enumeration value="AMEX"/&gt;
 *     &lt;enumeration value="MASTERCARD"/&gt;
 *     &lt;enumeration value="DINERS"/&gt;
 *     &lt;enumeration value="WIESE"/&gt;
 *     &lt;enumeration value="CAJA_VENTANILLA"/&gt;
 *     &lt;enumeration value="DCTO_RRHH"/&gt;
 *     &lt;enumeration value="TODOS"/&gt;
 *     &lt;enumeration value="VEA_VISA"/&gt;
 *     &lt;enumeration value="CHEQUE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ViaCobro", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums")
@XmlEnum
public enum ViaCobro {

    INTERBANK,
    INTERSEGURO,
    BCP,
    SCOTIABANK,
    BBVA,
    BLATINO,
    BSUDAMERICANO,
    VISA,
    AMEX,
    MASTERCARD,
    DINERS,
    WIESE,
    CAJA_VENTANILLA,
    DCTO_RRHH,
    TODOS,
    VEA_VISA,
    CHEQUE;

    public String value() {
        return name();
    }

    public static ViaCobro fromValue(String v) {
        return valueOf(v);
    }

}
