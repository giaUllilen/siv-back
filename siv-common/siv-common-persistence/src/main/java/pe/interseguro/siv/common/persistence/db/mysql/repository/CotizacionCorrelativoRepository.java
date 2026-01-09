        package pe.interseguro.siv.common.persistence.db.mysql.repository;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import pe.interseguro.siv.common.persistence.db.mysql.domain.CotizacionCorrelativo;

        import java.util.List;

public interface CotizacionCorrelativoRepository extends JpaRepository<CotizacionCorrelativo, Integer> {

    @Query(
            value =
                    "select    c " +
                            "from 	   CotizacionCorrelativo c " +
                            "  "
    )
    public List<CotizacionCorrelativo> listCorrelativo();
}