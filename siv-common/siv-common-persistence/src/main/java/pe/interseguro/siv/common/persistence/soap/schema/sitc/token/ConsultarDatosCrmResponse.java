//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.03 a las 06:28:01 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.token;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element name="ConsultarDatosCrmResult" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo}Propuesta" minOccurs="0"/&gt;
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
    "consultarDatosCrmResult"
})
@XmlRootElement(name = "ConsultarDatosCrmResponse")
public class ConsultarDatosCrmResponse {

    @XmlElementRef(name = "ConsultarDatosCrmResult", namespace = "http://www.interseguro.com.pe/TokenServices/", type = JAXBElement.class, required = false)
    protected JAXBElement<Propuesta> consultarDatosCrmResult;

    /**
     * Obtiene el valor de la propiedad consultarDatosCrmResult.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Propuesta }{@code >}
     *     
     */
    public JAXBElement<Propuesta> getConsultarDatosCrmResult() {
        return consultarDatosCrmResult;
    }

    /**
     * Define el valor de la propiedad consultarDatosCrmResult.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Propuesta }{@code >}
     *     
     */
    public void setConsultarDatosCrmResult(JAXBElement<Propuesta> value) {
        this.consultarDatosCrmResult = value;
    }

}
