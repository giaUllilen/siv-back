package pe.interseguro.siv.common.persistence.db.mysql.repository;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.ViewSolicitud;
import pe.interseguro.siv.common.persistence.db.mysql.response.Perfil;

public interface ViewSolicitudRepository extends JpaRepository<ViewSolicitud, Long> {
	List<ViewSolicitud> findByEstado(int estado);
	List<ViewSolicitud> findByNumeroPropuesta(String numeroPropuesta);
}
