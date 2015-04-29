package org.jfu.test.jooq.api;

import org.jfu.test.jooq.api.tables.records.AuthorRecord;

public interface AuthorDao {

    AuthorRecord getAuthor(Integer id);
}
