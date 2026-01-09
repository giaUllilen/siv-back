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
 * <p>Clase Java para TipoViaCobro.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p>
 * <pre>
 * &lt;simpleType name="TipoViaCobro"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Desconocido"/&gt;
 *     &lt;enumeration value="CuentaFinanciera"/&gt;
 *     &lt;enumeration value="TarjetaCredito"/&gt;
 *     &lt;enumeration value="CajaVentilla"/&gt;
 *     &lt;enumeration value="DescuentoPlanilla"/&gt;
 *     &lt;enumeration value="Todos"/&gt;
 *     &lt;enumeration value="Cheque"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TipoViaCobro", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums")
@XmlEnum
public enum TipoViaCobro {

    @XmlEnumValue("Desconocido")
    DESCONOCIDO("Desconocido"),
    @XmlEnumValue("CuentaFinanciera")
    CUENTA_FINANCIERA("CuentaFinanciera"),
    @XmlEnumValue("TarjetaCredito")
    TARJETA_CREDITO("TarjetaCredito"),
    @XmlEnumValue("CajaVentilla")
    CAJA_VENTILLA("CajaVentilla"),
    @XmlEnumValue("DescuentoPlanilla")
    DESCUENTO_PLANILLA("DescuentoPlanilla"),
    @XmlEnumValue("Todos")
    TODOS("Todos"),
    @XmlEnumValue("Cheque")
    CHEQUE("Cheque");
    private final String value;

    TipoViaCobro(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoViaCobro fromValue(String v) {
        for (TipoViaCobro c: TipoViaCobro.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
