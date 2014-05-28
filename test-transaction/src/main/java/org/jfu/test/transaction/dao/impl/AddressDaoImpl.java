package org.jfu.test.transaction.dao.impl;

import org.jfu.test.transaction.dao.AddressDao;
import org.jfu.test.transaction.po.Address;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class AddressDaoImpl extends SqlSessionDaoSupport implements AddressDao {

    @Override
    public Address select(Integer id) {
        return getSqlSession().selectOne(Address.class.getName() + ".select", id);
    }

    @Override
    public int insert(Address address) {
        return getSqlSession().insert(Address.class.getName() + ".insert", address);
    }

    @Override
    public int update(Address address) {
        return getSqlSession().update(Address.class.getName() + ".update", address);
    }

}
