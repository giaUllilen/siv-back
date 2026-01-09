package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResultType {
    private int id;
    private int err;
    private String msg;
    private String errsql;
}
