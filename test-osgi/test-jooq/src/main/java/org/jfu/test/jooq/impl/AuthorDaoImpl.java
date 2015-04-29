package org.jfu.test.jooq.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.jfu.test.jooq.api.AuthorDao;
import org.jfu.test.jooq.api.tables.records.AuthorRecord;

import static org.jfu.test.jooq.api.tables.Author.AUTHOR;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.osgi.service.log.LogService;


public class AuthorDaoImpl implements AuthorDao {

    private DataSource dataSource;

    private LogService logService;

    public AuthorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public void removeLogService(LogService logService) {
        this.logService = null;
    }

    @Override
    public AuthorRecord getAuthor(Integer id) {
        try {
            DSLContext create = DSL.using(dataSource.getConnection(), SQLDialect.MYSQL);
            Result<AuthorRecord> result = create.selectFrom(AUTHOR).where(AUTHOR.ID.equal(id)).fetch();

            if (logService != null) {
                logService.log(LogService.LOG_DEBUG, ">>>>>>>> result: " + result.toString());
            }

            if (result.isNotEmpty()) {
                return result.get(0);
            }
        } catch (SQLException e) {
            if (logService != null) {
                logService.log(LogService.LOG_ERROR, "SQLException", e);
            }
        }
        return null;
    }

}
