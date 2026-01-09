//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 05:40:32 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ReporteCobranzaMorosaConsulta complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ReporteCobranzaMorosaConsulta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PeriodoCobranza" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReporteCobranzaMorosaConsulta", namespace = "http://www.interseguro.com.pe/samp/cobranza", propOrder = {
    "periodoCobranza"
})
public class ReporteCobranzaMorosaConsulta {

    @XmlElement(name = "PeriodoCobranza", required = true, nillable = true)
    protected String periodoCobranza;

    /**
     * Obtiene el valor de la propiedad periodoCobranza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodoCobranza() {
        return periodoCobranza;
    }

    /**
     * Define el valor de la propiedad periodoCobranza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodoCobranza(String value) {
        this.periodoCobranza = value;
    }

}
