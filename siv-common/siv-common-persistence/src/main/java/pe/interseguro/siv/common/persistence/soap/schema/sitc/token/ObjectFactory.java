//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.03 a las 06:28:01 PM COT 
//


package pe.interseguro.siv.common.persistence.soap.schema.sitc.token;

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
 * generated in the pe.interseguro.siv.common.persistence.soap.schema.sitc.token package. 
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
    private final static QName _Propuesta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Propuesta");
    private final static QName _Poliza_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Poliza");
    private final static QName _ValidarVigTokenGlsToken_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "glsToken");
    private final static QName _ValidarVigTokenUsuario_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "usuario");
    private final static QName _ValidarVigTokenUsuarioAplicacion_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "usuarioAplicacion");
    private final static QName _RegistrarTokenNumIp_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "numIp");
    private final static QName _RegistrarTokenResponseRegistrarTokenResult_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "RegistrarTokenResult");
    private final static QName _ConsultarDatosCrmResponseConsultarDatosCrmResult_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "ConsultarDatosCrmResult");
    private final static QName _ConsultarPolizaResponseConsultarPolizaResult_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "ConsultarPolizaResult");
    private final static QName _ActualizarAfiliacionPropuestaMoneda_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "moneda");
    private final static QName _ActualizarAfiliacionPropuestaCodPeriodoPago_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "codPeriodoPago");
    private final static QName _ActualizarNumeroPropuestaPropuestaAnt_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "propuestaAnt");
    private final static QName _ActualizarNumeroPropuestaPropuestaNueva_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "propuestaNueva");
    private final static QName _ActualizarNumeroPropuestaResponseActualizarNumeroPropuestaResult_QNAME = new QName("http://www.interseguro.com.pe/TokenServices/", "ActualizarNumeroPropuestaResult");
    private final static QName _PolizaFchVencTarjeta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "FchVencTarjeta");
    private final static QName _PolizaGlsBanco_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "GlsBanco");
    private final static QName _PolizaGlsDiaCobro_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "GlsDiaCobro");
    private final static QName _PolizaGlsNumDiaCobro_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "GlsNumDiaCobro");
    private final static QName _PolizaGlsPeriodoPago_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "GlsPeriodoPago");
    private final static QName _PolizaGlsTipoCuenta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "GlsTipoCuenta");
    private final static QName _PolizaNumDiaCobro_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "NumDiaCobro");
    private final static QName _PolizaNumPoliza_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "NumPoliza");
    private final static QName _PolizaNumeroTarjeta_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "NumeroTarjeta");
    private final static QName _PropuestaFechaVencimiento_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "FechaVencimiento");
    private final static QName _PropuestaFechaVencimientoCadena_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "FechaVencimientoCadena");
    private final static QName _PropuestaInstitucion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Institucion");
    private final static QName _PropuestaMoneda_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Moneda");
    private final static QName _PropuestaNumero_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "Numero");
    private final static QName _PropuestaTipoCuentaString_QNAME = new QName("http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", "TipoCuentaString");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pe.interseguro.siv.common.persistence.soap.schema.sitc.token
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ValidarVigToken }
     * 
     */
    public ValidarVigToken createValidarVigToken() {
        return new ValidarVigToken();
    }

    /**
     * Create an instance of {@link ValidarVigTokenResponse }
     * 
     */
    public ValidarVigTokenResponse createValidarVigTokenResponse() {
        return new ValidarVigTokenResponse();
    }

    /**
     * Create an instance of {@link RegistrarToken }
     * 
     */
    public RegistrarToken createRegistrarToken() {
        return new RegistrarToken();
    }

    /**
     * Create an instance of {@link RegistrarTokenResponse }
     * 
     */
    public RegistrarTokenResponse createRegistrarTokenResponse() {
        return new RegistrarTokenResponse();
    }

    /**
     * Create an instance of {@link ConsultarDatosCrm }
     * 
     */
    public ConsultarDatosCrm createConsultarDatosCrm() {
        return new ConsultarDatosCrm();
    }

    /**
     * Create an instance of {@link ConsultarDatosCrmResponse }
     * 
     */
    public ConsultarDatosCrmResponse createConsultarDatosCrmResponse() {
        return new ConsultarDatosCrmResponse();
    }

    /**
     * Create an instance of {@link Propuesta }
     * 
     */
    public Propuesta createPropuesta() {
        return new Propuesta();
    }

    /**
     * Create an instance of {@link ConsultarPoliza }
     * 
     */
    public ConsultarPoliza createConsultarPoliza() {
        return new ConsultarPoliza();
    }

    /**
     * Create an instance of {@link ConsultarPolizaResponse }
     * 
     */
    public ConsultarPolizaResponse createConsultarPolizaResponse() {
        return new ConsultarPolizaResponse();
    }

    /**
     * Create an instance of {@link Poliza }
     * 
     */
    public Poliza createPoliza() {
        return new Poliza();
    }

    /**
     * Create an instance of {@link ActualizarAfiliacionPropuesta }
     * 
     */
    public ActualizarAfiliacionPropuesta createActualizarAfiliacionPropuesta() {
        return new ActualizarAfiliacionPropuesta();
    }

    /**
     * Create an instance of {@link ActualizarAfiliacionPropuestaResponse }
     * 
     */
    public ActualizarAfiliacionPropuestaResponse createActualizarAfiliacionPropuestaResponse() {
        return new ActualizarAfiliacionPropuestaResponse();
    }

    /**
     * Create an instance of {@link ActualizarNumeroPropuesta }
     * 
     */
    public ActualizarNumeroPropuesta createActualizarNumeroPropuesta() {
        return new ActualizarNumeroPropuesta();
    }

    /**
     * Create an instance of {@link ActualizarNumeroPropuestaResponse }
     * 
     */
    public ActualizarNumeroPropuestaResponse createActualizarNumeroPropuestaResponse() {
        return new ActualizarNumeroPropuestaResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Propuesta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Propuesta")
    public JAXBElement<Propuesta> createPropuesta(Propuesta value) {
        return new JAXBElement<Propuesta>(_Propuesta_QNAME, Propuesta.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Poliza }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Poliza")
    public JAXBElement<Poliza> createPoliza(Poliza value) {
        return new JAXBElement<Poliza>(_Poliza_QNAME, Poliza.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "glsToken", scope = ValidarVigToken.class)
    public JAXBElement<String> createValidarVigTokenGlsToken(String value) {
        return new JAXBElement<String>(_ValidarVigTokenGlsToken_QNAME, String.class, ValidarVigToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuario", scope = ValidarVigToken.class)
    public JAXBElement<String> createValidarVigTokenUsuario(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuario_QNAME, String.class, ValidarVigToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuarioAplicacion", scope = ValidarVigToken.class)
    public JAXBElement<String> createValidarVigTokenUsuarioAplicacion(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuarioAplicacion_QNAME, String.class, ValidarVigToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "numIp", scope = RegistrarToken.class)
    public JAXBElement<String> createRegistrarTokenNumIp(String value) {
        return new JAXBElement<String>(_RegistrarTokenNumIp_QNAME, String.class, RegistrarToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuario", scope = RegistrarToken.class)
    public JAXBElement<String> createRegistrarTokenUsuario(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuario_QNAME, String.class, RegistrarToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuarioAplicacion", scope = RegistrarToken.class)
    public JAXBElement<String> createRegistrarTokenUsuarioAplicacion(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuarioAplicacion_QNAME, String.class, RegistrarToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "RegistrarTokenResult", scope = RegistrarTokenResponse.class)
    public JAXBElement<String> createRegistrarTokenResponseRegistrarTokenResult(String value) {
        return new JAXBElement<String>(_RegistrarTokenResponseRegistrarTokenResult_QNAME, String.class, RegistrarTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Propuesta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "ConsultarDatosCrmResult", scope = ConsultarDatosCrmResponse.class)
    public JAXBElement<Propuesta> createConsultarDatosCrmResponseConsultarDatosCrmResult(Propuesta value) {
        return new JAXBElement<Propuesta>(_ConsultarDatosCrmResponseConsultarDatosCrmResult_QNAME, Propuesta.class, ConsultarDatosCrmResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Poliza }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "ConsultarPolizaResult", scope = ConsultarPolizaResponse.class)
    public JAXBElement<Poliza> createConsultarPolizaResponseConsultarPolizaResult(Poliza value) {
        return new JAXBElement<Poliza>(_ConsultarPolizaResponseConsultarPolizaResult_QNAME, Poliza.class, ConsultarPolizaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "moneda", scope = ActualizarAfiliacionPropuesta.class)
    public JAXBElement<String> createActualizarAfiliacionPropuestaMoneda(String value) {
        return new JAXBElement<String>(_ActualizarAfiliacionPropuestaMoneda_QNAME, String.class, ActualizarAfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "codPeriodoPago", scope = ActualizarAfiliacionPropuesta.class)
    public JAXBElement<String> createActualizarAfiliacionPropuestaCodPeriodoPago(String value) {
        return new JAXBElement<String>(_ActualizarAfiliacionPropuestaCodPeriodoPago_QNAME, String.class, ActualizarAfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuario", scope = ActualizarAfiliacionPropuesta.class)
    public JAXBElement<String> createActualizarAfiliacionPropuestaUsuario(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuario_QNAME, String.class, ActualizarAfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuarioAplicacion", scope = ActualizarAfiliacionPropuesta.class)
    public JAXBElement<String> createActualizarAfiliacionPropuestaUsuarioAplicacion(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuarioAplicacion_QNAME, String.class, ActualizarAfiliacionPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "propuestaAnt", scope = ActualizarNumeroPropuesta.class)
    public JAXBElement<String> createActualizarNumeroPropuestaPropuestaAnt(String value) {
        return new JAXBElement<String>(_ActualizarNumeroPropuestaPropuestaAnt_QNAME, String.class, ActualizarNumeroPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "propuestaNueva", scope = ActualizarNumeroPropuesta.class)
    public JAXBElement<String> createActualizarNumeroPropuestaPropuestaNueva(String value) {
        return new JAXBElement<String>(_ActualizarNumeroPropuestaPropuestaNueva_QNAME, String.class, ActualizarNumeroPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuario", scope = ActualizarNumeroPropuesta.class)
    public JAXBElement<String> createActualizarNumeroPropuestaUsuario(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuario_QNAME, String.class, ActualizarNumeroPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "usuarioAplicacion", scope = ActualizarNumeroPropuesta.class)
    public JAXBElement<String> createActualizarNumeroPropuestaUsuarioAplicacion(String value) {
        return new JAXBElement<String>(_ValidarVigTokenUsuarioAplicacion_QNAME, String.class, ActualizarNumeroPropuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.interseguro.com.pe/TokenServices/", name = "ActualizarNumeroPropuestaResult", scope = ActualizarNumeroPropuestaResponse.class)
    public JAXBElement<String> createActualizarNumeroPropuestaResponseActualizarNumeroPropuestaResult(String value) {
        return new JAXBElement<String>(_ActualizarNumeroPropuestaResponseActualizarNumeroPropuestaResult_QNAME, String.class, ActualizarNumeroPropuestaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FchVencTarjeta", scope = Poliza.class)
    public JAXBElement<String> createPolizaFchVencTarjeta(String value) {
        return new JAXBElement<String>(_PolizaFchVencTarjeta_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsBanco", scope = Poliza.class)
    public JAXBElement<String> createPolizaGlsBanco(String value) {
        return new JAXBElement<String>(_PolizaGlsBanco_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsDiaCobro", scope = Poliza.class)
    public JAXBElement<String> createPolizaGlsDiaCobro(String value) {
        return new JAXBElement<String>(_PolizaGlsDiaCobro_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsNumDiaCobro", scope = Poliza.class)
    public JAXBElement<String> createPolizaGlsNumDiaCobro(String value) {
        return new JAXBElement<String>(_PolizaGlsNumDiaCobro_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsPeriodoPago", scope = Poliza.class)
    public JAXBElement<String> createPolizaGlsPeriodoPago(String value) {
        return new JAXBElement<String>(_PolizaGlsPeriodoPago_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsTipoCuenta", scope = Poliza.class)
    public JAXBElement<String> createPolizaGlsTipoCuenta(String value) {
        return new JAXBElement<String>(_PolizaGlsTipoCuenta_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "NumDiaCobro", scope = Poliza.class)
    public JAXBElement<String> createPolizaNumDiaCobro(String value) {
        return new JAXBElement<String>(_PolizaNumDiaCobro_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "NumPoliza", scope = Poliza.class)
    public JAXBElement<String> createPolizaNumPoliza(String value) {
        return new JAXBElement<String>(_PolizaNumPoliza_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "NumeroTarjeta", scope = Poliza.class)
    public JAXBElement<String> createPolizaNumeroTarjeta(String value) {
        return new JAXBElement<String>(_PolizaNumeroTarjeta_QNAME, String.class, Poliza.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FechaVencimiento", scope = Propuesta.class)
    public JAXBElement<XMLGregorianCalendar> createPropuestaFechaVencimiento(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PropuestaFechaVencimiento_QNAME, XMLGregorianCalendar.class, Propuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "FechaVencimientoCadena", scope = Propuesta.class)
    public JAXBElement<String> createPropuestaFechaVencimientoCadena(String value) {
        return new JAXBElement<String>(_PropuestaFechaVencimientoCadena_QNAME, String.class, Propuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "GlsTipoCuenta", scope = Propuesta.class)
    public JAXBElement<String> createPropuestaGlsTipoCuenta(String value) {
        return new JAXBElement<String>(_PolizaGlsTipoCuenta_QNAME, String.class, Propuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Institucion", scope = Propuesta.class)
    public JAXBElement<String> createPropuestaInstitucion(String value) {
        return new JAXBElement<String>(_PropuestaInstitucion_QNAME, String.class, Propuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Moneda", scope = Propuesta.class)
    public JAXBElement<String> createPropuestaMoneda(String value) {
        return new JAXBElement<String>(_PropuestaMoneda_QNAME, String.class, Propuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "Numero", scope = Propuesta.class)
    public JAXBElement<String> createPropuestaNumero(String value) {
        return new JAXBElement<String>(_PropuestaNumero_QNAME, String.class, Propuesta.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Interseguro.Sat.Domain.Afiliaciones.Modelo", name = "TipoCuentaString", scope = Propuesta.class)
    public JAXBElement<String> createPropuestaTipoCuentaString(String value) {
        return new JAXBElement<String>(_PropuestaTipoCuentaString_QNAME, String.class, Propuesta.class, value);
    }

}
