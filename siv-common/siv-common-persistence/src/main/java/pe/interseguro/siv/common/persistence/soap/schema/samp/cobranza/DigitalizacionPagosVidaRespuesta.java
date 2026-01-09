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
 * <p>Clase Java para DigitalizacionPagosVidaRespuesta complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DigitalizacionPagosVidaRespuesta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CodigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MensajeRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IdDigitalizacionPagosVida" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DigitalizacionPagosVidaRespuesta", namespace = "http://www.interseguro.com.pe/samp/cobranza", propOrder = {
    "codigoRespuesta",
    "mensajeRespuesta",
    "idDigitalizacionPagosVida"
})
public class DigitalizacionPagosVidaRespuesta {

    @XmlElement(name = "CodigoRespuesta", required = true, nillable = true)
    protected String codigoRespuesta;
    @XmlElement(name = "MensajeRespuesta", required = true, nillable = true)
    protected String mensajeRespuesta;
    @XmlElement(name = "IdDigitalizacionPagosVida")
    protected long idDigitalizacionPagosVida;

    /**
     * Obtiene el valor de la propiedad codigoRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    /**
     * Define el valor de la propiedad codigoRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoRespuesta(String value) {
        this.codigoRespuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad mensajeRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    /**
     * Define el valor de la propiedad mensajeRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensajeRespuesta(String value) {
        this.mensajeRespuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad idDigitalizacionPagosVida.
     * 
     */
    public long getIdDigitalizacionPagosVida() {
        return idDigitalizacionPagosVida;
    }

    /**
     * Define el valor de la propiedad idDigitalizacionPagosVida.
     * 
     */
    public void setIdDigitalizacionPagosVida(long value) {
        this.idDigitalizacionPagosVida = value;
    }

}
