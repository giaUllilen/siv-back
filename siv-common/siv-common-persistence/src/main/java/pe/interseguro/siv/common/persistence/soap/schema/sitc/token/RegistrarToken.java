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
 *         &lt;element name="numPropuesta" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="numIp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="usuarioAplicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "numPropuesta",
    "numIp",
    "usuario",
    "usuarioAplicacion"
})
@XmlRootElement(name = "RegistrarToken")
public class RegistrarToken {

    protected Long numPropuesta;
    @XmlElementRef(name = "numIp", namespace = "http://www.interseguro.com.pe/TokenServices/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numIp;
    @XmlElementRef(name = "usuario", namespace = "http://www.interseguro.com.pe/TokenServices/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> usuario;
    @XmlElementRef(name = "usuarioAplicacion", namespace = "http://www.interseguro.com.pe/TokenServices/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> usuarioAplicacion;

    /**
     * Obtiene el valor de la propiedad numPropuesta.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumPropuesta() {
        return numPropuesta;
    }

    /**
     * Define el valor de la propiedad numPropuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumPropuesta(Long value) {
        this.numPropuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad numIp.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumIp() {
        return numIp;
    }

    /**
     * Define el valor de la propiedad numIp.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumIp(JAXBElement<String> value) {
        this.numIp = value;
    }

    /**
     * Obtiene el valor de la propiedad usuario.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUsuario() {
        return usuario;
    }

    /**
     * Define el valor de la propiedad usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUsuario(JAXBElement<String> value) {
        this.usuario = value;
    }

    /**
     * Obtiene el valor de la propiedad usuarioAplicacion.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUsuarioAplicacion() {
        return usuarioAplicacion;
    }

    /**
     * Define el valor de la propiedad usuarioAplicacion.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUsuarioAplicacion(JAXBElement<String> value) {
        this.usuarioAplicacion = value;
    }

}
