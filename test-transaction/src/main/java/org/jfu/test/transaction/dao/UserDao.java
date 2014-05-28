package org.jfu.test.transaction.dao;

import org.jfu.test.transaction.po.User;

public interface UserDao {

    User select(Integer id);

    int insert(User user);

    int update(User user);
}
