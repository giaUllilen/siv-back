//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 12:05:51 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para Tarjeta_PrimeraPrima complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Tarjeta_PrimeraPrima"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FechaVencimiento" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="Moneda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TipoCuenta" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums}TipoCuentaFinanciera" minOccurs="0"/&gt;
 *         &lt;element name="TipoViaCobro" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums}TipoViaCobro" minOccurs="0"/&gt;
 *         &lt;element name="ViaCobro" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums}ViaCobro" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tarjeta_PrimeraPrima", propOrder = {
    "fechaVencimiento",
    "moneda",
    "numero",
    "tipoCuenta",
    "tipoViaCobro",
    "viaCobro"
})
public class TarjetaPrimeraPrima {

    @XmlElementRef(name = "FechaVencimiento", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> fechaVencimiento;
    @XmlElementRef(name = "Moneda", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> moneda;
    @XmlElementRef(name = "Numero", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> numero;
    @XmlElement(name = "TipoCuenta")
    @XmlSchemaType(name = "string")
    protected TipoCuentaFinanciera tipoCuenta;
    @XmlElement(name = "TipoViaCobro")
    @XmlSchemaType(name = "string")
    protected TipoViaCobro tipoViaCobro;
    @XmlElement(name = "ViaCobro")
    @XmlSchemaType(name = "string")
    protected ViaCobro viaCobro;

    /**
     * Obtiene el valor de la propiedad fechaVencimiento.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Define el valor de la propiedad fechaVencimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setFechaVencimiento(JAXBElement<XMLGregorianCalendar> value) {
        this.fechaVencimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMoneda(JAXBElement<String> value) {
        this.moneda = value;
    }

    /**
     * Obtiene el valor de la propiedad numero.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNumero() {
        return numero;
    }

    /**
     * Define el valor de la propiedad numero.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNumero(JAXBElement<String> value) {
        this.numero = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoCuenta.
     * 
     * @return
     *     possible object is
     *     {@link TipoCuentaFinanciera }
     *     
     */
    public TipoCuentaFinanciera getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * Define el valor de la propiedad tipoCuenta.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoCuentaFinanciera }
     *     
     */
    public void setTipoCuenta(TipoCuentaFinanciera value) {
        this.tipoCuenta = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoViaCobro.
     * 
     * @return
     *     possible object is
     *     {@link TipoViaCobro }
     *     
     */
    public TipoViaCobro getTipoViaCobro() {
        return tipoViaCobro;
    }

    /**
     * Define el valor de la propiedad tipoViaCobro.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoViaCobro }
     *     
     */
    public void setTipoViaCobro(TipoViaCobro value) {
        this.tipoViaCobro = value;
    }

    /**
     * Obtiene el valor de la propiedad viaCobro.
     * 
     * @return
     *     possible object is
     *     {@link ViaCobro }
     *     
     */
    public ViaCobro getViaCobro() {
        return viaCobro;
    }

    /**
     * Define el valor de la propiedad viaCobro.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaCobro }
     *     
     */
    public void setViaCobro(ViaCobro value) {
        this.viaCobro = value;
    }

}
