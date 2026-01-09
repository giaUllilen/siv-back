package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.bean.PersonaUbigeoBean;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

	@Query(
			value =
				"select    p " +
				"from 	   Persona p " +
				"where 	   p.tipoDocumento = :tipoDocumento " +
				"  and	   p.numeroDocumento = :numeroDocumento " +
				"  "
		)
	public Persona findByTipoNumeroDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);

	@Query(
			value =
				"select    p " +
				"from 	   Persona p " +
				"where	   p.numeroDocumento = :numeroDocumento " +
				"  "
		)
	public Persona findByNumeroDocumento(@Param("numeroDocumento") String numeroDocumento);
	@Query(
			value =
					"SELECT  new pe.interseguro.siv.common.bean.PersonaUbigeoBean(p.numeroDocumento,\n" +
							"m.valor as departamento ,\n" +
							"m2.valor as provincia ,\n" +
							"m3.valor as distrito) \n" +
							"FROM Persona p \n" +
							"inner join Multitabla m \n" +
							"on m.codigo =p.departamento and m.codigoTabla = :depa \n" +
							"inner join Multitabla m2 \n" +
							"on m2.codigo =p.provincia and m2.codigoTabla =:prov \n" +
							"inner join Multitabla m3 \n" +
							"on m3.codigo =p.distrito and m3.codigoTabla =:dist \n" +
							"where p.numeroDocumento = :numeroDocumento " +
							"  "
	)
	PersonaUbigeoBean findByNumeroDocumentoUbigeo(@Param("numeroDocumento") String numeroDocumento,
												  @Param("depa") String depa,
												  @Param("prov") String prov,
												  @Param("dist") String dist);
}
