package pe.interseguro.siv.common.persistence.db.mysql.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionPG {

    private String nroCotizacion;
    private String fechaCotizacion;
    private String productoId;
    private String asegurado;
    private String productoNombre;
    private String planId;
    private String planNombre;
    private String periodoPago;
    private String moneda;
    private String monedaSimbolo;
    private String primaBruta;
    private String primaPagoTotal;
    private String numeroDocumento;
    private String grupoFamiliar;
    private String subplanId;
    private String subplan;
    private String crmOportunidadId;
    private String crmCotizadorId;
    private String numeroCotizacionOrigen;
    private String estado;
    private String estadoGeneral;
    private Date fechaRegistroSamp;
}