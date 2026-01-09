//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 05:40:32 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DigitalizacionPagosVidaConsulta complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DigitalizacionPagosVidaConsulta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NumeroPropuestaCadena" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Monto" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="Moneda" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IdPropuestaAfiliacion" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="IdCulqui" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CodUsuario" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CodUsuarioAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Pasarela" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Pasarela_pp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consulta", namespace = "http://tempuri.org/", propOrder = {
    "numeroPropuestaCadena",
    "monto",
    "moneda",
    "idPropuestaAfiliacion",
    "idCulqui",
    "codUsuario",
    "codUsuarioAplicacion",
    "pasarela",
    "pasarela_pp",
    "indPagoPasarela"
})
public class DigitalizacionPagosVidaConsulta {

    @XmlElement(name = "NumeroPropuestaCadena", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String numeroPropuestaCadena;
    @XmlElement(name = "Monto", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true)
    protected BigDecimal monto;
    @XmlElement(name = "Moneda", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String moneda;
    @XmlElement(name = "IdPropuestaAfiliacion", namespace = "http://www.interseguro.com.pe/samp/cobranza")
    protected int idPropuestaAfiliacion;
    @XmlElement(name = "IdCulqui", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String idCulqui;
    @XmlElement(name = "CodUsuario", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String codUsuario;
    @XmlElement(name = "CodUsuarioAplicacion", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String codUsuarioAplicacion;
    @XmlElement(name = "Pasarela", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String pasarela;
    @XmlElement(name = "Pasarela_pp", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String pasarela_pp;
    @XmlElement(name = "IndPagoPasarela", namespace = "http://www.interseguro.com.pe/samp/cobranza", required = true, nillable = true)
    protected String indPagoPasarela;

    /**
     * Obtiene el valor de la propiedad numeroPropuestaCadena.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroPropuestaCadena() {
        return numeroPropuestaCadena;
    }

    /**
     * Define el valor de la propiedad numeroPropuestaCadena.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroPropuestaCadena(String value) {
        this.numeroPropuestaCadena = value;
    }

    /**
     * Obtiene el valor de la propiedad monto.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMonto(BigDecimal value) {
        this.monto = value;
    }

    /**
     * Obtiene el valor de la propiedad moneda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * Define el valor de la propiedad moneda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoneda(String value) {
        this.moneda = value;
    }

    /**
     * Obtiene el valor de la propiedad idPropuestaAfiliacion.
     * 
     */
    public int getIdPropuestaAfiliacion() {
        return idPropuestaAfiliacion;
    }

    /**
     * Define el valor de la propiedad idPropuestaAfiliacion.
     * 
     */
    public void setIdPropuestaAfiliacion(int value) {
        this.idPropuestaAfiliacion = value;
    }

    /**
     * Obtiene el valor de la propiedad idCulqui.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCulqui() {
        return idCulqui;
    }

    /**
     * Define el valor de la propiedad idCulqui.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCulqui(String value) {
        this.idCulqui = value;
    }

    /**
     * Obtiene el valor de la propiedad codUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUsuario() {
        return codUsuario;
    }

    /**
     * Define el valor de la propiedad codUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUsuario(String value) {
        this.codUsuario = value;
    }

    /**
     * Obtiene el valor de la propiedad codUsuarioAplicacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUsuarioAplicacion() {
        return codUsuarioAplicacion;
    }

    /**
     * Define el valor de la propiedad codUsuarioAplicacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUsuarioAplicacion(String value) {
        this.codUsuarioAplicacion = value;
    }
    
    /**
     * Obtiene el valor de la propiedad pasarela.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasarela() {
        return pasarela;
    }

    /**
     * Define el valor de la propiedad pasarela.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasarela(String value) {
        this.pasarela = value;
    }
    
    /**
     * Obtiene el valor de la propiedad pasarela primera prima.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasarela_pp() {
        return pasarela_pp;
    }

    /**
     * Define el valor de la propiedad pasarela primera prima.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasarela_pp(String value) {
        this.pasarela_pp = value;
    }

    /**
     * Obtiene el valor de la propiedad indicador pago pasarela.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
	public String getIndPagoPasarela() {
		return indPagoPasarela;
	}

	/**
     * Define el valor de la propiedad indicador pago pasarela.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
	public void setIndPagoPasarela(String indPagoPasarela) {
		this.indPagoPasarela = indPagoPasarela;
	}

}
