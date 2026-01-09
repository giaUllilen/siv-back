package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.response.Perfil;

public interface UsuarioPerfilRepository extends JpaRepository<Multitabla, Long> {
	
	
	@Query(
		value =
			"select    new pe.interseguro.siv.common.persistence.db.mysql.response.Perfil(m.idMultitabla, m.codigo, m.valor)  " + 
			"from 	   Multitabla m " +
			"INNER JOIN UsuarioPerfil u ON u.perfil = m.codigo " + 
			"where 	   m.codigoTabla = '009' " + 
			"  and	   m.estadoTabla = '1' " + 
			"  and	   m.estado = '1' " + 
			"  and	   m.codigo is not null " +
			"  and	   u.usuario = :usuario " +
			" order by  m.orden  "
	)
	public Perfil findByUsuario(@Param("usuario") String usuario);
	
}
