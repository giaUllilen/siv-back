package pe.interseguro.siv.common.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el enum PerfilEnum
 */
public class PerfilEnumTest {

    @Test
    public void testPerfilAgenteValores() {
        assertEquals("1", PerfilEnum.PERFIL_AGENTE.getCodigo());
        assertEquals("Agente", PerfilEnum.PERFIL_AGENTE.getPerfil());
    }

    @Test
    public void testPerfilAuditoriaValores() {
        assertEquals("2", PerfilEnum.PERFIL_AUDITORIA.getCodigo());
        assertEquals("Auditoría", PerfilEnum.PERFIL_AUDITORIA.getPerfil());
    }

    @Test
    public void testPerfilOperacionesValores() {
        assertEquals("3", PerfilEnum.PERFIL_OPERACIONES.getCodigo());
        assertEquals("Operaciones", PerfilEnum.PERFIL_OPERACIONES.getPerfil());
    }

    @Test
    public void testCantidadPerfiles() {
        PerfilEnum[] perfiles = PerfilEnum.values();
        assertEquals(3, perfiles.length);
    }

    @Test
    public void testValueOf() {
        PerfilEnum perfil = PerfilEnum.valueOf("PERFIL_AGENTE");
        assertNotNull(perfil);
        assertEquals("1", perfil.getCodigo());
    }

    @Test
    public void testSetCodigo() {
        // Aunque no es común modificar enums, probamos que el setter funcione
        PerfilEnum perfil = PerfilEnum.PERFIL_AGENTE;
        String codigoOriginal = perfil.getCodigo();
        
        perfil.setCodigo("99");
        assertEquals("99", perfil.getCodigo());
        
        // Restaurar valor original
        perfil.setCodigo(codigoOriginal);
    }

    @Test
    public void testSetPerfil() {
        PerfilEnum perfil = PerfilEnum.PERFIL_AGENTE;
        String perfilOriginal = perfil.getPerfil();
        
        perfil.setPerfil("Nuevo Perfil");
        assertEquals("Nuevo Perfil", perfil.getPerfil());
        
        // Restaurar valor original
        perfil.setPerfil(perfilOriginal);
    }
}
