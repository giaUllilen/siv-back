//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 12:05:51 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para AfiliacionPropuesta complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AfiliacionPropuesta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Cliente" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo}Cliente" minOccurs="0"/&gt;
 *         &lt;element name="CodUsuarioCreador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CodUsuarioCreadorApli" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FechaAfiliacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="FechaIngresoMandato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="GlsToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="IdCotizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IndAfiliacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Moneda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumeroDiaCobro" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="NumeroPoliza" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="NumeroProcesoAfiliacion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="NumeroPropuesta" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="PeriodoPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Ramo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SubRamo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Tarjeta" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo}Tarjeta" minOccurs="0"/&gt;
 *         &lt;element name="Tarjeta_PP" type="{http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo}Tarjeta_PrimeraPrima" minOccurs="0"/&gt;
 *         &lt;element name="TelefonoTitular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ValPrimaFormaPago" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AfiliacionPropuesta", propOrder = {
    "cliente",
    "codUsuarioCreador",
    "codUsuarioCreadorApli",
    "fechaAfiliacion",
    "fechaIngresoMandato",
    "glsToken",
    "id",
    "idCotizacion",
    "indAfiliacion",
    "moneda",
    "numeroDiaCobro",
    "numeroPoliza",
    "numeroProcesoAfiliacion",
    "numeroPropuesta",
    "periodoPago",
    "ramo",
    "subRamo",
    "tarjeta",
    "tarjetaPP",
    "telefonoTitular",
    "valPrimaFormaPago"
})
public class AfiliacionPropuesta {

    @XmlElementRef(name = "Cliente", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<Cliente> cliente;
    @XmlElementRef(name = "CodUsuarioCreador", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codUsuarioCreador;
    @XmlElementRef(name = "CodUsuarioCreadorApli", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> codUsuarioCreadorApli;
    @XmlElementRef(name = "FechaAfiliacion", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> fechaAfiliacion;
    @XmlElement(name = "FechaIngresoMandato")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaIngresoMandato;
    @XmlElementRef(name = "GlsToken", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> glsToken;
    @XmlElement(name = "Id")
    protected Integer id;
    @XmlElementRef(name = "IdCotizacion", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idCotizacion;
    @XmlElementRef(name = "IndAfiliacion", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> indAfiliacion;
    @XmlElementRef(name = "Moneda", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> moneda;
    @XmlElement(name = "NumeroDiaCobro")
    protected Integer numeroDiaCobro;
    @XmlElementRef(name = "NumeroPoliza", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<Long> numeroPoliza;
    @XmlElement(name = "NumeroProcesoAfiliacion")
    protected Integer numeroProcesoAfiliacion;
    @XmlElementRef(name = "NumeroPropuesta", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<Long> numeroPropuesta;
    @XmlElementRef(name = "PeriodoPago", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> periodoPago;
    @XmlElementRef(name = "Ramo", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> ramo;
    @XmlElementRef(name = "SubRamo", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> subRamo;
    @XmlElementRef(name = "Tarjeta", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<Tarjeta> tarjeta;
    @XmlElementRef(name = "Tarjeta_PP", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<TarjetaPrimeraPrima> tarjetaPP;
    @XmlElementRef(name = "TelefonoTitular", namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", type = JAXBElement.class, required = false)
    protected JAXBElement<String> telefonoTitular;
    @XmlElement(name = "ValPrimaFormaPago")
    protected BigDecimal valPrimaFormaPago;

    /**
     * Obtiene el valor de la propiedad cliente.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Cliente }{@code >}
     *     
     */
    public JAXBElement<Cliente> getCliente() {
        return cliente;
    }

    /**
     * Define el valor de la propiedad cliente.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Cliente }{@code >}
     *     
     */
    public void setCliente(JAXBElement<Cliente> value) {
        this.cliente = value;
    }

    /**
     * Obtiene el valor de la propiedad codUsuarioCreador.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodUsuarioCreador() {
        return codUsuarioCreador;
    }

    /**
     * Define el valor de la propiedad codUsuarioCreador.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodUsuarioCreador(JAXBElement<String> value) {
        this.codUsuarioCreador = value;
    }

    /**
     * Obtiene el valor de la propiedad codUsuarioCreadorApli.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodUsuarioCreadorApli() {
        return codUsuarioCreadorApli;
    }

    /**
     * Define el valor de la propiedad codUsuarioCreadorApli.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodUsuarioCreadorApli(JAXBElement<String> value) {
        this.codUsuarioCreadorApli = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaAfiliacion.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    /**
     * Define el valor de la propiedad fechaAfiliacion.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setFechaAfiliacion(JAXBElement<XMLGregorianCalendar> value) {
        this.fechaAfiliacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaIngresoMandato.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaIngresoMandato() {
        return fechaIngresoMandato;
    }

    /**
     * Define el valor de la propiedad fechaIngresoMandato.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaIngresoMandato(XMLGregorianCalendar value) {
        this.fechaIngresoMandato = value;
    }

    /**
     * Obtiene el valor de la propiedad glsToken.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGlsToken() {
        return glsToken;
    }

    /**
     * Define el valor de la propiedad glsToken.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGlsToken(JAXBElement<String> value) {
        this.glsToken = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad idCotizacion.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdCotizacion() {
        return idCotizacion;
    }

    /**
     * Define el valor de la propiedad idCotizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdCotizacion(JAXBElement<String> value) {
        this.idCotizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad indAfiliacion.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIndAfiliacion() {
        return indAfiliacion;
    }

    /**
     * Define el valor de la propiedad indAfiliacion.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIndAfiliacion(JAXBElement<String> value) {
        this.indAfiliacion = value;
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
     * Obtiene el valor de la propiedad numeroDiaCobro.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroDiaCobro() {
        return numeroDiaCobro;
    }

    /**
     * Define el valor de la propiedad numeroDiaCobro.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroDiaCobro(Integer value) {
        this.numeroDiaCobro = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroPoliza.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getNumeroPoliza() {
        return numeroPoliza;
    }

    /**
     * Define el valor de la propiedad numeroPoliza.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setNumeroPoliza(JAXBElement<Long> value) {
        this.numeroPoliza = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroProcesoAfiliacion.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroProcesoAfiliacion() {
        return numeroProcesoAfiliacion;
    }

    /**
     * Define el valor de la propiedad numeroProcesoAfiliacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroProcesoAfiliacion(Integer value) {
        this.numeroProcesoAfiliacion = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroPropuesta.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getNumeroPropuesta() {
        return numeroPropuesta;
    }

    /**
     * Define el valor de la propiedad numeroPropuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setNumeroPropuesta(JAXBElement<Long> value) {
        this.numeroPropuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad periodoPago.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPeriodoPago() {
        return periodoPago;
    }

    /**
     * Define el valor de la propiedad periodoPago.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPeriodoPago(JAXBElement<String> value) {
        this.periodoPago = value;
    }

    /**
     * Obtiene el valor de la propiedad ramo.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRamo() {
        return ramo;
    }

    /**
     * Define el valor de la propiedad ramo.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRamo(JAXBElement<String> value) {
        this.ramo = value;
    }

    /**
     * Obtiene el valor de la propiedad subRamo.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSubRamo() {
        return subRamo;
    }

    /**
     * Define el valor de la propiedad subRamo.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubRamo(JAXBElement<String> value) {
        this.subRamo = value;
    }

    /**
     * Obtiene el valor de la propiedad tarjeta.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Tarjeta }{@code >}
     *     
     */
    public JAXBElement<Tarjeta> getTarjeta() {
        return tarjeta;
    }

    /**
     * Define el valor de la propiedad tarjeta.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Tarjeta }{@code >}
     *     
     */
    public void setTarjeta(JAXBElement<Tarjeta> value) {
        this.tarjeta = value;
    }

    /**
     * Obtiene el valor de la propiedad tarjetaPP.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TarjetaPrimeraPrima }{@code >}
     *     
     */
    public JAXBElement<TarjetaPrimeraPrima> getTarjetaPP() {
        return tarjetaPP;
    }

    /**
     * Define el valor de la propiedad tarjetaPP.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TarjetaPrimeraPrima }{@code >}
     *     
     */
    public void setTarjetaPP(JAXBElement<TarjetaPrimeraPrima> value) {
        this.tarjetaPP = value;
    }

    /**
     * Obtiene el valor de la propiedad telefonoTitular.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelefonoTitular() {
        return telefonoTitular;
    }

    /**
     * Define el valor de la propiedad telefonoTitular.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelefonoTitular(JAXBElement<String> value) {
        this.telefonoTitular = value;
    }

    /**
     * Obtiene el valor de la propiedad valPrimaFormaPago.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValPrimaFormaPago() {
        return valPrimaFormaPago;
    }

    /**
     * Define el valor de la propiedad valPrimaFormaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValPrimaFormaPago(BigDecimal value) {
        this.valPrimaFormaPago = value;
    }

}
