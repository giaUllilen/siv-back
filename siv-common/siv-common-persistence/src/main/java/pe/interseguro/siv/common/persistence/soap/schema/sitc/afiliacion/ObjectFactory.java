//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.02.26 a las 12:05:51 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AfiliacionPropuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "AfiliacionPropuesta");
    private final static QName _Cliente_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Cliente");
    private final static QName _Tarjeta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Tarjeta");
    private final static QName _TarjetaPrimeraPrima_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Tarjeta_PrimeraPrima");
    private final static QName _ResultadoServicio_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "ResultadoServicio");
    private final static QName _EstadoTarjeta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", "EstadoTarjeta");
    private final static QName _TipoCuentaFinanciera_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", "TipoCuentaFinanciera");
    private final static QName _TipoViaCobro_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", "TipoViaCobro");
    private final static QName _ViaCobro_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", "ViaCobro");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _RegistrarAfiliacionPropuestaAfiliacionPropuesta_QNAME = new QName("http://www.interseguro.com.pe/AfiliacionPropuestaServices/", "afiliacionPropuesta");
    private final static QName _RegistrarAfiliacionPropuestaResponseRegistrarAfiliacionPropuestaResult_QNAME = new QName("http://www.interseguro.com.pe/AfiliacionPropuestaServices/", "RegistrarAfiliacionPropuestaResult");
    private final static QName _TarjetaPrimeraPrimaFechaVencimiento_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "FechaVencimiento");
    private final static QName _TarjetaPrimeraPrimaMoneda_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Moneda");
    private final static QName _TarjetaPrimeraPrimaNumero_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Numero");
    private final static QName _TarjetaFechaVencimientoCadena_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "FechaVencimientoCadena");
    private final static QName _ClienteApellidoMaterno_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "ApellidoMaterno");
    private final static QName _ClienteApellidoPaterno_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "ApellidoPaterno");
    private final static QName _ClienteDocumentoIdentidad_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "DocumentoIdentidad");
    private final static QName _ClienteNombre_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Nombre");
    private final static QName _ClienteTipoDocumento_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "TipoDocumento");
    private final static QName _ResultadoServicioCodigoRespuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "CodigoRespuesta");
    private final static QName _ResultadoServicioIdAfiliacionPropuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "IdAfiliacionPropuesta");
    private final static QName _ResultadoServicioMensajeRespuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "MensajeRespuesta");
    private final static QName _AfiliacionPropuestaCodUsuarioCreador_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "CodUsuarioCreador");
    private final static QName _AfiliacionPropuestaCodUsuarioCreadorApli_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "CodUsuarioCreadorApli");
    private final static QName _AfiliacionPropuestaFechaAfiliacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "FechaAfiliacion");
    private final static QName _AfiliacionPropuestaGlsToken_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "GlsToken");
    private final static QName _AfiliacionPropuestaIdCotizacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "IdCotizacion");
    private final static QName _AfiliacionPropuestaIndAfiliacion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "IndAfiliacion");
    private final static QName _AfiliacionPropuestaNumeroPoliza_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "NumeroPoliza");
    private final static QName _AfiliacionPropuestaNumeroPropuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "NumeroPropuesta");
    private final static QName _AfiliacionPropuestaPeriodoPago_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "PeriodoPago");
    private final static QName _AfiliacionPropuestaRamo_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Ramo");
    private final static QName _AfiliacionPropuestaSubRamo_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "SubRamo");
    private final static QName _AfiliacionPropuestaTarjetaPP_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Tarjeta_PP");
    private final static QName _AfiliacionPropuestaTelefonoTitular_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "TelefonoTitular");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pe.interseguro.siv.common.persistence.soap.schema.sitc.afiliacion
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RegistrarAfiliacionPropuesta }
     * 
     */
    public RegistrarAfiliacionPropuesta createRegistrarAfiliacionPropuesta() {
        return new RegistrarAfiliacionPropuesta();
    }

    /**
     * Create an instance of {@link AfiliacionPropuesta }
     * 
     */
    public AfiliacionPropuesta createAfiliacionPropuesta() {
        return new AfiliacionPropuesta();
    }

    /**
     * Create an instance of {@link RegistrarAfiliacionPropuestaResponse }
     * 
     */
    public RegistrarAfiliacionPropuestaResponse createRegistrarAfiliacionPropuestaResponse() {
        return new RegistrarAfiliacionPropuestaResponse();
    }

    /**
     * Create an instance of {@link ResultadoServicio }
     * 
     */
    public ResultadoServicio createResultadoServicio() {
        return new ResultadoServicio();
    }

    /**
     * Create an instance of {@link Cliente }
     * 
     */
    public Cliente createCliente() {
        return new Cliente();
    }

    /**
     * Create an instance of {@link Tarjeta }
     * 
     */
    public Tarjeta createTarjeta() {
        return new Tarjeta();
    }

    /**
     * Create an instance of {@link TarjetaPrimeraPrima }
     * 
     */
    public TarjetaPrimeraPrima createTarjetaPrimeraPrima() {
        return new TarjetaPrimeraPrima();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AfiliacionPropuesta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "AfiliacionPropuesta")
    public JAXBElement<AfiliacionPropuesta> createAfiliacionPropuesta(AfiliacionPropuesta value) {
        return new JAXBElement<AfiliacionPropuesta>(_AfiliacionPropuesta_QNAME, AfiliacionPropuesta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Cliente }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Cliente")
    public JAXBElement<Cliente> createCliente(Cliente value) {
        return new JAXBElement<Cliente>(_Cliente_QNAME, Cliente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tarjeta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Tarjeta")
    public JAXBElement<Tarjeta> createTarjeta(Tarjeta value) {
        return new JAXBElement<Tarjeta>(_Tarjeta_QNAME, Tarjeta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TarjetaPrimeraPrima }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Tarjeta_PrimeraPrima")
    public JAXBElement<TarjetaPrimeraPrima> createTarjetaPrimeraPrima(TarjetaPrimeraPrima value) {
        return new JAXBElement<TarjetaPrimeraPrima>(_TarjetaPrimeraPrima_QNAME, TarjetaPrimeraPrima.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoServicio }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "ResultadoServicio")
    public JAXBElement<ResultadoServicio> createResultadoServicio(ResultadoServicio value) {
        return new JAXBElement<ResultadoServicio>(_ResultadoServicio_QNAME, ResultadoServicio.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EstadoTarjeta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", name = "EstadoTarjeta")
    public JAXBElement<EstadoTarjeta> createEstadoTarjeta(EstadoTarjeta value) {
        return new JAXBElement<EstadoTarjeta>(_EstadoTarjeta_QNAME, EstadoTarjeta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoCuentaFinanciera }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", name = "TipoCuentaFinanciera")
    public JAXBElement<TipoCuentaFinanciera> createTipoCuentaFinanciera(TipoCuentaFinanciera value) {
        return new JAXBElement<TipoCuentaFinanciera>(_TipoCuentaFinanciera_QNAME, TipoCuentaFinanciera.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoViaCobro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", name = "TipoViaCobro")
    public JAXBElement<TipoViaCobro> createTipoViaCobro(TipoViaCobro value) {
        return new JAXBElement<TipoViaCobro>(_TipoViaCobro_QNAME, TipoViaCobro.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViaCobro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo.Enums", name = "ViaCobro")
    public JAXBElement<ViaCobro> createViaCobro(ViaCobro value) {
        return new JAXBElement<ViaCobro>(_ViaCobro_QNAME, ViaCobro.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AfiliacionPropuesta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/AfiliacionPropuestaServices/", name = "afiliacionPropuesta", scope = RegistrarAfiliacionPropuesta.class)
    public JAXBElement<AfiliacionPropuesta> createRegistrarAfiliacionPropuestaAfiliacionPropuesta(AfiliacionPropuesta value) {
        return new JAXBElement<AfiliacionPropuesta>(_RegistrarAfiliacionPropuestaAfiliacionPropuesta_QNAME, AfiliacionPropuesta.class, RegistrarAfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoServicio }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/AfiliacionPropuestaServices/", name = "RegistrarAfiliacionPropuestaResult", scope = RegistrarAfiliacionPropuestaResponse.class)
    public JAXBElement<ResultadoServicio> createRegistrarAfiliacionPropuestaResponseRegistrarAfiliacionPropuestaResult(ResultadoServicio value) {
        return new JAXBElement<ResultadoServicio>(_RegistrarAfiliacionPropuestaResponseRegistrarAfiliacionPropuestaResult_QNAME, ResultadoServicio.class, RegistrarAfiliacionPropuestaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FechaVencimiento", scope = TarjetaPrimeraPrima.class)
    public JAXBElement<XMLGregorianCalendar> createTarjetaPrimeraPrimaFechaVencimiento(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TarjetaPrimeraPrimaFechaVencimiento_QNAME, XMLGregorianCalendar.class, TarjetaPrimeraPrima.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Moneda", scope = TarjetaPrimeraPrima.class)
    public JAXBElement<String> createTarjetaPrimeraPrimaMoneda(String value) {
        return new JAXBElement<String>(_TarjetaPrimeraPrimaMoneda_QNAME, String.class, TarjetaPrimeraPrima.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Numero", scope = TarjetaPrimeraPrima.class)
    public JAXBElement<String> createTarjetaPrimeraPrimaNumero(String value) {
        return new JAXBElement<String>(_TarjetaPrimeraPrimaNumero_QNAME, String.class, TarjetaPrimeraPrima.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FechaVencimiento", scope = Tarjeta.class)
    public JAXBElement<XMLGregorianCalendar> createTarjetaFechaVencimiento(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TarjetaPrimeraPrimaFechaVencimiento_QNAME, XMLGregorianCalendar.class, Tarjeta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FechaVencimientoCadena", scope = Tarjeta.class)
    public JAXBElement<String> createTarjetaFechaVencimientoCadena(String value) {
        return new JAXBElement<String>(_TarjetaFechaVencimientoCadena_QNAME, String.class, Tarjeta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Moneda", scope = Tarjeta.class)
    public JAXBElement<String> createTarjetaMoneda(String value) {
        return new JAXBElement<String>(_TarjetaPrimeraPrimaMoneda_QNAME, String.class, Tarjeta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Numero", scope = Tarjeta.class)
    public JAXBElement<String> createTarjetaNumero(String value) {
        return new JAXBElement<String>(_TarjetaPrimeraPrimaNumero_QNAME, String.class, Tarjeta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "ApellidoMaterno", scope = Cliente.class)
    public JAXBElement<String> createClienteApellidoMaterno(String value) {
        return new JAXBElement<String>(_ClienteApellidoMaterno_QNAME, String.class, Cliente.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "ApellidoPaterno", scope = Cliente.class)
    public JAXBElement<String> createClienteApellidoPaterno(String value) {
        return new JAXBElement<String>(_ClienteApellidoPaterno_QNAME, String.class, Cliente.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "DocumentoIdentidad", scope = Cliente.class)
    public JAXBElement<String> createClienteDocumentoIdentidad(String value) {
        return new JAXBElement<String>(_ClienteDocumentoIdentidad_QNAME, String.class, Cliente.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Nombre", scope = Cliente.class)
    public JAXBElement<String> createClienteNombre(String value) {
        return new JAXBElement<String>(_ClienteNombre_QNAME, String.class, Cliente.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "TipoDocumento", scope = Cliente.class)
    public JAXBElement<String> createClienteTipoDocumento(String value) {
        return new JAXBElement<String>(_ClienteTipoDocumento_QNAME, String.class, Cliente.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "CodigoRespuesta", scope = ResultadoServicio.class)
    public JAXBElement<String> createResultadoServicioCodigoRespuesta(String value) {
        return new JAXBElement<String>(_ResultadoServicioCodigoRespuesta_QNAME, String.class, ResultadoServicio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "IdAfiliacionPropuesta", scope = ResultadoServicio.class)
    public JAXBElement<String> createResultadoServicioIdAfiliacionPropuesta(String value) {
        return new JAXBElement<String>(_ResultadoServicioIdAfiliacionPropuesta_QNAME, String.class, ResultadoServicio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "MensajeRespuesta", scope = ResultadoServicio.class)
    public JAXBElement<String> createResultadoServicioMensajeRespuesta(String value) {
        return new JAXBElement<String>(_ResultadoServicioMensajeRespuesta_QNAME, String.class, ResultadoServicio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Cliente }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Cliente", scope = AfiliacionPropuesta.class)
    public JAXBElement<Cliente> createAfiliacionPropuestaCliente(Cliente value) {
        return new JAXBElement<Cliente>(_Cliente_QNAME, Cliente.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "CodUsuarioCreador", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaCodUsuarioCreador(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaCodUsuarioCreador_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "CodUsuarioCreadorApli", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaCodUsuarioCreadorApli(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaCodUsuarioCreadorApli_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FechaAfiliacion", scope = AfiliacionPropuesta.class)
    public JAXBElement<XMLGregorianCalendar> createAfiliacionPropuestaFechaAfiliacion(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_AfiliacionPropuestaFechaAfiliacion_QNAME, XMLGregorianCalendar.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsToken", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaGlsToken(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaGlsToken_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "IdCotizacion", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaIdCotizacion(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaIdCotizacion_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "IndAfiliacion", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaIndAfiliacion(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaIndAfiliacion_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Moneda", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaMoneda(String value) {
        return new JAXBElement<String>(_TarjetaPrimeraPrimaMoneda_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "NumeroPoliza", scope = AfiliacionPropuesta.class)
    public JAXBElement<Long> createAfiliacionPropuestaNumeroPoliza(Long value) {
        return new JAXBElement<Long>(_AfiliacionPropuestaNumeroPoliza_QNAME, Long.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "NumeroPropuesta", scope = AfiliacionPropuesta.class)
    public JAXBElement<Long> createAfiliacionPropuestaNumeroPropuesta(Long value) {
        return new JAXBElement<Long>(_AfiliacionPropuestaNumeroPropuesta_QNAME, Long.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "PeriodoPago", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaPeriodoPago(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaPeriodoPago_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Ramo", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaRamo(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaRamo_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "SubRamo", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaSubRamo(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaSubRamo_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tarjeta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Tarjeta", scope = AfiliacionPropuesta.class)
    public JAXBElement<Tarjeta> createAfiliacionPropuestaTarjeta(Tarjeta value) {
        return new JAXBElement<Tarjeta>(_Tarjeta_QNAME, Tarjeta.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TarjetaPrimeraPrima }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Tarjeta_PP", scope = AfiliacionPropuesta.class)
    public JAXBElement<TarjetaPrimeraPrima> createAfiliacionPropuestaTarjetaPP(TarjetaPrimeraPrima value) {
        return new JAXBElement<TarjetaPrimeraPrima>(_AfiliacionPropuestaTarjetaPP_QNAME, TarjetaPrimeraPrima.class, AfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "TelefonoTitular", scope = AfiliacionPropuesta.class)
    public JAXBElement<String> createAfiliacionPropuestaTelefonoTitular(String value) {
        return new JAXBElement<String>(_AfiliacionPropuestaTelefonoTitular_QNAME, String.class, AfiliacionPropuesta.class, value);
    }

}
