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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Poliza complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Poliza"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FchVencTarjeta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GlsBanco" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GlsDiaCobro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GlsNumDiaCobro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GlsPeriodoPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GlsTipoCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumDiaCobro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumPoliza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumeroTarjeta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Poliza", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", propOrder = {
    "fchVencTarjeta",
    "glsBanco",
    "glsDiaCobro",
    "glsNumDiaCobro",
    "glsPeriodoPago",
    "glsTipoCuenta",
    "numDiaCobro",
    "numPoliza",
    "numeroTarjeta"
})
public class Poliza {

    @XmlElementRef(name = "FchVencTarjeta", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> fchVencTarjeta;
    @XmlElementRef(name = "GlsBanco", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> glsBanco;
    @XmlElementRef(name = "GlsDiaCobro", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> glsDiaCobro;
    @XmlElementRef(name = "GlsNumDiaCobro", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> glsNumDiaCobro;
    @XmlElementRef(name = "GlsPeriodoPago", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> glsPeriodoPago;
    @XmlElementRef(name = "GlsTipoCuenta", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> glsTipoCuenta;
    @XmlElementRef(name = "NumDiaCobro", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numDiaCobro;
    @XmlElementRef(name = "NumPoliza", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numPoliza;
    @XmlElementRef(name = "NumeroTarjeta", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numeroTarjeta;

    /**
     * Obtiene el valor de la propiedad fchVencTarjeta.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFchVencTarjeta() {
        return fchVencTarjeta;
    }

    /**
     * Define el valor de la propiedad fchVencTarjeta.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFchVencTarjeta(JAXBElement<String> value) {
        this.fchVencTarjeta = value;
    }

    /**
     * Obtiene el valor de la propiedad glsBanco.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGlsBanco() {
        return glsBanco;
    }

    /**
     * Define el valor de la propiedad glsBanco.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGlsBanco(JAXBElement<String> value) {
        this.glsBanco = value;
    }

    /**
     * Obtiene el valor de la propiedad glsDiaCobro.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGlsDiaCobro() {
        return glsDiaCobro;
    }

    /**
     * Define el valor de la propiedad glsDiaCobro.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGlsDiaCobro(JAXBElement<String> value) {
        this.glsDiaCobro = value;
    }

    /**
     * Obtiene el valor de la propiedad glsNumDiaCobro.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGlsNumDiaCobro() {
        return glsNumDiaCobro;
    }

    /**
     * Define el valor de la propiedad glsNumDiaCobro.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGlsNumDiaCobro(JAXBElement<String> value) {
        this.glsNumDiaCobro = value;
    }

    /**
     * Obtiene el valor de la propiedad glsPeriodoPago.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGlsPeriodoPago() {
        return glsPeriodoPago;
    }

    /**
     * Define el valor de la propiedad glsPeriodoPago.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGlsPeriodoPago(JAXBElement<String> value) {
        this.glsPeriodoPago = value;
    }

    /**
     * Obtiene el valor de la propiedad glsTipoCuenta.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGlsTipoCuenta() {
        return glsTipoCuenta;
    }

    /**
     * Define el valor de la propiedad glsTipoCuenta.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGlsTipoCuenta(JAXBElement<String> value) {
        this.glsTipoCuenta = value;
    }

    /**
     * Obtiene el valor de la propiedad numDiaCobro.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumDiaCobro() {
        return numDiaCobro;
    }

    /**
     * Define el valor de la propiedad numDiaCobro.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumDiaCobro(JAXBElement<String> value) {
        this.numDiaCobro = value;
    }

    /**
     * Obtiene el valor de la propiedad numPoliza.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumPoliza() {
        return numPoliza;
    }

    /**
     * Define el valor de la propiedad numPoliza.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumPoliza(JAXBElement<String> value) {
        this.numPoliza = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroTarjeta.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * Define el valor de la propiedad numeroTarjeta.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumeroTarjeta(JAXBElement<String> value) {
        this.numeroTarjeta = value;
    }

}
