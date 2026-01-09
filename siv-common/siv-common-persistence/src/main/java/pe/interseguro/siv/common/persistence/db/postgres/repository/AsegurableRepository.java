package pe.interseguro.siv.common.persistence.db.postgres.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import pe.interseguro.siv.common.persistence.db.postgres.bean.Asegurable;
import pe.interseguro.siv.common.persistence.db.postgres.bean.CotizacionCobertura;
import pe.interseguro.siv.common.persistence.db.postgres.bean.ResultType;

import java.util.List;

public interface AsegurableRepository {

    @Select(
            "select id_cliente, numero_documento from cliente WHERE numero_documento = #{numeroDocumento}  LIMIT 1 "
    )
    @Results(value = {
            @Result(column = "id_cliente", property = "codigoCliente"),
            @Result(column = "numero_documento", property = "numeroDocumento"),
    })
    public Asegurable getAsegurable(@Param("numeroDocumento") String numeroDocumento);

    @Select(
            "select * from fn_save_cli2(#{json}) "
    )
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "err", property = "err"),
            @Result(column = "msg", property = "msg"),
            @Result(column = "errsql", property = "errsql")
    })
    public ResultType save(@Param("json") String json);

}
