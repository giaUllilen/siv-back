        package pe.interseguro.siv.common.persistence.db.mysql.domain;

        import lombok.AccessLevel;
        import lombok.AllArgsConstructor;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        import javax.persistence.*;
        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

        import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "cotizacion_correlativo", catalog = "siv_db")
public class CotizacionCorrelativo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name = "correlativo", unique = true, nullable = false)
    private int correlativo;

    public int getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public CotizacionCorrelativo() {
    }
}