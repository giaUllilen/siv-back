//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 12:05:51 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para EstadoTarjeta.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="EstadoTarjeta"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Desconocido"/&gt;
 *     &lt;enumeration value="Ingresado"/&gt;
 *     &lt;enumeration value="EnProceso"/&gt;
 *     &lt;enumeration value="Rechazado"/&gt;
 *     &lt;enumeration value="Aprobado"/&gt;
 *     &lt;enumeration value="Validado"/&gt;
 *     &lt;enumeration value="NoConforme"/&gt;
 *     &lt;enumeration value="Desistido"/&gt;
 *     &lt;enumeration value="PorAprobar"/&gt;
 *     &lt;enumeration value="PorValidar"/&gt;
 *     &lt;enumeration value="EnProcesoValidacion"/&gt;
 *     &lt;enumeration value="ValidacionAprobada"/&gt;
 *     &lt;enumeration value="ValidacionRechazada"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "EstadoTarjeta", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums")
@XmlEnum
public enum EstadoTarjeta {

    @XmlEnumValue("Desconocido")
    DESCONOCIDO("Desconocido"),
    @XmlEnumValue("Ingresado")
    INGRESADO("Ingresado"),
    @XmlEnumValue("EnProceso")
    EN_PROCESO("EnProceso"),
    @XmlEnumValue("Rechazado")
    RECHAZADO("Rechazado"),
    @XmlEnumValue("Aprobado")
    APROBADO("Aprobado"),
    @XmlEnumValue("Validado")
    VALIDADO("Validado"),
    @XmlEnumValue("NoConforme")
    NO_CONFORME("NoConforme"),
    @XmlEnumValue("Desistido")
    DESISTIDO("Desistido"),
    @XmlEnumValue("PorAprobar")
    POR_APROBAR("PorAprobar"),
    @XmlEnumValue("PorValidar")
    POR_VALIDAR("PorValidar"),
    @XmlEnumValue("EnProcesoValidacion")
    EN_PROCESO_VALIDACION("EnProcesoValidacion"),
    @XmlEnumValue("ValidacionAprobada")
    VALIDACION_APROBADA("ValidacionAprobada"),
    @XmlEnumValue("ValidacionRechazada")
    VALIDACION_RECHAZADA("ValidacionRechazada");
    private final String value;

    EstadoTarjeta(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EstadoTarjeta fromValue(String v) {
        for (EstadoTarjeta c: EstadoTarjeta.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
