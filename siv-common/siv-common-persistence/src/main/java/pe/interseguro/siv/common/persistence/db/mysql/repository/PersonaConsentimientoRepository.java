package pe.interseguro.siv.common.persistence.db.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.PersonaConsentimiento;

public interface PersonaConsentimientoRepository extends JpaRepository<PersonaConsentimiento, Integer> {

    @Query(
        value = 
            "select    pc " +
            "from      PersonaConsentimiento pc " +
            "where     pc.tipoDocumento = :tipoDocumento " +
            "  and     pc.numeroDocumento = :numeroDocumento " +
            "  "
    )
    public PersonaConsentimiento findByTipoNumeroDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);
} 