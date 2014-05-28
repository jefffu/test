package org.jfu.test.transaction.dao.impl;

import org.jfu.test.transaction.dao.UserDao;
import org.jfu.test.transaction.po.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

    @Override
    public User select(Integer id) {
        return getSqlSession().selectOne(User.class.getName() + ".select", id);
    }

    @Override
    public int insert(User user) {
        return getSqlSession().insert(User.class.getName() + ".insert", user);
    }

    @Override
    public int update(User user) {
        return getSqlSession().update(User.class.getName() + ".update", user);
    }
}
