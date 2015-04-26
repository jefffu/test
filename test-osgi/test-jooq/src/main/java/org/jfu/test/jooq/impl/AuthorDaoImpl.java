package org.jfu.test.jooq.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.jfu.test.jooq.api.Author;
import org.jfu.test.jooq.api.AuthorDao;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class AuthorDaoImpl implements AuthorDao {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Author getAuthor(Long id) {
        // TODO Auto-generated method stub
        try {
            DSLContext create = DSL.using(dataSource.getConnection(), SQLDialect.MYSQL);
            Result<Record> result = create.select().fetch();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
