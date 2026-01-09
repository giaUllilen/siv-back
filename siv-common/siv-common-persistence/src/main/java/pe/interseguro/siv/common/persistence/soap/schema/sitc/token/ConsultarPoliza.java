//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.03 a las 06:28:01 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numPoliza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "numPoliza"
})
@XmlRootElement(name = "ConsultarPoliza")
public class ConsultarPoliza {

    protected Long numPoliza;

    /**
     * Obtiene el valor de la propiedad numPoliza.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumPoliza() {
        return numPoliza;
    }

    /**
     * Define el valor de la propiedad numPoliza.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumPoliza(Long value) {
        this.numPoliza = value;
    }

}
