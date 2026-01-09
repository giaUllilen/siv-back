//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 05:40:32 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza;

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
 *         &lt;element name="RegistroDigitalizacionPagosVidaResult" type="{http://www.interseguro.com.pe/samp/cobranza}DigitalizacionPagosVidaRespuesta" minOccurs="0"/&gt;
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
    "registroDigitalizacionPagosVidaResult"
})
@XmlRootElement(name = "RegistroDigitalizacionPagosVidaResponse")
public class RegistroDigitalizacionPagosVidaResponse {

    @XmlElementRef(name = "RegistroDigitalizacionPagosVidaResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<DigitalizacionPagosVidaRespuesta> registroDigitalizacionPagosVidaResult;

    /**
     * Obtiene el valor de la propiedad registroDigitalizacionPagosVidaResult.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DigitalizacionPagosVidaRespuesta }{@code >}
     *     
     */
    public JAXBElement<DigitalizacionPagosVidaRespuesta> getRegistroDigitalizacionPagosVidaResult() {
        return registroDigitalizacionPagosVidaResult;
    }

    /**
     * Define el valor de la propiedad registroDigitalizacionPagosVidaResult.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DigitalizacionPagosVidaRespuesta }{@code >}
     *     
     */
    public void setRegistroDigitalizacionPagosVidaResult(JAXBElement<DigitalizacionPagosVidaRespuesta> value) {
        this.registroDigitalizacionPagosVidaResult = value;
    }

}
