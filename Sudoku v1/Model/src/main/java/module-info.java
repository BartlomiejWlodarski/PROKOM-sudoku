module pl.kompo.model {
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires java.sql;
//    requires org.postgresql.jdbc;

    opens pl.kompo.model;
    exports pl.kompo.model;
    exports pl.kompo.model.exceptions;
    exports pl.kompo.model.dao;
    opens pl.kompo.model.dao;
}